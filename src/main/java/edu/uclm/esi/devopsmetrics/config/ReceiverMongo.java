package edu.uclm.esi.devopsmetrics.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReceiverMongo {
	private static final Logger LOG = LoggerFactory.getLogger(ReceiverMongo.class);
	public static final String RECEIVE_METHOD_NAME = "receiveMessageMongo";

    public void receiveMessage(String message) {
        LOG.info("Received message: ");
        LOG.info(message);
    }
}
