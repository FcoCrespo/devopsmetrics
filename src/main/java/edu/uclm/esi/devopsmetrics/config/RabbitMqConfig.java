package edu.uclm.esi.devopsmetrics.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author FcoCrespo
 * src: https://www.rabbitmq.com/tutorials/tutorial-six-spring-amqp.html
 */
@Configuration
public class RabbitMqConfig {
    
    public static final String EXCHANGE_NAME = "devopsmetrics_exchange";
    public static final String ROUTING_KEY = "devopsmetrics_routing_key";

    public static final String QUEUE_NAME = "devopsmetrics_queue";
    public static final boolean IS_DURABLE_QUEUE = false;

    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME, IS_DURABLE_QUEUE);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        final SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(QUEUE_NAME);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(ReceiverMQ receiver) {
        return new MessageListenerAdapter(receiver, ReceiverMQ.RECEIVE_METHOD_NAME);
    }

    @Bean
    public ReceiverMQ receiver() {
        return new ReceiverMQ();
    }

}