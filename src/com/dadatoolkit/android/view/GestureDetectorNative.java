package com.dadatoolkit.android.view;

import android.os.Bundle;
import android.view.MotionEvent;

public class GestureDetectorNative {
	
	public static native void init();
	
	/**
	 * 
	 * @return int[] type array
	 */
	
	public static native int[] getSupportedEngine();
	
	public static native int getEngineVersion();
	
	/**
	 * 
	 * @param type
	 * @param event
	 * @param outData
	 * @return
	 */
	public static native boolean getResult(int type,MotionEvent event,Bundle outData);
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	
	public static native boolean hasSupported(int type);
	
	
	
	static {
		System.loadLibrary("DadaGestureEngine");
	}
}
