package com.dadatoolkit.android.app;

import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import com.dadatoolkit.android.os.HandlerThread;
import com.dadatoolkit.android.os.HandlerThread.OnHandleListener;
import com.dadatoolkit.android.utils.Dlog;

public class InputHelper implements OnHandleListener {
	private InputManager mIM;
	private HandlerThread H;
	private static final long DEFAULT_TOUCH_TIME_OUT = 200;//ms


	private static final int TYPE_KEY_EVENT   = 0;
	private static final int TYPE_MOTION_EVENT = 1;


	private static final String DATA_DURATION="duration";

	public InputHelper(){
		mIM = InputManager.getInstance();
		H.setOnHandleListener(this);
		H.start();

	}

	public void pressKey(int keycode,long duration){
		Message msg = H.getThreadHandler().obtainMessage(TYPE_KEY_EVENT);
		KeyEvent keyevent = new KeyEvent(KeyEvent.ACTION_DOWN,keycode);
		msg.obj  = keyevent;
		Bundle data = new Bundle();
		data.putLong(DATA_DURATION, duration);
		msg.sendToTarget();
	}



	public void pressKey(int keycode){
		pressKey(keycode,DEFAULT_TOUCH_TIME_OUT);
	}

	@Override
	public void handleMessage(Message msg) {
		switch(msg.what){
		case TYPE_KEY_EVENT:
			handleKeyEvent(msg);
			break;
		case TYPE_MOTION_EVENT:
			break;
		default:
			break;
		}
	}


	private void handleKeyEvent(Message msg){
		KeyEvent keyevent = (KeyEvent)msg.obj;
		switch(keyevent.getAction()){
		case KeyEvent.ACTION_DOWN:
			mIM.injectInputEvent(keyevent, 0);
			KeyEvent upKeyEvent = new KeyEvent(KeyEvent.ACTION_UP,keyevent.getKeyCode());
			Message msg2 = H.getThreadHandler().obtainMessage(TYPE_KEY_EVENT);
			msg2.obj  = upKeyEvent;
			H.getThreadHandler().sendMessageDelayed(msg2, msg.getData().getLong(DATA_DURATION));
			break;
		case KeyEvent.ACTION_UP:
			mIM.injectInputEvent(keyevent, 0);
			break;
		default:
			Dlog.e(getClass(), msg.toString());
			break;
		}

	}

// TODO
//	private void handleMotionEvent(){
//
//	}

}
