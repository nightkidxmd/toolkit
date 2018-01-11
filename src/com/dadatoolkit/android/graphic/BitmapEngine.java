package com.dadatoolkit.android.graphic;

public class BitmapEngine {
	public static int FILTER_MODE_TOGREY = 0x1;
	public static int FILTER_MODE_FILTEROUT_RED   = FILTER_MODE_TOGREY << 1;
	public static int FILTER_MODE_FILTEROUT_GREEN = FILTER_MODE_TOGREY << 2;
	public static int FILTER_MODE_FILTEROUT_BLUE  = FILTER_MODE_TOGREY << 3;
	public static int FILTER_MODE_NEGATIVE        = FILTER_MODE_TOGREY << 4;
	

	public  native static  int[] nativeEffect(int[] buffer,int width,int height);
	
	
static{
	System.loadLibrary("BitmapEngine");
}
}
