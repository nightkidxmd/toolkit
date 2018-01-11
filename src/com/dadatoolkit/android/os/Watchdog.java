package com.dadatoolkit.android.os;

import android.os.Message;

import com.dadatoolkit.android.os.HandlerThread.OnHandleListener;


/**
 * 
 * Not finished
 *
 */
public class Watchdog implements OnHandleListener {
	
	private HandlerThread H;
	private static final int MESSAGE_FEED = 0;
	private static final int MESSAGE_RESET = 1;
	private OnDogListener L;
	
	public interface OnDogListener{
		public void feed();
		public void reset();
	}
	
	public Watchdog(long timeout){
		H = new HandlerThread();
		H.setOnHandleListener(this);
	}

	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		switch(msg.what){
			case MESSAGE_FEED:
				L.feed();
				//sendMessage(MESSAGE_RESET,);
				
				break;
			case MESSAGE_RESET:
				L.reset();
				break;
			default:
				break;
		}
	}
	
	private void sendMessage(int what,long delayMillis){
		H.getThreadHandler().removeMessages(what);
		H.getThreadHandler().sendMessageDelayed(H.getThreadHandler().obtainMessage(what), delayMillis);
	}

}
