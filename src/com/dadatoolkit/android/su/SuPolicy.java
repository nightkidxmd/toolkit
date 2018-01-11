package com.dadatoolkit.android.su;

import android.content.Context;

public abstract class SuPolicy {


	Context mContext;

	public SuPolicy(Context context){
		mContext = context;
	}
	


	/**
	 * su -V
	 * @return String
	 */

	public abstract String getVersionName();

	/**
	 * su -v
	 * @return int 
	 */

	public abstract int getVersionCode();



	/**
	 * su -c cmd
	 * @param cmd   command
	 * @return String  Content of InputStream  Only One line
	 */

	public abstract  String execute(String cmd);



	/**
	 * su -c cmd
	 * @param cmd
	 * @param isGetResult
	 * @return String  Content of InputStream  Only One line
	 */

	public abstract  String execute(String cmd,boolean isGetResult);
	
	
	
	public abstract void aquireRoot();
	
	public abstract void startDaemon();
	
	public abstract void stopDaemon();
	
	public abstract void setOnRootStateListener(OnRootStateListener l);
	
	
	public abstract boolean isRooted();

	public interface OnRootStateListener{
		void onRoot(boolean success);
	}


}
