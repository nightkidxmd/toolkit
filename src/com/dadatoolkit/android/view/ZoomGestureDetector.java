package com.dadatoolkit.android.view;

import android.view.MotionEvent;

/**
 * 
 * @author maoda.xu@samsung.com
 *
 */

public class ZoomGestureDetector extends GestureDetectorBase {

	private double mOldDistance = 0xffffffff;
	private OnZoomListener mListener;
	private boolean mSkip = false;
	private float mStartX = 0.0f;
	private float mStartY = 0.0f;
	
	public interface OnZoomListener{
		public void onZoom(double scale);
	}

	public ZoomGestureDetector(GestureDetectorService service) {
		super(service);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void recogonizing() {
		MotionEvent event = getEvent();
		switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				mStartX = event.getX();
				mStartY = event.getY();
				break;
			case MotionEvent.ACTION_MOVE:
				if(!validMove(mStartX, mStartY, event.getX(), event.getY())){
					break;
				}
				if(event.getPointerCount() == 2 && mListener != null){
					if(!mSkip){
						float x0 = event.getX(0);
						float y0 = event.getY(0);
						float x1 = event.getX(1);
						float y1 = event.getY(1);
						double new_distance  = Math.sqrt((x0-x1)*(x0-x1)+(y0-y1)*(y0-y1));
						if(mOldDistance != 0xffffffff){
							mListener.onZoom(new_distance - mOldDistance);
							recoginzied();
							mOldDistance = 0xffffffff;
						}else
							mOldDistance = new_distance;
					}else{
						mSkip = false;
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				mOldDistance = 0xffffffff;
				break;
			default:
				break;
		}
	}

	@Override
	protected void reset() {
		// TODO Auto-generated method stub
		mSkip = true;
		mOldDistance = 0xffffffff;
	}
	
	
	public void setListener(OnZoomListener l){
		mListener = l;
	}

}
