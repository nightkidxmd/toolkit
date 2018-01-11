package com.dadatoolkit.android.content.res;

import java.lang.reflect.Field;

import android.content.res.Resources;
import android.util.Log;

import com.dadatoolkit.api.ReflectUtilApi;

public class ResourcesLoader {
	private OnResourcesLoadListener mListener;


	public ResourcesLoader(OnResourcesLoadListener l){
		mListener = l;
	}

	public interface OnResourcesLoadListener{
		public void onResourcesLoad(Class<?> clazz,Object[] data);
		public void onResourceLoadDone(String pkg);

	}


	public void loadStringResouces(Resources r,String packageName){
		Field[] fields = ReflectUtilApi.getInnerDeclaredFields(packageName+".R", "string");
		if(fields != null){
			for (Field f : fields) {
				String name = f.getName();
				try {
					String content = r.getString(Integer
							.valueOf(String.valueOf(ReflectUtilApi
									.getInnerStaticClassField(packageName+".R", "string", name))));
					if(mListener != null)
						mListener.onResourcesLoad(String.class,new String[]{packageName,name,content});
				} catch (Exception e) {
					Log.w("DADA", "Res Not Found:" + name);
				}
			}
		}
		if(mListener != null)
			mListener.onResourceLoadDone(packageName);

	}
	
	public static int loadResourceIdByName(Resources r,String name, String defType,String defPackage){
		return r.getIdentifier(name, defType, defPackage);
	}
	
	public static String loadResourceNameById(Resources r,int resid){
		return r.getResourceName(resid);
	}
	
	public static String loadResourceEntryNameById(Resources r,int resid){	
		return r.getResourceEntryName(resid);
	}
	
	public static String loadResourcePackageNameById(Resources r,int resid){	
		return r.getResourcePackageName(resid);
	}
	
	public static String loadResourceTypeNameById(Resources r,int resid){	
		return r.getResourceTypeName(resid);
	}

}
