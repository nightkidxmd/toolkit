package com.dadatoolkit.android.view;

import android.view.MotionEvent;
/**
 * Better not use together with {@link DoubleTapGestureDetector},
 * Or it'll trigger ShortPress first then DoubleTap
 * 
 * @author maoda.xu@samsung.com
 *
 */
public class ShortPressGestureDetector extends GestureDetectorBase{
	private boolean mIsSkip = false;
	private OnShortPressListener mListener;
	private int mCount = 0;
	private float mStartX = 0.0f;
	private float mStartY = 0.0f;
	
	public interface OnShortPressListener{
		public void onShortPress();
	}

	public ShortPressGestureDetector(GestureDetectorService service) {
		super(service);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void recogonizing() {
		// TODO Auto-generated method stub
		MotionEvent event =  getEvent();
		switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				mStartX = event.getX();
				mStartY = event.getY();
				mCount = event.getPointerCount();
				break;
			case MotionEvent.ACTION_MOVE:
				if(!validMove(mStartX, mStartY, event.getX(), event.getY())){
					break;
				}
				reset();
				break;
			case MotionEvent.ACTION_UP:
				if(mCount < 2 && !mIsSkip && mListener != null){
					mListener.onShortPress();
					recoginzied();
				}else{
					mIsSkip = false;
				}
				mCount = mCount -1 ;
				break;
			default:
				break;
		}
	}

	@Override
	protected void reset() {
		// TODO Auto-generated method stub
		mIsSkip = true;
	}

	public void setListener(OnShortPressListener l){
		mListener = l;
	}

}
