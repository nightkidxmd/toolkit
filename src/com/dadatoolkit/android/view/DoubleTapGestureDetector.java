package com.dadatoolkit.android.view;

import android.os.Message;
import android.view.MotionEvent;

/**
 * Better not use together with {@link ShortPressGestureDetector},
 * Or it'll trigger ShortPress first then DoubleTap
 * 
 * @author maoda.xu@samsung.com
 *
 */

public class DoubleTapGestureDetector extends GestureDetectorBase {
	private OnDoubleTapListener  mListener;
	private final int mDoubleTabTimeout = 500;  //30ms;
	private int mTabCount = 0;
	private Object mLock = new Object();
	
	private final int MESSAGE_RESET_DOUBLE_TAP = 4;
	
	public interface OnDoubleTapListener{
		public void onDoubleTap();
	}
	
	
	public DoubleTapGestureDetector(GestureDetectorService service) {
		super(service);
		// TODO Auto-generated constructor stub
	}




	@Override
	protected void recogonizing() {
		// TODO Auto-generated method stub
		MotionEvent event = getEvent();
		switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				if(event.getPointerCount() < 2){
					if(mListener != null){
						synchronized(mLock){
							mTabCount++;
							Message msg = H.getThreadHandler().obtainMessage(MESSAGE_RESET_DOUBLE_TAP);
							if(mTabCount == 2){
								if(H.getThreadHandler().hasMessages(MESSAGE_RESET_DOUBLE_TAP)){
									H.removeMessages(MESSAGE_RESET_DOUBLE_TAP);
									H.sendMessage(msg);
								}
								mListener.onDoubleTap();
								recoginzied();
							}

							if(!H.getThreadHandler().hasMessages(MESSAGE_RESET_DOUBLE_TAP)&& mTabCount != 2){
								H.sendMessage(msg, mDoubleTabTimeout);
							}
						}
					}
				}
				break;
				default:
					break;
			}
		
	}

	@Override
	protected void reset() {
		// TODO Auto-generated method stub

	}
	
	
	
	public void handleMessage(Message msg){
		super.handleMessage(msg);
		switch(msg.what){
			case MESSAGE_RESET_DOUBLE_TAP:
				synchronized(mLock){
					 mTabCount = 0;
				}
				break;
			default:
				break;
		}
	}
	
	
	 public void setListener(OnDoubleTapListener l){
		 mListener = l;
	 }


}
