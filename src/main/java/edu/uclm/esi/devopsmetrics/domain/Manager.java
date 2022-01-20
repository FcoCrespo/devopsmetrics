package edu.uclm.esi.devopsmetrics.domain;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
*
* @author FcoCrespo
* 
*/
@Service
public class Manager {
	
	private static final Log LOG = LogFactory.getLog(Manager.class);
	private static BlockingQueue<Thread> commitsQueue = new LinkedBlockingDeque<>();
	private static BlockingQueue<Thread> orderQueue = new LinkedBlockingDeque<>();
	private static BlockingQueue<Thread> productQueue = new LinkedBlockingDeque<>();
	private static BlockingQueue<Thread> testQueue = new LinkedBlockingDeque<>();
	private static BlockingQueue<Thread> issueQueue = new LinkedBlockingDeque<>();
	
	
	private Manager() {
	
	}
	
	private static class ManagerHolder {
		static Manager singleton=new Manager();
	}
	
	public static Manager get() {
		return ManagerHolder.singleton;
	}

	public void addElementCommits(Thread th) {
		LOG.info("Adding task to commits Queue");
		commitsQueue.add(th);
	}
	
	@Scheduled(fixedDelay = 2000)
	public void executeThreadsCommits() throws InterruptedException {
		if(!commitsQueue.isEmpty()) {
			Thread thread = commitsQueue.poll();
			thread.start();
			thread.join();
			LOG.info("Finish task of Commits Queue: "+thread.getId());
		}
		
	}
	
	public void addElementOrder(Thread th) {
		LOG.info("Adding task to order Queue");
		orderQueue.add(th);
	}
	
	@Scheduled(fixedDelay = 2000)
	public void executeThreadsOrder() throws InterruptedException {
		if(!orderQueue.isEmpty()) {
			Thread thread = orderQueue.poll();
			thread.start();
			thread.join();
			LOG.info("Finish task of order Queue: "+thread.getId());
		}
		
	}
	
	public void addElementProduct(Thread th) {
		LOG.info("Adding task to product Queue");
		productQueue.add(th);
	}
	
	@Scheduled(fixedDelay = 2000)
	public void executeThreadsProduct() throws InterruptedException {
		if(!productQueue.isEmpty()) {
			Thread thread = productQueue.poll();
			thread.start();
			thread.join();
			LOG.info("Finish task of product Queue: "+thread.getId());
		}
		
	}
	
	public void addElementTest(Thread th) {
		LOG.info("Adding task to order Queue");
		testQueue.add(th);
	}
	
	@Scheduled(fixedDelay = 2000)
	public void executeThreadsTest() throws InterruptedException {
		if(!testQueue.isEmpty()) {
			Thread thread = testQueue.poll();
			thread.start();
			thread.join();
			LOG.info("Finish task of test Queue: "+thread.getId());
		}
		
	}
	
	public void addElementIssue(Thread th) {
		LOG.info("Adding task to issue Queue");
		issueQueue.add(th);
	}
	
	@Scheduled(fixedDelay = 2000)
	public void executeThreadsIssue() throws InterruptedException {
		if(!issueQueue.isEmpty()) {
			Thread thread = issueQueue.poll();
			thread.start();
			thread.join();
			LOG.info("Finish task of issue Queue: "+thread.getId());
		}
		
	}
}
