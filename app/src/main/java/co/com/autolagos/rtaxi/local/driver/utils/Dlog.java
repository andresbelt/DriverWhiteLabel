package co.com.autolagos.rtaxi.local.driver.utils;

import android.util.Log;


import co.com.autolagos.rtaxi.local.driver.base.AppBase;
//clase muestra LOG con DEBUG
public class Dlog {

	static final String TAG = "LogRtaxiDriver";

	
	 /** Log Level Error **/
	public static final void e(String tag,String message) {
		if (AppBase.DEBUG)Log.e(TAG, buildLogMsg(message));
	}
	 /** Log Level Warning **/
	public static final void w(String tag,String message) {
		if (AppBase.DEBUG)Log.w(TAG, buildLogMsg(message));
	}
	 /** Log Level Information **/
	public static final void i(String tag,String message) {
		if (AppBase.DEBUG)Log.i(TAG, buildLogMsg(message));
	}
	/** Log Level Debug **/
	public static final void d(String tag,String message) {
		if (AppBase.DEBUG)Log.d(TAG, buildLogMsg(message));
	}
	/** Log Level Verbose **/
	public static final void v(String tag,String message) {
		if (AppBase.DEBUG)Log.v(TAG, buildLogMsg(message));
	}
	

	public static String buildLogMsg(String message) {

		StackTraceElement ste = Thread.currentThread().getStackTrace()[4];

		StringBuilder sb = new StringBuilder();

		sb.append("[");
		sb.append(ste.getFileName().replace(".java", ""));
		sb.append("::");
		sb.append(ste.getMethodName());
		sb.append("]");
		sb.append(message);

		return sb.toString();

	}

}