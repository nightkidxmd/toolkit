package com.dadatoolkit.java.io;

import com.dadatoolkit.java.io.CMDUtil.OnResultListener;

public class AAPTHelper {
	private String AAPT;

	public AAPTHelper(String path) {
		// TODO Auto-generated constructor stub
		AAPT="\""+path+"aapt.exe\" ";
	}
	
	public void dumpResourcesTable(String filepath,OnResultListener l){
		CMDUtil.executeAndGetResult(AAPT+"dump resources "+filepath, l,false,true);
	}

}
