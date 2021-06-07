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
 * @author crespo
 */
@Configuration
public class RabbitMqMongo {
    
    public static final String EXCHANGE_NAME = "devopsmetricsmongo_exchange";
    public static final String ROUTING_KEY = "devopsmetricsmongo_routing_key";

    private static final String QUEUE_NAME = "devopsmetricsmongo_queue";
    private static final boolean IS_DURABLE_QUEUE = false;

    @Bean
    Queue queueMongo() {
        return new Queue(QUEUE_NAME, IS_DURABLE_QUEUE);
    }

    @Bean
    TopicExchange exchangeMongo() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    Binding bindingMongo(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    SimpleMessageListenerContainer containerMongo(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        final SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(QUEUE_NAME);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapterMongo(ReceiverMongo receiver) {
        return new MessageListenerAdapter(receiver, ReceiverMongo.RECEIVE_METHOD_NAME);
    }

    @Bean
    ReceiverMongo receiverMongo() {
        return new ReceiverMongo();
    }

}