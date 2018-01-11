package com.dadatoolkit.android.view;

import android.os.Message;
import android.view.MotionEvent;

import com.dadatoolkit.android.os.HandlerThread;
import com.dadatoolkit.android.os.HandlerThread.OnHandleListener;

/**
 * 
 * @author maoda.xu@samsung.com
 *
 */
public abstract class GestureDetectorBase implements OnHandleListener {
	
	private GestureDetectorService mService;
	public static final int MESSAGE_RECOGONIZING=1;
	public static final int MESSAGE_RESET=2;

	protected HandlerThread H;
	private MotionEvent mEvent;
//	private OnGestureListener mListener;
	
	private float valid_move = 25.0f;
	

	public GestureDetectorBase(GestureDetectorService service) {
		// TODO Auto-generated constructor stub
		mService = service;
		H = new HandlerThread(getName());
		H.setOnHandleListener(this);
		H.start();
	}
	
	
	/**
	 * 
	 * @param event
	 */
	public void recogonizingLw(MotionEvent event){
		mEvent = event;
		//Bundle outData = new Bundle();
		//GestureDetectorNative.getResult(0, event, outData);
		H.sendMessage(H.getThreadHandler().obtainMessage(MESSAGE_RECOGONIZING));
	}
	
	
	public void resetLw(){
		H.sendMessage(H.getThreadHandler().obtainMessage(MESSAGE_RESET));
	}
	
	
	
	
	
	public void handleMessage(Message msg){
		switch(msg.what){
			case MESSAGE_RECOGONIZING:
				recogonizing();
				break;
			case MESSAGE_RESET:
				reset();
				break;
			default:
				break;
		}
	}
	
	protected MotionEvent getEvent(){
		return mEvent;
	}
	
	protected void recoginzied(){
		mService.onGestureRecogonized(this);
//		if(mListener != null){
//			mListener.onGesture(type, data);
//		}
	}
	
//	public void setOnGestureListener(OnGestureListener l){
//		mListener = l;
//	}
	
	
	public void destroy(){
		if(H != null){
		   H.quit();
		}
	}

	
	
	
	private String getName(){
		return this.getClass().getSimpleName();
	}
	
	
	
	
	protected void updateValidMove(float valid_move){
		this.valid_move = valid_move;
	}
	
	protected boolean validMove(float startX,float startY,float endX,float endY){
		return Math.sqrt((startX-endX)*(startX-endX)+(startY-endY)*(startY-endY)) > valid_move;
	}

	
	/**
	 * Start recogonizing
	 */

	protected abstract void recogonizing();
	
	/**
	 * Stop current recogonizing work
	 */
	protected abstract void reset();
	
	
//	public interface OnGestureListener{
//		public void onGesture(int type,Bundle data);
//		
//	}
	


	
}
