package com.dadatoolkit.android.graphic;

import com.dadatoolkit.java.LruCache;

import android.graphics.Bitmap;


public class BitmapCache {
	private static final int DEFAULT_MAX_SIZE = 12;
	private LruCache<Integer,Bitmap> mBitmaps;
	
	
	public BitmapCache() {
		mBitmaps = new LruCache<Integer,Bitmap>(DEFAULT_MAX_SIZE);
	}
		
	public BitmapCache(int maxSize) {
		mBitmaps = new LruCache<Integer,Bitmap>(maxSize);
	}
	
	public Bitmap get(int id){	
		return mBitmaps.get(id);
	}
	
	public void put(int id,Bitmap bitmap){	
		mBitmaps.put(id,bitmap);
	}
	
	
	public int getBitmapId(Bitmap bitmap){

		return 0;
	}
	

}
