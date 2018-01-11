package com.dadatoolkit.api;

import android.content.res.Resources;

import com.dadatoolkit.android.content.res.ResourcesLoader;
import com.dadatoolkit.android.content.res.ResourcesLoader.OnResourcesLoadListener;
/**
 * 
 * @author maoda.xu@samsung.com
 *
 */
public class ResourcesLoaderApi{
    private ResourcesLoader loader = null;
	public ResourcesLoaderApi(OnResourcesLoadListener l) {
		loader = new ResourcesLoader(l);
	}
	
	public void loadStringResouces(Resources r,String packageName){
		loader.loadStringResouces(r, packageName);
	}
	
	

}
