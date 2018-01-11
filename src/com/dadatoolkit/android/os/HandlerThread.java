package com.dadatoolkit.android.os;
/**
 * 2014-01-24 maoda.xu@samsung.com first version
 */
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Thread with Looper(NOT MAIN THREAD LOOPER)<br>
 * @author maoda.xu@samsung.com
 *
 */
public class HandlerThread extends Thread {
	public static final int MESSAGE_QUIT = -999;
	private List<OnHandleListener> mHandleListeners = new ArrayList<OnHandleListener>();
	private ThreadHandler H = null;
    private Runnable target;
	public interface OnHandleListener{
		public void handleMessage(Message msg);
	}
	
	public HandlerThread(){
		super();
	}
	
	
	public HandlerThread(String name){
		super(name);
	}
	
	
	
	
	/**
	 * If use HandlerThread() as constuctor, <br>
	 * do not foreget to setRunnable(Runnable run)
	 * @param run
	 */
	
	public HandlerThread(Runnable run){
		target = run;
	}
	/**
	 * Call setOnHandleListener then you could use this<br>
	 * to sendMessage to this handler.<br>
	 * And handle it in handleMessage(Message msg)
	 * 
	 * @param msg
	 */
	
	public void sendMessage(Message msg){
		H.sendMessage(msg);
	}
	
	/**
	 * 
	 * @param msg
	 * @param delayMillis
	 */
	
	public void sendMessage(Message msg, long delayMillis){
		H.sendMessageDelayed(msg, delayMillis);
	}
	
	
	/**
	 * 
	 * @param what
	 */
	
	public void removeMessages(int what){
		H.removeMessages(what);
	}
	
	
	public final ThreadHandler getThreadHandler(){
		return H;
	}
	
	
	/**
	 * set OnHandleListener
	 * @param l
	 */
	
	public void setOnHandleListener(OnHandleListener l){
		if(!mHandleListeners.contains(l))
			mHandleListeners.add(l);
	}
	/**
	 * The runnable between Looper.prepare() & Looper.myLooper().loop()
	 * @param run
	 */
	public void setRunnable(Runnable run){
		target = run;
	}
	
	/**
	 * Call it when HandlerThread is no longer used.
	 */
	
	public void quit(){
		Message msg = H.obtainMessage(MESSAGE_QUIT);
		H.sendMessage(msg);
	}
	
	@Override
	public void run(){
		Looper.prepare();
		if(H == null){
			H = new ThreadHandler();
		}
		

	    if(target != null){
	    	target.run();
	    }
		Looper.loop();
	}
	
	@SuppressLint("HandlerLeak")
	public class ThreadHandler extends Handler{
			
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case MESSAGE_QUIT:
				Looper.myLooper().quit();
			default:
				for(int i=0;i<mHandleListeners.size();i++){
					mHandleListeners.get(i).handleMessage(msg);
				}
				break;
				
			}
		}
	}

}
