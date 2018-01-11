package com.dadatoolkit.linux.firewall;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;

/**
 * FW solution used with firewalld
 * @author maoda.xu@samsung.com
 *
 */
public class FirewallConnection {
	private InputStream mIn;
	private OutputStream mOut;
	private LocalSocket mSocket;
	private String mCMD;
	private Object mSendLock = new Object();
	private Object mReceiveLock = new Object();
	private SendThread mSendThread = new SendThread();
	private ReceiveThread mReceiveThread = new ReceiveThread();
	private boolean isConnected = false;
	
	private OnDataReadListener L;

	public FirewallConnection() {

	}

	/**
	 * send cmd
	 * @param cmd
	 * @param l        get result from this callback, could be null.
	 */

	void send(String cmd,OnDataReadListener l){
		
		if(cmd.contains("&")||cmd.contains("|")){//Security
			return;
		}
		
		synchronized(mSendLock){
			mCMD = cmd;
			L = l;
			mSendLock.notify();
		}
	}
	
	/**
	 * connect server
	 * @return
	 */

	boolean connect(){
		if(mSocket != null){
			return true;
		}

		try {

			mSocket = new LocalSocket();
			LocalSocketAddress address = new LocalSocketAddress("firewalld",LocalSocketAddress.Namespace.RESERVED);
			mSocket.connect(address);


			mIn = mSocket.getInputStream();
			mOut = mSocket.getOutputStream();
			isConnected = true;
			mSendThread.start();
			mReceiveThread.start();
		} catch (IOException e) {
			e.printStackTrace();
			disconnect();
			return false;
		}


		return true;
	}

	
	/**
	 * disconnect server
	 */
	void disconnect(){
		if(mSocket != null){
			try {
				mSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if(mIn != null){
			try {
				mIn.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}


		if(mOut != null){
			try {
				mOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		isConnected = false;

		synchronized(mSendLock){
			mSendLock.notify();
		}

		synchronized(mReceiveLock){
			mReceiveLock.notify();
		}

	}

	
	
	public interface OnDataReadListener{
		public void onRead(String data);
	}
	
	
	private class SendThread extends Thread{
		public void run(){
			while(isConnected){
				synchronized(mSendLock){
					try {
						mSendLock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					if(!isConnected){
						break;
					}
					int len = mCMD.length();
					byte[] cmdbuffer= mCMD.getBytes();
					byte[] cmdlen = new byte[2];
					cmdlen[0] = (byte)(len & 0xff);
					cmdlen[1] = (byte)((len >> 8) & 0xff);
					try {
						mOut.write(cmdlen);
						mOut.write(cmdbuffer);
					} catch (IOException e) {
						e.printStackTrace();
					}
					synchronized(mReceiveLock){
						mReceiveLock.notify();
					}
				}
			}

		}
	}


	private class ReceiveThread extends Thread{
		public void run(){
			while(isConnected){
				synchronized(mReceiveLock){
					try {
						mReceiveLock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					if(!isConnected){
						break;
					}
					try {
						byte[] buffer = new byte[2];
						mIn.read(buffer, 0, 2);
						int len = (((int)buffer[0]) & 0xff) | ((((int)buffer[1]) & 0xff) << 8 );
						buffer = new byte[len];
						mIn.read(buffer, 0, len);
						if(L!= null){
							L.onRead(byte2String(buffer,len-2));
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}
	}
	
	private  String byte2String(byte[] bytes,int len){
		if(bytes == null)
			return null;
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<len;i++){
			sb.append((char)bytes[i]);	
		}
		return sb.toString();
	}
}
