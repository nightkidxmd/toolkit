package com.dadatoolkit.android.app.am;

import com.dadatoolkit.android.su.NotRootException;
import com.dadatoolkit.android.su.SuImpl;

import android.content.Context;

public class ActivityManagerHelperRoot extends ActivityManagerHelper {
	
	private SuImpl su;
	private final String AMPREFIX="am ";

	public ActivityManagerHelperRoot(Context context) throws NotRootException {
		 su = new SuImpl(context);
	}
	
	
	private void execute(String CMD){
		CMD=AMPREFIX+CMD;
		su.execute(CMD);
		
	}



	@Override
	public void killProcess(String packageName) {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append("kill")
		.append(" ")
		.append(packageName);
		execute(sb.toString());
	}



	@Override
	public void killAll() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append("kill-all");
		execute(sb.toString());
	}



	@Override
	public void forceStopPackage(String packageName) {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append("force-stop")
		.append(" ")
		.append(packageName);
		execute(sb.toString());
	}

}
