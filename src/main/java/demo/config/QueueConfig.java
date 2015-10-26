package demo.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author cowboy76
 *
 */
@Configuration
@EnableRabbit
public class QueueConfig {

	public static final String TEST_EXCHANGE = "fluentd_exchange";
	public static final String TEST_QUEUE_NAME = "fluentd.queue";
	public static final String TEST_ROUTING_KEY = "nginx.access";
	
	@Bean
	public Jackson2JsonMessageConverter  jackson2JsonMessageConverter() {
		Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
	
		return converter;
	}

	@Bean
	public AmqpAdmin amqpAdmin() {		
		return new RabbitAdmin(connectionFactory);
	}
	
	@Autowired
	private ConnectionFactory connectionFactory;
	
	@Bean
	public RabbitTemplate rabbitTemplate() {
		RabbitTemplate template = new RabbitTemplate(connectionFactory);
		//The routing key is set to the name of the queue by the broker for the default exchange.
		template.setExchange(TEST_EXCHANGE);
		template.setRoutingKey(TEST_ROUTING_KEY);

		template.setQueue(TEST_QUEUE_NAME);
	//	template.setMessageConverter(jackson2JsonMessageConverter());
		return template;
	}

	@Bean
	public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
	//	factory.setMessageConverter(jackson2JsonMessageConverter());
		factory.setConcurrentConsumers(10);
		factory.setMaxConcurrentConsumers(10);
		return factory;
	}

}
