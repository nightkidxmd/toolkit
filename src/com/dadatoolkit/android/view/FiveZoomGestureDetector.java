package com.dadatoolkit.android.view;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;


public class FiveZoomGestureDetector extends GestureDetectorBase {
	private float mStartX = 0.0f;
	private float mStartY = 0.0f;

	public FiveZoomGestureDetector(GestureDetectorService service) {
		super(service);
		// TODO Auto-generated constructor stub
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	protected void recogonizing() {
		// TODO Auto-generated method stub
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
				
				if(event.getPointerCount()>2){
					PointerCoords[] pointerCoords = new PointerCoords[event.getPointerCount()];
					for(int i=0;i<event.getPointerCount();i++){
						PointerCoords outPointerCoords = new PointerCoords();
						event.getPointerCoords(i, outPointerCoords);
						pointerCoords[i]=outPointerCoords;
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				break;
			default:
				break;
		}
	}

	@Override
	protected void reset() {
		// TODO Auto-generated method stub
		
	}

}
