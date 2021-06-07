package esi.uclm.esi.devopsmetrics.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 *
 * @author crespo
 */
public class Receiver {
	
	private static final Log LOG = LogFactory.getLog(Receiver.class);
    
    public static final String RECEIVE_METHOD_NAME = "receiveMessage";

    public void receiveMessage(String message) {
        LOG.info("Client has received  \"" + message + '"');
    }
    
}