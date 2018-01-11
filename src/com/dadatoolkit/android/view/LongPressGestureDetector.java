package com.dadatoolkit.android.view;

import android.os.Message;
import android.view.MotionEvent;
/**
 * 
 * @author maoda.xu@samsung.com
 *
 */
public class LongPressGestureDetector extends GestureDetectorBase{

	private OnLongPressListener  mListener;
	
	private final int MESSAGE_LONG_PRESS = 3;
	private final int mLongPressTimeout = 1000; //1s
	private float mStartX;
	private float mStartY;
	
	public interface OnLongPressListener{
		public void onLongPress();
	}
	
	
	public LongPressGestureDetector(GestureDetectorService service) {
		super(service);
		// TODO Auto-generated constructor stub
		H.setOnHandleListener(this);
	}



	@Override
	protected void recogonizing() {
		// TODO Auto-generated method stub
		MotionEvent event = getEvent();
		switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				mStartX = event.getX();
				mStartY = event.getY();
				if(event.getPointerCount() < 2 && mListener != null){
					if(!H.getThreadHandler().hasMessages(MESSAGE_LONG_PRESS)){
						Message msg = H.getThreadHandler().obtainMessage(MESSAGE_LONG_PRESS);
						H.sendMessage(msg, mLongPressTimeout);
					}
				}
	
				break;
		    case MotionEvent.ACTION_MOVE:
		    	if(!validMove(mStartX, mStartY, event.getX(), event.getY())){
		    		reset();
		    	}
		    	break;
			default:
				break;
	}
	
	}

	@Override
	protected void reset() {
		// TODO Auto-generated method stub
	    if(mListener != null){
	    	if(H.getThreadHandler().hasMessages(MESSAGE_LONG_PRESS)){
	    		H.removeMessages(MESSAGE_LONG_PRESS);
	    	}
	    }
	}
	
	public void handleMessage(Message msg){
		super.handleMessage(msg);
		switch(msg.what){
			case MESSAGE_LONG_PRESS:
				mListener.onLongPress();
				recoginzied();
				break;
			default:
				break;
		}
	}
	
	
	public void setListener(OnLongPressListener l){
		mListener = l;
	}



}
