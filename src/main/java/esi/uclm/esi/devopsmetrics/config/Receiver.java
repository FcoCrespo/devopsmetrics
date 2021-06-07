package esi.uclm.esi.devopsmetrics.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;


/**
 *
 * @author crespo
 */

@Service
public class Receiver {
	
	private static final Log LOG = LogFactory.getLog(Receiver.class);
    
    
	@RabbitListener(queues = "devopsmetrics_queue")
    public void receiveMessage(final Message message) {
        LOG.info("Client has received  \"" + message.toString() + '"');
    }
    
}