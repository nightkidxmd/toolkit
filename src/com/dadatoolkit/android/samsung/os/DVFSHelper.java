package com.dadatoolkit.android.samsung.os;

import java.lang.reflect.Method;

import android.content.Context;
import com.dadatoolkit.java.ReflectUtil;
/**
 * 
 * @author maoda.xu@samsung.com
 *
 */

public class DVFSHelper {
	private final String SAMSUNG_DVFS_PACKAGENAME="android.os.DVFSHelper";
	public static final int TYPE_NONE = 11;
	public static final int TYPE_CPU_MIN = 12;
	public static final int TYPE_CPU_MAX = 13;
	public static final int TYPE_CPU_CORE_NUM_MIN = 14;
	public static final int TYPE_CPU_CORE_NUM_MAX = 15;
	public static final int TYPE_GPU_MIN = 16;
	public static final int TYPE_GPU_MAX = 17;
	public static final int TYPE_EMMC_BURST_MODE = 18;
	public static final int TYPE_BUS_MIN = 19;
	public static final int TYPE_BUS_MAX = 20;
	private Object mDVFSHelper;
	private int mType = TYPE_NONE;
	/**
	 * This is for samsung DVFS, it dosen't need any permission.<br>
	 * Supported type:<br>
	 * TYPE_CPU_MIN<br>
	 * TYPE_CPU_MAX<br>
	 * TYPE_CPU_CORE_NUM_MIN<br>
	 * TYPE_CPU_CORE_NUM_MAX<br>
	 * TYPE_GPU_MIN<br>
	 * TYPE_GPU_MAX<br>
	 * TYPE_EMMC_BURST_MODE<br>
	 * TYPE_BUS_MIN<br>
	 * TYPE_BUS_MAX
	 * @param context
	 * @param type
	 */
	public DVFSHelper(Context context,int type) throws NotSupportedSamsungDVFSException{
		mDVFSHelper = ReflectUtil.getInstance(ReflectUtil.getConstructor(SAMSUNG_DVFS_PACKAGENAME, Context.class,int.class), context,type);
		if(mDVFSHelper == null){
			throw new NotSupportedSamsungDVFSException();
		}
		mType = type;	
	}



	public void aquire(){
		ReflectUtil.inVoke(mDVFSHelper, getMethod("acquire"));
	}

	public void release(){
		ReflectUtil.inVoke(mDVFSHelper, getMethod("release"));
	}
	/**
	 * 
	 * @param value
	 */
	public void addExtraOption(String value){
		ReflectUtil.inVoke(mDVFSHelper, getMethod(
				"addExtraOption",String.class,String.class), getNameByType(mType),value);
	}
	/**
	 * 
	 * @return int[]
	 */

	public int[] getSupportedCPUFrequency(){
		return (int [])ReflectUtil.inVoke(mDVFSHelper, getMethod("getSupportedCPUFrequency"));
	}

	/**
	 * 
	 * @return int[]
	 */

	public int[] getSupportedCPUCoreNum(){
		return (int [])ReflectUtil.inVoke(mDVFSHelper, getMethod("getSupportedCPUCoreNum"));
	}

	/**
	 * 
	 * @return int[]
	 */

	public int[] getSupportedGPUFrequency(){
		return (int [])ReflectUtil.inVoke(mDVFSHelper, getMethod("getSupportedGPUFrequency"));
	}

	/**
	 * 
	 * @return int[]
	 */

	public int[] getSupportedBUSFrequency(){
		return (int [])ReflectUtil.inVoke(mDVFSHelper, getMethod("getSupportedBUSFrequency"));
	}
	/**
	 * 
	 * @return boolean true aquired.
	 */

	public boolean isAquired(){
		return (Boolean)ReflectUtil.inVoke(mDVFSHelper, getMethod("isAquired"));
	}

	private String getNameByType(int type){
		String ret = null;
		switch(type){
		case TYPE_CPU_MIN:
		case TYPE_CPU_MAX:
			ret = "CPU";
			break;
		case TYPE_CPU_CORE_NUM_MIN:
		case TYPE_CPU_CORE_NUM_MAX:
			ret = "CORE_NUM";
			break;
		case TYPE_GPU_MIN:
		case TYPE_GPU_MAX:
			ret = "GPU";
		case TYPE_BUS_MIN:
		case TYPE_BUS_MAX:
			ret = "BUS";
		default:
			break;
		}

		return ret;
	}

	private Method getMethod(String methodName,Class<?>... parameterTypes){
		return ReflectUtil.getMethod(SAMSUNG_DVFS_PACKAGENAME,methodName,parameterTypes);
	}

}
