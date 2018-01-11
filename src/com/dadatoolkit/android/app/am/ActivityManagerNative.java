package com.dadatoolkit.android.app.am;

import android.os.Binder;
import android.os.IBinder;

import com.dadatoolkit.java.ReflectUtil;

public class ActivityManagerNative {
	private static final String AMN_CLASS = "android.app.ActivityManagerNative";

	private static ActivityManagerNative mInstance;


	private Object AMN;


	private ActivityManagerNative(){
		AMN =  ReflectUtil.inVoke(null, ReflectUtil.getMethod(AMN_CLASS, "getDefault"));
	}

	public static ActivityManagerNative getDefault(){
		if(mInstance == null){
			mInstance = new ActivityManagerNative();

		}

		return mInstance;

	}

	public void setProcessForeground(boolean isForeground){
		IBinder binder = new Binder();
		ReflectUtil.inVoke(AMN, ReflectUtil.getMethod(AMN_CLASS, 
				"setProcessForeground", 
				IBinder.class,int.class,boolean.class),
				binder,android.os.Process.myPid(),isForeground);
	}
	
	public void moveTaskToBack(int taskId){
		ReflectUtil.inVoke(AMN, ReflectUtil.getMethod(AMN_CLASS, 
				"moveTaskToBack", 
				int.class),
				taskId);
	}
	
	public void killUid(int uid,String reason){
		ReflectUtil.inVoke(AMN, ReflectUtil.getMethod(AMN_CLASS, 
				"killUid", 
				int.class,
				String.class),
				uid,reason);
	}

	public void killPids(int[] pids,String reason,boolean secure){
		ReflectUtil.inVoke(AMN, ReflectUtil.getMethod(AMN_CLASS, 
				"killPids", 
				int[].class,
				String.class,
				boolean.class),
				pids,reason,secure);
	}
	
	public void forceStopPackage(String packageName,int userId){
		ReflectUtil.inVoke(AMN, ReflectUtil.getMethod(AMN_CLASS, 
				"forceStopPackage", 
				String.class,
				int.class,
				boolean.class),
				packageName,userId);
	}
}
