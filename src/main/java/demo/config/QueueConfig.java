package demo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * DirectExchange 를 사용한 Spring RabbitMQ 설정 
 * @author cowboy76
 *
 */
@Configuration
@EnableRabbit
public class QueueConfig {

	public static final String TEST_EXCHANGE = "test-exchange";
	public static final String TEST_QUEUE_NAME = "test.queue";
	public static final String TEST_ROUTING_KEY = "test";

	@Bean
	public SimpleMessageConverter jackson2JsonMessageConverter() {
		return new SimpleMessageConverter();
	}

	@Autowired
	private ConnectionFactory connectionFactory;

	@Bean
	Queue queue() {
		return new Queue(TEST_QUEUE_NAME, false);
	}

	@Bean
	DirectExchange exchange() {
		return new DirectExchange(TEST_EXCHANGE);
	}

	@Bean
	Binding binding(Queue queue, DirectExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(TEST_ROUTING_KEY);
	}

	@Bean
	public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		factory.setMessageConverter(jackson2JsonMessageConverter());
		factory.setConcurrentConsumers(10);
		factory.setMaxConcurrentConsumers(10);

		return factory;
	}

}
