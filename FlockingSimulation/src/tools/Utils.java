package tools;

public class Utils {
	
	public static void pause(double time) {
		try {
			Thread.sleep((long)time);
		} catch (InterruptedException e) {
			// We are happy with interruptions, so do not report exception
		}
	}
}
