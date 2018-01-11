package com.dadatoolkit.java.io;

import com.dadatoolkit.java.io.CMDUtil.OnResultListener;


public class ConnectionMonitor extends Thread implements  OnResultListener{
	
	private String lastState;
	
	private  OnConnectionChanged l;
	private AdbHelper adbHelper;
	private String mDeviceSearial;
	private OnResultListener mDeviceChangedMonitor = new  OnResultListener(){
		@Override
		public void onDone(String result) {
			// TODO Auto-generated method stub
			System.err.println("mDeviceSearial="+mDeviceSearial+" result="+result);
			if(mDeviceSearial != null && result != null && !mDeviceSearial.equals(result)){
				l.onDeviceChanged();
			}
			//System.err.println(result);
			mDeviceSearial = result;
		}
		
	};
	
	public ConnectionMonitor(AdbHelper adbHelper,OnConnectionChanged l){
		this.l = l;
		this.adbHelper = adbHelper;
	}
	
	public void run(){
		while(true){
			adbHelper.getState(this);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	@Override
	public void onDone(String newState) {
		// TODO Auto-generated method stub
		if(mDeviceSearial == null){
			adbHelper.getSerialNo(mDeviceChangedMonitor);
		}
		
		if(lastState == null){
			lastState = newState;
		}else if(!lastState.equals(newState) ){
			if(newState.equals("device")){
				l.onConnected();
				adbHelper.getSerialNo(mDeviceChangedMonitor);
			}else{
				l.onDisconnected();
			}
			lastState = newState;
			
		}
		
		
	}
	
	
	public interface OnConnectionChanged{
		public void onDisconnected();
		public void onConnected();
		public void onDeviceChanged();
	}


}
