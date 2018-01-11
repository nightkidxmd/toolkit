package com.dadatoolkit.android.app.am;

import com.dadatoolkit.android.su.NotRootException;

import android.app.ActivityManager;
import android.content.Context;

public abstract class  ActivityManagerHelper{
	private static Object mInstance;

	/**
	 * 
	 * @param context
	 * @param isRoot
	 * @return Object   if isRoot==true,cast to {@link ActivityManagerHelper} else
	 *                  cast to {@link ActivityManager}
	 * @throws NotRootException 
	 */
	
	public static Object getInstance(Context context,boolean isRoot) throws NotRootException{
		if(mInstance == null){
			if(isRoot){
				mInstance = new ActivityManagerHelperRoot(context);
			}else{
				mInstance = context.getSystemService(Context.ACTIVITY_SERVICE);
				
				
			}
		}
		return mInstance;
	}
	
	public abstract void killProcess(String packageName);
	public abstract void killAll();
	public abstract void forceStopPackage(String packageName);
}
