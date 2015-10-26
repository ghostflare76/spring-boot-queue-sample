package demo;

import static demo.config.QueueConfig.TEST_EXCHANGE;
import static demo.config.QueueConfig.TEST_QUEUE_NAME;
import static demo.config.QueueConfig.TEST_ROUTING_KEY;

import java.text.MessageFormat;
import java.util.Date;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/direct")
public class QueueController {

	
	@Autowired
	AmqpTemplate amqpTeamplte;
	
	@Autowired
	private Environment environment;

	@RequestMapping("/send")
	public void send(@RequestParam(required = false) String message) {
		
		if (StringUtils.isEmpty(message)) {
			message = MessageFormat.format("Hello World from {0} ({1,time})", environment.getProperty("spring.rabbitmq.host"), new Date());
		}		
		
		for (int i =0 ; i < 100; i++) {
			amqpTeamplte.convertAndSend(TEST_EXCHANGE, TEST_ROUTING_KEY, message);
		}
		
	}
	
	@RequestMapping("/receive")
	public String receive() {
		String message = (String)amqpTeamplte.receiveAndConvert(TEST_QUEUE_NAME);
		return MessageFormat.format("{0} > {1}", environment.getProperty("spring.rabbitmq.host"), message);
	}
}
