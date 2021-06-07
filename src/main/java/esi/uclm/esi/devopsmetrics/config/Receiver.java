package esi.uclm.esi.devopsmetrics.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

/**
 *
 * @author crespo
 */
@Service
public class Receiver {
	
	private static final Log LOG = LogFactory.getLog(Receiver.class);
    
    public static final String RECEIVE_METHOD_NAME = "receiveMessage";

    @RabbitListener(queues=RabbitMqConfig.QUEUE_NAME)
    public void receiveMessage(final Message <String> message) {
        LOG.info("Client has received  \"" + message.toString() + '"');
    }
    
}