package com.dadatoolkit.android.view;

import com.dadatoolkit.android.os.HandlerThread;
import com.dadatoolkit.android.os.HandlerThread.OnHandleListener;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;

/**
 * @deprecated
 * @author maoda.xu@samsung.com
 *
 */
public class GestureDetector implements OnHandleListener{
	
	public static final int  MESSAGE_DOUBLE_TAB = 1;
	public static final int  MESSAGE_LONG_PRESS = 2;
	
	private double mOldDistance = 0xffffffff;
	private OnZoomListener       mOnZoomListener;
	private OnLongPressListener  mOnLongPressListener;
	private OnDoubleTabListener  mOnDoulbeTabListener;
	private HandlerThread H;
	private final int mLongPressTimeout = 1000; //1s
	private final int mDoubleTabTimeout = 500;  //30ms;
	private int mTabCount = 0;
	private Object mLock = new Object();
	private float mStartX=0f;
	private float mStartY=0f;
	
	
	
	public interface OnZoomListener{
		public void onGestureZoom(double scale);
	}
	
	
	public interface OnLongPressListener{
		public void onLongPress();
	}
	
	
	public interface OnDoubleTabListener{
		public void onDoubleTab();
	}
	
	public void setOnZoomListener(OnZoomListener listener){
		mOnZoomListener = listener;
	}
	
	
	public void setOnLongPressListener(OnLongPressListener listener){
		mOnLongPressListener = listener;
		initHandlerThread();
	}
	
	public void setOnDoubleTabListener(OnDoubleTabListener listener){
		mOnDoulbeTabListener = listener;
		initHandlerThread();
	}
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public boolean onTouchEvent (MotionEvent event){
		if(H == null)
			return true;
		
		switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				mStartX = event.getX();
				mStartY = event.getY();
				if(mOnLongPressListener != null){
					if(!H.getThreadHandler().hasMessages(MESSAGE_LONG_PRESS)){
						Message msg = H.getThreadHandler().obtainMessage(MESSAGE_LONG_PRESS);
						H.sendMessage(msg, mLongPressTimeout);
					}
				}
				if(event.getPointerCount() < 2){
					if(mOnDoulbeTabListener != null){
						synchronized(mLock){
							mTabCount++;
							Message msg = H.getThreadHandler().obtainMessage(MESSAGE_DOUBLE_TAB);
							if(mTabCount == 2){
								if(H.getThreadHandler().hasMessages(MESSAGE_DOUBLE_TAB)){
									H.removeMessages(MESSAGE_DOUBLE_TAB);
									H.sendMessage(msg);
								}
								mOnDoulbeTabListener.onDoubleTab();
							}
							if(!H.getThreadHandler().hasMessages(MESSAGE_DOUBLE_TAB)&& mTabCount != 2){
								H.sendMessage(msg, mDoubleTabTimeout);
							}
						}
					}
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if(Math.abs(mStartX - event.getX()) < 20.0f && Math.abs(mStartY - event.getY()) < 20.0f){
					break;
				}
				if(event.getPointerCount() >= 3){
					MotionEvent.PointerCoords[] outPointerCoords = new MotionEvent.PointerCoords[event.getPointerCount()];
					for(int i=0;i<event.getPointerCount();i++){
						outPointerCoords[i] = new MotionEvent.PointerCoords();
						event.getPointerCoords(i, outPointerCoords[i]);
					}
					Log.e("DADA","outPointerCoords[0]:"+outPointerCoords[0].x+","+outPointerCoords[0].y);
				
				
				}else if(event.getPointerCount() == 2 && mOnZoomListener != null){
					float x0 = event.getX(0);
					float y0 = event.getY(0);
					float x1 = event.getX(1);
					float y1 = event.getY(1);
					double new_distance  = Math.sqrt((x0-x1)*(x0-x1)+(y0-y1)*(y0-y1));
					if(mOldDistance != 0xffffffff){
						mOnZoomListener.onGestureZoom(new_distance - mOldDistance);
					}
					mOldDistance = new_distance;
				}
			    if(mOnLongPressListener != null){
			    	if(H.getThreadHandler().hasMessages(MESSAGE_LONG_PRESS)){
			    		H.removeMessages(MESSAGE_LONG_PRESS);
			    	}
			    }
				break;
			case MotionEvent.ACTION_UP:
				if(mOnZoomListener != null)
					mOldDistance = 0xffffffff;
			    if(mOnLongPressListener != null){
			    	if(H.getThreadHandler().hasMessages(MESSAGE_LONG_PRESS)){
			    		H.removeMessages(MESSAGE_LONG_PRESS);
			    	}
			    }
				break;
			default:
				break;
		}
		return true;
	}
	
	public void destroy(){
		if(H != null){
			H.quit();
			H = null;
		}
	}
	
	
	private void initHandlerThread(){
		if(H == null){
			H = new HandlerThread("GestureDetector");
			H.setOnHandleListener(this);
			H.start();
		}
	}

	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		switch(msg.what){
			case MESSAGE_DOUBLE_TAB:
				synchronized(mLock){
					 mTabCount = 0;
				}
				break;
			case MESSAGE_LONG_PRESS:
				 mOnLongPressListener.onLongPress();
				break;
			default:
				break;
		}
	}
	
}
