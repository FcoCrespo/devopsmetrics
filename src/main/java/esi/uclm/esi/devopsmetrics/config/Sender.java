package esi.uclm.esi.devopsmetrics.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
public class Sender {

    private static final Logger LOG = LoggerFactory.getLogger(Sender.class);
    
    @Autowired
	RabbitTemplate rabbitTemplate;

    @Scheduled(fixedDelay = 2500)
    public void sendMessage() {
    	String saludo= "Hello There!";
        String message = "{\"message\":\"" + saludo + "\"}";
        LOG.info("Sending message...");
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY, message);
    }
}