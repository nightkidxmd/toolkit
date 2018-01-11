package com.dadatoolkit.android.view;

import com.dadatoolkit.java.ReflectUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Need permission
 * @author maoda.xu@samsung.com
 *
 */
public class SurfaceControl {
	private static final String CLASS="android.view.SurfaceControl";

	public static Bitmap screenshot(Context context){
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(dm);
		return screenshot(dm.widthPixels,dm.heightPixels);
	}


	public static Bitmap screenshot(int width,int height){
		return (Bitmap) ReflectUtil.inVoke(null, 
				ReflectUtil.getMethod(CLASS, "screenshot", int.class,int.class),
				width,height);
	}

}
