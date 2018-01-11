package com.dadatoolkit.android.su;

import android.content.Context;
/**
 * 
 * @author maoda.xu@samsung.com
 *
 */
public  class SuHelper extends SuPolicy{
	protected SuImpl su;
	
	/**
	 * 
	 * @param context
	 * @throws NotRootException
	 */
	public SuHelper(Context context) throws NotRootException  {	
		super(context);
		su = new SuImpl(context);

		
	}



	@Override
	public String getVersionName() {
		// TODO Auto-generated method stub
		return su.getVersionName();
	}





	@Override
	public int getVersionCode() {
		// TODO Auto-generated method stub
		return su.getVersionCode();
	}





	@Override
	public String execute(String cmd) {
		// TODO Auto-generated method stub
		return su.execute(cmd);
	}





	@Override
	public String execute(String cmd, boolean isGetResult) {
		// TODO Auto-generated method stub
		return su.execute(cmd, isGetResult);
	}





	@Override
	public void aquireRoot() {
		// TODO Auto-generated method stub
		su.aquireRoot();
	}





	@Override
	public void startDaemon() {
		// TODO Auto-generated method stub
		su.startDaemon();
	}




	@Override
	public void stopDaemon() {
		// TODO Auto-generated method stub
		su.stopDaemon();
	}




	@Override
	public void setOnRootStateListener(OnRootStateListener l) {
		// TODO Auto-generated method stub
		su.setOnRootStateListener(l);
	}




	@Override
	public boolean isRooted() {
		// TODO Auto-generated method stub
		return su.isRooted();
	}
	


}
