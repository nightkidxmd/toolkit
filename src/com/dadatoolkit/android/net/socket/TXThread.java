package com.dadatoolkit.android.net.socket;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.dadatoolkit.android.net.socket.protocol.Protocol;

public class TXThread extends Thread {
	private OutputStream mOutput;
	private Object mLock = new Object();
	private byte[] mBuffer;
	private boolean mIsStop = false;
	
	
	public TXThread(OutputStream os){
		mOutput = os;
	}
	
	
	
	public void run(){
		DataInputStream in = null;
	     try {
			 while(true){
				 synchronized(mLock){
					 try {
						mLock.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 }
				 

				 
				 mOutput.write(mBuffer);
				 mOutput.flush();
				 
				 
				 if(mIsStop){
					 break;
				 }
				 
			  }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
			     try {
					if(in != null)
						in.close();
					if(mOutput != null){
						mOutput.close();
						mOutput = null;
					}
			     } catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
				}
			}
	}
	
	
	
	protected void finalize () {
		try {
			super.finalize();
			if(mOutput != null){
				mOutput.close();
			}
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
		
	public void sendMessage(String string){
		string = Protocol.encodec(string);
		synchronized(mLock){
			mBuffer = string.getBytes();
			mLock.notify();
		}
	}
	
	public void end(){
		synchronized(mLock){
			mBuffer = Protocol.SOCKET_STOP_CMD.getBytes();
			mIsStop = true;
			mLock.notify();
		}
	}
	
	

}
