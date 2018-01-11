package com.dadatoolkit.android.app;
/**
 * 2014-01-27 maoda.xu@samsung.com add singleton
 *                                 add white or black list mode
 * 2014-01-26 maoda.xu@samsung.com add first version
 */
import java.util.ArrayList;
import java.util.List;
import com.dadatoolkit.android.os.HandlerThread;
import com.dadatoolkit.android.os.HandlerThread.OnHandleListener;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;

/**
 * 
 * @author maoda.xu@samsung.com
 *
 */
public class ProgramMonitorFactory implements OnHandleListener {
	private ActivityManager mAM;
	private HandlerThread mMonitorThread;
	private final long DEFAULT_INTERVEL = 200;//ms
	public static final  String MESSAGE_DATA_TOP_ACTIVITY = "top_actvity";
	public static  final String MESSAGE_DATA_TOP_PID = "pid";
	private List<OnProgramChangedListener> mOnProgramChangedListeners = new ArrayList<OnProgramChangedListener>();
	private List<String> mAppList = new ArrayList<String>();
	private String mLastActivityName = "";
	private long mMonitorIntervel = DEFAULT_INTERVEL;
	private static ProgramMonitorFactory mInstance;
	private int mMode = MODE_WHITELIST;
	
	
	public static final int MESSAGE_CHECK_ACTIVIVTY = 1;
	public static final int MESSAGE_TOP_ACTIVITY_CHANGED = 2;
	
	public static final int MODE_WHITELIST = 1;
	public static final int MODE_BLACKLIST = 2;
	

	public interface OnProgramChangedListener{
		public boolean onChanged(String topActvityName,int pid);
	}
	
	private ProgramMonitorFactory(Context context,int mode) {
		mAM = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		initMonitorThread();	
	}
	
	/**
	 * Singleton
	 * @param context
	 * @param mode  MODE_WHITELIST<br>
	 *              MODE_BLACKLIST
	 * @return
	 */

	
	public static ProgramMonitorFactory getInstance(Context context,int mode){
		if(mInstance == null){
			mInstance = new ProgramMonitorFactory(context,mode);
		}	
		return mInstance;	
	}
	
	/**
	 * start monitor
	 */
	
	public void startMonitor(){
		startMonitor(-1);
	}
	
	/**
	 * start monitor
	 * @param interval default value is 500ms
	 */
	
	public void startMonitor(long interval){
		if(interval != -1){
			mMonitorIntervel = interval;
		}
		
		if(mMonitorThread == null){
			initMonitorThread();
		}
		if(!mMonitorThread.isAlive()){
			mMonitorThread.start();
		}
	}
	
	/**
	 * stop monitor
	 */
	
	public void stopMonitor(){
		if(mMonitorThread != null && mMonitorThread.isAlive()){
			mMonitorThread.removeMessages(MESSAGE_CHECK_ACTIVIVTY);
			mMonitorThread.removeMessages(MESSAGE_TOP_ACTIVITY_CHANGED);
			mMonitorThread.quit();
			mMonitorThread= null;
		}
		mInstance = null;
	}
	
	/**
	 * set OnProgramChangedListener
	 * @param l
	 */
	
	public void setOnProgramChangedListener(OnProgramChangedListener l){
		if(!mOnProgramChangedListeners.contains(l)){
			mOnProgramChangedListeners.add(l);
		}
	}
	
	/**
	 * 
	 * @param packageName single package
	 */
	
	public void addApp(String packageName){
		if(!mAppList.contains(packageName)){
			mAppList.add(packageName);
		}
	}
	
	/**
	 * 
	 * @param packageNames String[] packages
	 */
	
	public void addApp(String[] packageNames){
		for(String p:packageNames){
			if(!mAppList.contains(p)){
				mAppList.add(p);
			}
		}
	}
	
	/**
	 * 
	 * @param packageNames List<String> packages
	 */
	
	public void addApp(List<String> packageNames){
		for(int i=0;packageNames != null && i<packageNames.size();i++){
			String packageName = packageNames.get(i);
			if(!mAppList.contains(packageName)){
				mAppList.add(packageName);
			}
		}
		
	}
	
	private void initMonitorThread(){
		if(mMonitorThread == null){
			mMonitorThread = new HandlerThread(new Runnable(){
				public void run() {
					Message msg = mMonitorThread.getThreadHandler().obtainMessage(MESSAGE_CHECK_ACTIVIVTY);
					mMonitorThread.sendMessage(msg);
				}
				
			});
			
			mMonitorThread.setOnHandleListener(this);
			
		}
	}
	

	
	private void checkActvity(){
		List<RunningTaskInfo> taskInfo = mAM.getRunningTasks(10);
		ComponentName topActivity=taskInfo.get(0).topActivity;
			String topActivityName = topActivity.getClassName();// topActivity.getPackageName();
			if(!mLastActivityName.equals(topActivityName) && 
					(mMode == MODE_WHITELIST? !mAppList.contains(topActivityName):mAppList.contains(topActivityName)) ){
				mLastActivityName = topActivityName;
				mMonitorThread.removeMessages(MESSAGE_TOP_ACTIVITY_CHANGED);
				int pid = getPid(topActivity.getPackageName());
				Message msg = new Message(); 
				msg.what = MESSAGE_TOP_ACTIVITY_CHANGED;
				Bundle data = new Bundle();
				data.putString(MESSAGE_DATA_TOP_ACTIVITY, topActivityName);
				data.putInt(MESSAGE_DATA_TOP_PID, pid);
				msg.setData(data);
				mMonitorThread.sendMessage(msg);
			}
			mMonitorThread.removeMessages(MESSAGE_CHECK_ACTIVIVTY);
			Message msg = new Message();
			msg.what = MESSAGE_CHECK_ACTIVIVTY;
			mMonitorThread.sendMessage(msg,mMonitorIntervel);
	}
	
	private int getPid(String pakcageName){
		int pid = 0;
		List<RunningAppProcessInfo> appProcessInfo = mAM.getRunningAppProcesses();
		for(int i=0;i<appProcessInfo.size();i++){
			if(appProcessInfo.get(i).processName.equals(pakcageName)){
				pid =appProcessInfo.get(i).pid;
				break;
			}
		}
		return pid;

	}


	public void handleMessage(Message msg) {
		switch(msg.what){
		case MESSAGE_TOP_ACTIVITY_CHANGED:
			String topActvityName = msg.getData().getString(MESSAGE_DATA_TOP_ACTIVITY);
			int pid = msg.getData().getInt(MESSAGE_DATA_TOP_PID);
			 for(int i =0;mOnProgramChangedListeners != null && i < mOnProgramChangedListeners.size();i++){
				 mOnProgramChangedListeners.get(i).onChanged(topActvityName,pid);
			 }
		case MESSAGE_CHECK_ACTIVIVTY:
			checkActvity();
			break;
		default:
			break;
		}
	}

}
