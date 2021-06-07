package edu.uclm.esi.devopsmetrics.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReceiverMQ {

	private static final Logger LOG = LoggerFactory.getLogger(ReceiverMQ.class);
	public static final String RECEIVE_METHOD_NAME = "receiveMessage";

    public void receiveMessage(String message) {
        LOG.info("Received message: ");
        LOG.info(message);
    }
}
