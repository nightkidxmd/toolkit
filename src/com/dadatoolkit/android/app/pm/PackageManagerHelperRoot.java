package com.dadatoolkit.android.app.pm;

import com.dadatoolkit.android.su.NotRootException;
import com.dadatoolkit.android.su.SuImpl;
import com.dadatoolkit.android.su.SuPolicy;

import android.content.Context;
import android.content.pm.PackageManager;

/** 
 *  @hide
 */
public class PackageManagerHelperRoot extends PackageManagerHelper {

	private SuPolicy su;
	private final String PMPREFIX="pm ";


	public PackageManagerHelperRoot(Context context) throws NotRootException{
		su = new SuImpl(context);
	}


	public PackageManagerHelperRoot(SuPolicy su,Context context){
		this.su = su;
	}


	@Override
	public void setComponentEnabledSetting(String packageName, String className,int status) {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		switch(status){
		case PackageManager.COMPONENT_ENABLED_STATE_DISABLED:
			sb.append("disable");
			break;
		case PackageManager.COMPONENT_ENABLED_STATE_ENABLED:
			sb.append("enable");
			break;
		default:
			break;
		}

		sb.append(" ").append(packageName).append("/").append(className);

		su.execute(sb.toString());
	}


	@Override
	public void setApplicationEnabledSetting(String packageName, int status) {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(PMPREFIX);
		switch(status){
		case PackageManager.COMPONENT_ENABLED_STATE_DISABLED:
			sb.append("disable");
			break;
		case PackageManager.COMPONENT_ENABLED_STATE_ENABLED:
			sb.append("enable");
			break;
		default:
			break;
		}

		sb.append(" ").append(packageName);

		su.execute((sb.toString()));
	}




	@Override
	public void grantPackagePermission(String packageName, String permission) {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(PMPREFIX);
		sb.append("grant")
		.append(" ")
		.append(packageName)
		.append(" ")
		.append(permission);
		su.execute(sb.toString());
	}


	@Override
	public void revokePackagePermission(String packageName, String permission) {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(PMPREFIX);
		sb.append("revoke")
		.append(" ")
		.append(packageName)
		.append(" ")
		.append(permission);
		su.execute(sb.toString());
	}


	@Override
	public void install(String apkPath) {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(PMPREFIX);
		sb.append("install")
		.append(" ")
		.append("\""+apkPath+"\"");
		su.execute(sb.toString());
	}



}
