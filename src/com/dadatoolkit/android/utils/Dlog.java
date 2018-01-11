package com.dadatoolkit.android.utils;



import android.util.Log;


public class Dlog  {
	public static void e(Class<?> clazz,String msg){
		Log.e(clazz.getSimpleName(), msg);
	}

	/**
	 * Log into kernel
	 * @param clazz
	 * @param msg
	 */
	public static void k(Class<?> clazz,String msg){
		Log.e(clazz.getSimpleName(), "!@"+clazz.getSimpleName()+":"+msg);
	}
	
	/**
	 * Print java stack
	 */

	public static void printStack(){
		StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		Dlog.e(Dlog.class,"-----------Stack Begin-------------");
		for(int i=3;i<ste.length;i++){
			Dlog.e(Dlog.class, "at "+ste[i].getClassName()+"."+ste[i].getMethodName()+"("+ste[i].getFileName()+":"+ste[i].getLineNumber()+")");
		}
		
		Dlog.e(Dlog.class,"-----------Stack End-------------");
	}
}
