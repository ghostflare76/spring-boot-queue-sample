package demo;

import static demo.config.QueueConfig.TEST_QUEUE_NAME;

import java.text.MessageFormat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import demo.domain.QueueObject;

public class QueueTest extends SpringBootQueueApplicationTests {

	private final static int MAX_QUQUE = 100000;

	@Autowired
	RabbitTemplate rabbitTemplate;

	@Autowired
	private Environment environment;

	@Test
	public void testSender() {
		long start = System.currentTimeMillis();
		AtomicInteger counter = new AtomicInteger();
		for (int i = 0; i < MAX_QUQUE; i++) {
			QueueObject ex = new QueueObject(counter.incrementAndGet(), "RabbitMq Spring JSON Sample");
			rabbitTemplate.convertAndSend(TEST_QUEUE_NAME, ex.toString());
		}
		long elapsedTime = System.currentTimeMillis() - start;
		System.out.println("testSender elapsed time -> " + elapsedTime);
	}

	@Test
	public void testSingleReceive() {
		long start = System.currentTimeMillis();
		for (int i = 0; i < MAX_QUQUE; i++) {
			String message = (String)rabbitTemplate.receiveAndConvert(TEST_QUEUE_NAME);
			System.out.println(
				MessageFormat.format("{0} > {1}", environment.getProperty("spring.rabbitmq.host"), message));
		}
		long elapsedTime = System.currentTimeMillis() - start;
		System.out.println("testSingleReceive elapsed time -> " + elapsedTime);
	}

	@Test
	public void testMultiReceive() throws Exception {

		CountDownLatch latch = new CountDownLatch(MAX_QUQUE);
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		long start = System.currentTimeMillis();

		for (int i = 0; i < MAX_QUQUE; i++) {
			Worker worker = new Worker(latch);
			executorService.execute(worker);
		}

		executorService.shutdown();

		try {
			latch.await();
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
		long elapsedTime = System.currentTimeMillis() - start;
		System.out.println("testReceive elapsed time -> " + elapsedTime);
	}

	class Worker implements Runnable {

		private CountDownLatch latch;

		public Worker(CountDownLatch latch) {
			this.latch = latch;
		}

		@Override
		public void run() {
			try {
				String message = (String)rabbitTemplate.receiveAndConvert(TEST_QUEUE_NAME);

				System.out.println(MessageFormat.format("{0} : {1} > {2}", Thread.currentThread().getName(),
					environment.getProperty("spring.rabbitmq.host"), message));

			} finally {
				latch.countDown();
			}
		}

	}
}
