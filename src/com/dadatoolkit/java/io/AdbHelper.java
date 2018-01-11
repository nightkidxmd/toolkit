package com.dadatoolkit.java.io;

import java.io.File;

import com.dadatoolkit.java.io.CMDUtil.OnResultListener;
/**
 * Helper for PC,need adb tool kit.
 * @author maoda.xu@samsung.com
 *
 */
public class AdbHelper {
	private String ADB;
	
	/**
	 * 
	 * @param path path
	 */
	public AdbHelper(String path){
		ADB = "\""+path+"adb.exe\" ";
	}
	
	public void killServer(){
		CMDUtil.execute(ADB+"kill-server",true);
	}
	
	
	
	public void startServer(){
		CMDUtil.execute(ADB+"start-server",true);
	}
	
	
	
	public void waitForDevice(){
		CMDUtil.execute(ADB+"wait-for-device",true);
	}
	
	public void getPackagesList(OnResultListener l){
		CMDUtil.executeAndGetResult("cmd /c "+ADB+"shell pm list packages", l,false,false);
	}
	
	
	
	public void getPackagesListPath(OnResultListener l,String pkg){
		CMDUtil.executeAndGetResult(ADB+"shell pm path "+pkg,l);
	}
	
	public void pull(String from, String to){
		File temp = new File(to);
		CMDUtil.execute(ADB+"pull "+from+" "+"\""+to+"\"",!temp.isDirectory());

	}
	
	public void push(String from, String to){
		File temp = new File(from);
		CMDUtil.execute(ADB+"push "+"\""+from+"\""+" "+to,!temp.isDirectory());

	}
	
	
	public void install(String path){
		CMDUtil.execute(ADB+"install -r "+path,true);
	}
	
	
	public void stopActivity(String pkg){
		CMDUtil.execute(ADB+"shell am force-stop "+pkg,false);
	}
	
	
	public void startActivity(String component,String... args){
		if(args != null){
			StringBuilder arg = new StringBuilder();
			for(String a:args){
				arg.append(a).append(" ");
			}
			CMDUtil.execute(ADB+"shell am start "+arg.toString()+component,true);
		}else{
			CMDUtil.execute(ADB+"shell am start "+component,true);
		}
		
		
		
	}
	
	public void forwardTCP(int localPort,int remotePort){
		CMDUtil.execute(ADB+"forward tcp:"+localPort+" tcp:"+remotePort, true);
	}
	
	
	
	public void getSerialNo(OnResultListener l){
		CMDUtil.executeAndGetResult(ADB+"get-serialno",l);
	}
	
	
	public void getType(OnResultListener l){
		CMDUtil.executeAndGetResult(ADB+"shell getprop ro.build.type",l);
	}

	
	
	
	public void getState(OnResultListener l){
		CMDUtil.executeAndGetResult(ADB+"get-state",l);
	}
	
	
	public void executeShell(String cmd,OnResultListener l){
		CMDUtil.executeAndGetResult(ADB+"shell "+cmd ,l,false,true);
	}
}
