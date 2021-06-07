package esi.uclm.esi.devopsmetrics.config;



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
public class CustomMessageSender {

	private static final Log LOG = LogFactory.getLog(CustomMessageSender.class);

    private final RabbitTemplate rabbitTemplate;

    public CustomMessageSender(final RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Scheduled(fixedDelay = 2500L)
    public void sendMessage() {
    	String message = "Hello there!";
    	LOG.info("Sending message...");
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY, message);
    }
}