package com.dadatoolkit.android.app.pm;
/**
 * 2014-03-12 by maoda.xu@samsung.com
 */
import com.dadatoolkit.android.su.NotRootException;
import com.dadatoolkit.android.su.SuPolicy;

import android.content.Context;
import android.content.pm.PackageManager;

public abstract class PackageManagerHelper {
	
	private static Object mInstance;
	
	/**
	 * 
	 * @param context
	 * @param isRoot
	 * @return Object   if isRoot==true,cast to {@link PackageManagerHelper} else
	 *                  cast to {@link PackageManager}
	 * @throws NotRootException 
	 */
	
	public static Object getInstance(Context context,boolean isRoot,SuPolicy SU) throws NotRootException{
		if(mInstance == null){
			if(isRoot){
				if(SU != null){
					mInstance = new PackageManagerHelperRoot(SU,context);
				}else{
					mInstance = new PackageManagerHelperRoot(context);
				}
				
			}else{
				mInstance = context.getPackageManager();
				
			}
		}
		return mInstance;
	}
	
	
	/**
	 * 
	 * @param packageName
	 * @param className
	 * @param status    PackageManager.COMPONENT_ENABLED_STATE_DISABLED / PackageManager.COMPONENT_ENABLED_STATE_ENABLED
	 */
	
	public abstract void setComponentEnabledSetting(String packageName,String className,int status);
	
	/**
	 * 
	 * @param packageName
	 * @param status  PackageManager.COMPONENT_ENABLED_STATE_DISABLED / PackageManager.COMPONENT_ENABLED_STATE_ENABLED
	 */
	public abstract void setApplicationEnabledSetting(String packageName,int status);
	
	/**
	 *  Only optional permissions the application has
	 *  declared can be granted or revoked.
	 * @param packageName
	 * @param permission
	 */
	public abstract void grantPackagePermission(String packageName,String permission);
	
	/**
	 *  Only optional permissions the application has
	 *  declared can be granted or revoked.
	 * @param packageName
	 * @param permission
	 */
	public abstract void revokePackagePermission(String packageName,String permission);
	
	
	public abstract void install(String apkPath);
	
}