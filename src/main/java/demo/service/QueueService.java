package demo.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import demo.domain.QueueObject;


@Component
public class QueueService {
		
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Scheduled(fixedDelay = 5000)
	public void sender() {
	   // QueueObject ex = new QueueObject();	    
	   // rabbitTemplate.convertAndSend("test-queue",ex);
	  }
	
	@RabbitListener(queues = "test-queue")
	public void handleMessage(QueueObject ex) {
		System.out.println(ex);
		
	}
}
