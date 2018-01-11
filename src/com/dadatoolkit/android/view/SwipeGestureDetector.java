package com.dadatoolkit.android.view;

import android.view.MotionEvent;

public class SwipeGestureDetector extends GestureDetectorBase {
	
	
	public static final int DIRECTION_LEFT   = 1;
	public static final int DIRECTION_UP     = 2;
	public static final int DIRECTION_RIGHT  = 3;
	public static final int DIRECTION_DOWN   = 4;
	private float mStartX;
	private float mStartY;
	private OnSwipeListener mListener;
	private int mCount = 0;


	public SwipeGestureDetector(GestureDetectorService service) {
		super(service);
		// TODO Auto-generated constructor stub
		updateValidMove(225.0f);
	}

	@Override
	protected void recogonizing() {
		// TODO Auto-generated method stub
		MotionEvent event = getEvent();
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			mStartX = event.getX();
			mStartY = event.getY();
			mCount++;
			break;
		case MotionEvent.ACTION_MOVE:
			 
			break;
		case MotionEvent.ACTION_UP:
			mCount--;
			if(mCount != 0){
				break;
			}
			float x = event.getX();
			float y =  event.getY();
			
			if(!validMove(mStartX,mStartY,x,y)){
				break;
			}
			recoginzied();
			
			if(mListener != null){
				mListener.onSwipe(getDirection(mStartX,mStartY,x,y));
			}
			
			break;
		
		default:
			break;
		}
		
	}

	@Override
	protected void reset() {
		// TODO Auto-generated method stub
		mCount = 0;
	}
	
	public void setOnSwipeListener(OnSwipeListener l){
		mListener = l;
	}
	
	
	
	
	private int getDirection(float startX,float startY,float endX,float endY){
		float deltaX = endX - startX;
		float deltaY = endY - startY;
		int direction = 0;
		if(Math.abs(deltaX) > Math.abs(deltaY)){
			if(deltaX > 0){
				direction = DIRECTION_RIGHT;
			}else{
				direction = DIRECTION_LEFT;
			}
		}else{
			if(deltaY > 0){
				direction = DIRECTION_DOWN;
			}else{
				direction = DIRECTION_UP;
			}
			
		}
		
		return direction;
		
	}
	
	
	
	public interface OnSwipeListener{
		public void onSwipe(int direction);
	}
	
	

}
