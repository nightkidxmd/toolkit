package com.dadatoolkit.api;

import com.dadatoolkit.android.view.GestureDetectorBase;
import com.dadatoolkit.android.view.GestureDetectorService;
/**
 * 
 * @author maoda.xu@samsung.com
 *
 */
public class GestureDetectorServiceApi {
	private GestureDetectorService mService;
	public GestureDetectorServiceApi(){
		mService = new GestureDetectorService();
	}
	
	/**
	 * 
	 * @return {@link GestureDetectorService}
	 */
	
	public GestureDetectorService getService(){
		return mService;
	}
	
	/**
	 * Register detector
	 * @param d   {@link GestureDetectorBase}
	 */
	
	public void registerDetector(GestureDetectorBase d){
		mService.registerDetector(d);
	}
	
	/**
	 * 
	 */
	
	public void destroy(){
		mService.destroy();
	}
}
