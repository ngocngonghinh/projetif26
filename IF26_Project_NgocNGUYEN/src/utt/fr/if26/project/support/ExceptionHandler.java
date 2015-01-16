package utt.fr.if26.project.support;

import android.util.Log;

public class ExceptionHandler {
	public static final String LOCATION = "Exception in ";

	public static ExceptionHandler getInstance() {
		if (null == instance) {
			instance = new ExceptionHandler();
		}
		return instance;
	}

	private ExceptionHandler() {
	}

	private static ExceptionHandler instance;

	/**
	 * Personal handler of an exception
	 * 
	 * @param e
	 */
	public void mHandler(Exception e) {
		for (StackTraceElement ste : e.getStackTrace()) {
			Log.e(LOCATION + ste.getClassName(), ste.getMethodName() + ", " + ste.getLineNumber());
		}
	}
}
