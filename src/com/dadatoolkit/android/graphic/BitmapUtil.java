package com.dadatoolkit.android.graphic;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class BitmapUtil {
	
	public static final int ROTATION_90 = 0;
	public static final int ROTATION_180 = 1;
	public static final int ROTATION_270 = 2;
	

	
	public static  Bitmap effect(Bitmap bitmap,boolean recycle){
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int[] buffer = new int[width * height];
		bitmap.getPixels(buffer, 0, width , 0, 0,width ,height);
		Bitmap.Config config = bitmap.getConfig();
		if(recycle){
			recycle(bitmap);
		}
		BitmapEngine.nativeEffect(buffer,width ,height );
		bitmap = Bitmap.createBitmap(buffer, width, height, config);
		return bitmap;
		
	}
	
	
	public static byte[] effect(byte[] data){
		Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int[] buffer = new int[width * height];
		bitmap.getPixels(buffer, 0, width , 0, 0,width ,height);
		recycle(bitmap);
		BitmapEngine.nativeEffect(buffer,width,width);
		Bitmap.Config config = bitmap.getConfig();
		bitmap = Bitmap.createBitmap(buffer, width, height, config);
		return Bitmap2Bytes(bitmap,true);
	}
	
	
	/**
	 * 
	 * @param data              byte array of compressed image data
	 * @param filePath          file save as
	 * @param format            {@link Bitmap.CompressFormat}
	 * @param quality           Hint to the compressor, 0-100. 0 meaning compress for small size, 
	 *                          100 meaning compress for max quality. Some formats, 
	 *                          like PNG which is lossless, will ignore the quality setting
	 * @param opts              {@link BitmapFactory.Options}
	 * @return ret              true if success
	 */
	
	public static boolean saveBitmapToFile(byte[] data,String filePath,Bitmap.CompressFormat format,
			int quality,BitmapFactory.Options opts){
		FileOutputStream stream = null;
		boolean ret =false;
		Bitmap bitmap  = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
		bitmap = effect(bitmap,true);
		try {
			stream = new FileOutputStream(filePath);
			if(bitmap != null){
				ret = bitmap.compress(format, quality, stream);
				recycle(bitmap);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(stream != null){
				try {
					stream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				stream = null;
			}
		}
		
		return ret;
		
	}
	
	/**
	 * 
	 * @param bitamp              orignal bitmap
	 * @param resizeWidth         new width
	 * @param resizeHeight        new height
	 * @param recycle             If true the orignal bitmap will be recycled and do not do further operation
	 *                            with orignal bitmap in this case.If you need to reuse orignal bitmap plz set
	 *                            false here and <b>do not forget recycle by yourself.</b>
	 * @return newBitmap          resized bitmap
	 */
	
	public static Bitmap resizeBitmap(Bitmap bitamp,int resizeWidth,int resizeHeight,boolean recycle){
		Bitmap newBitmap = Bitmap.createScaledBitmap(bitamp, resizeWidth, resizeHeight, true);
		if(recycle){
			recycle(bitamp);
		}
		return newBitmap;
	}
	
	
	/**
	 * 
	 * @param bitmap            orignal bitmap
	 * @param filePath          file path to save as
	 * @param format            {@link Bitmap.CompressFormat}
	 * @param quality           Hint to the compressor, 0-100. 0 meaning compress for small size, 
	 *                          100 meaning compress for max quality. Some formats, 
	 *                          like PNG which is lossless, will ignore the quality setting
	 * @param recycle           If true the orignal bitmap will be recycled and do not do further operation
	 *                          with orignal bitmap in this case.If you need to reuse orignal bitmap plz set
	 *                          false here and <b>do not forget recycle by yourself.</b>
	 * @return ret              true if success
	 */
	
	public static boolean saveBitmapToFile(Bitmap bitmap,String filePath,Bitmap.CompressFormat format,
			int quality,boolean recycle){
		FileOutputStream stream = null;
		boolean ret =false;
		Log.d("DADA","start effect");
		Bitmap bitmap_new = effect(bitmap,recycle);
		Log.d("DADA","stop effect");
		try {
			Log.e("DADA","start save indeed");
			long starttime = System.currentTimeMillis();
			stream = new FileOutputStream(filePath);
			if(bitmap_new != null){
				ret = bitmap_new.compress(format, quality, stream);
				recycle(bitmap_new);
			}
			Log.e("DADA","stop save indeed"+" time="+(System.currentTimeMillis()-starttime)+"ms");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(stream != null){
				try {
					stream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				stream = null;
			}
		}
		
		return ret;
		
	}
	
	
	public static void recycle(Bitmap bitmap){
		if(bitmap != null && !bitmap.isRecycled()){
			bitmap.recycle();
			bitmap = null;
		}
	}
	
	public static byte[] Bitmap2Bytes(Bitmap bitmap,boolean recycle){
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 100, stream);
		if(recycle)
			recycle(bitmap);
		return stream.toByteArray();
		
	}
	
	
	public static Bitmap Rotate(byte[] data, int rotation){
		Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int[] orientation = {90,180,270};
		Matrix m = new Matrix();
		m.setRotate(orientation[rotation], width/2, height/2);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, m, true);
        recycle(bitmap);
		           
		return newBitmap;
	
	}
	
	
	public static Bitmap Rotate(Bitmap bitmap, int rotation){
		return Rotate(bitmap,rotation,true);
	}
	
	public static Bitmap Rotate(Bitmap bitmap, int rotation,boolean recycle){
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int[] orientation = {90,180,270};
		Matrix m = new Matrix();
		m.setRotate(orientation[rotation], width/2, height/2);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, m, true);
        if(recycle)
        	recycle(bitmap);
		           
		return newBitmap;
	
	}
	
	public static Bitmap drawableToBitmap(Drawable drawable){
		return ((BitmapDrawable)drawable).getBitmap();
	}
	
	@SuppressWarnings("deprecation")
	public static Drawable bitmapToDrawable(Bitmap bitmap){
		return new BitmapDrawable(bitmap);
	}
}
