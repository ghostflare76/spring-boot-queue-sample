package demo;

import static demo.config.QueueConfig.TEST_QUEUE_NAME;

import java.text.MessageFormat;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import demo.domain.QueueObject;

public class QueueTest extends SpringBootQueueApplicationTests {
	
	@Autowired
	RabbitTemplate rabbitTemplate;	
	
	@Autowired
	private Environment environment;
	

	@Test
	public void testSender() {
		long start = System.currentTimeMillis();
		for (int i =0 ; i < 1000; i++) {
			QueueObject ex = new QueueObject();
			rabbitTemplate.convertAndSend("test-queue", ex);
		}	
		long elapsedTime = System.currentTimeMillis() - start;
		System.out.println("testSender elapsed time -> " + elapsedTime);
	}
	
	@Test
	public void testSingleReceive() {
		long start = System.currentTimeMillis();
		for (int i =0 ; i < 1000; i++) {
			String message = (String)rabbitTemplate.receiveAndConvert(TEST_QUEUE_NAME);			
			System.out.println(MessageFormat.format("{0} > {1}", environment.getProperty("spring.rabbitmq.host"), message));	
		}	
		long elapsedTime = System.currentTimeMillis() - start;
		System.out.println("testSingleReceive elapsed time -> " + elapsedTime);
	}
	
	@Test
	public void testMultiReceive() throws Exception {
		
		CountDownLatch latch = new CountDownLatch(100);		
		long start = System.currentTimeMillis();
		for (int i = 0; i < 100; i++) {
			new Thread(new Worker(latch)).start();
		}
		
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
				
				System.out.println(MessageFormat.format("{0} > {1}", environment.getProperty("spring.rabbitmq.host"), message));			
			} finally {
				latch.countDown();
			}
		
		}
		
	}
	
	public void testReceiver() {
		
	}
}
