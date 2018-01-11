package com.dadatoolkit.android.view;

import java.util.ArrayList;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
/**
 * 
 * @author maoda.xu@samsung.com
 *
 */
public class GestureDetectorService implements OnTouchListener{
	
	private ArrayList<GestureDetectorBase> mDetectors;
	
	public GestureDetectorService(){
		initial();
	}
	
	
	private void initial(){
		mDetectors = new ArrayList<GestureDetectorBase>();
		loadDetectors();
	}
	
	private void loadDetectors(){

	}
	
	
	/**
	 * Deliver event to all detectors
	 * @param event
	 */
	
	public void recogonizing(MotionEvent event){
		for(GestureDetectorBase d:mDetectors){
			d.recogonizingLw(event);
		}
	}
	
	
	/**
	 * One detector recogonized the gesture,
	 * stop other detector's recogonizing work.
	 * @param detector
	 */
	
	public void onGestureRecogonized(GestureDetectorBase detector){
		for(GestureDetectorBase d:mDetectors){
			if(d.equals(detector)){
				continue;
			}
			
			d.resetLw();
		}
	}
	
	/**
	 * 
	 * @param detector
	 */
	
	public void registerDetector(GestureDetectorBase detector){
		if(!mDetectors.contains(detector))
			mDetectors.add(detector);
	}
	
	/**
	 * 
	 * @param detector
	 */
	
	public void unregisterDetector(GestureDetectorBase detector){
		if(mDetectors.contains(detector)){
			detector.destroy();
			mDetectors.remove(detector);
		}
	}
	
	public void destroy(){
		for(GestureDetectorBase d:mDetectors){
			d.destroy();
		}
	}


	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		recogonizing(event);
		return true;
	}

}
