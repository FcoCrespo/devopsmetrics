package edu.uclm.esi.devopsmetrics.utilities;

/**
*
* @author FcoCrespo
* 
*/
public class SleepClass {

	private SleepClass() {

	}
	public static void sleep(long l) throws InterruptedException{
        Thread.sleep(l);
    }
}
