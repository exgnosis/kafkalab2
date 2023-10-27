package x.utils;

import java.util.Random;
import java.text.NumberFormat;

public class MyUtils {
	
	private static final Random random = new Random();
	private static final NumberFormat numberFormat = NumberFormat.getInstance();
	
	public static void randomDelay (int upto) {
		randomDelay(0,upto);
		
	}
	public static void randomDelay (int from, int upto) {
		try {
			int sleepInterval = from + random.nextInt(upto-from+1);
			Thread.sleep(sleepInterval);
		} catch (InterruptedException e) { }
		
	}

	public static void sleepFor (int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) { }
		
	}
	
	public static String formatNumber(long number) {
		return numberFormat.format(number);
	}
	

}
