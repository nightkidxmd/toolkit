package com.dadatoolkit.android.net.socket;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.Message;
import android.util.Log;

import com.dadatoolkit.android.net.socket.protocol.Protocol;
import com.dadatoolkit.android.os.HandlerThread;
import com.dadatoolkit.android.os.HandlerThread.OnHandleListener;
import com.dadatoolkit.java.ByteUtil;

public class SocketClient implements OnHandleListener {
	private String mHostIP;
	private int mPort;
	private HandlerThread H;
	private static final int MESSAGE_HEART_BEAT_TIMEOUT = 1;
	private static final int MESSAGE_LISTEN = 2;
	private StatusListener L;
	private boolean isConnected = true;
	private OnMessageListener M;
	private Object mMsgLock = new Object();
	private byte[] mMsg;
	
	public interface StatusListener{
		public void onDisconnected();
	}
	
	public interface OnMessageListener{
		public void onMessage(String msg);
	}
	
	
	public SocketClient(String ip,int port,StatusListener l, OnMessageListener m){
		mHostIP = ip;
		mPort = port;
		L = l;
		M = m;
		H = new HandlerThread(mListen);
		H.setOnHandleListener(this);
		H.start();
	}
  
//    public void listen() {   
//    	Message msg = new Message();
//    	msg.what = MESSAGE_LISTEN;
//    	H.sendMessage(msg);
//
//    }
	
	private Runnable mListen = new Runnable(){

		@Override
		   public void run(){
	        Socket socket = null;   
	        DataInputStream in = null;
	        Log.e("DADA","sleep and wait for server");
	        try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        Log.e("DADA","sleep finished");
	        try {   
	            socket = new Socket(mHostIP, mPort);
	            Log.e("DADA",""+socket.isConnected());
	            in = new DataInputStream(socket.getInputStream()); 
	            while(socket.isConnected()){
	            	byte[] lengthbyte = new byte[4];
	            	in.read(lengthbyte);
	            	int length = ByteUtil.byte2Int(lengthbyte);
	            	byte[] buffer = new byte[length];
	            	in.read(buffer);
	            	String res = Protocol.decodeWithCrypt(buffer);

		            if(res.equals(HeartBeat.HEART_BEAT)){
		            	heartBeat();
		            }else{
		            	M.onMessage(res);
		            }
	            }
	        } catch (UnknownHostException e) {   
	            e.printStackTrace();   
	        } catch (IOException e) {   
	            e.printStackTrace();   
	        } finally {  
	        	 try {  
		        	if(in != null){
		        		in.close();
		        	}
		            if (socket != null) {                   
		                    socket.close();                   
		            }  
	        	 } catch (IOException e) {   
	        		 e.printStackTrace();
	             }  
	        	 L.onDisconnected();
	        	 
	        }   
	    }
		
	};
    
	public void sendMessage(String msg) {
		sendMessage(Protocol.encodeWithCrypt(msg));
	}
	
	/**
	 * 
	 * @param buffer Using {@link com.dadatoolkit.android.net.socket.protocol.Protocol} to get encoded buffer
	 */

	public void sendMessage(byte[] buffer) {
		synchronized (mMsgLock) {
			mMsg = buffer;
			mMsgLock.notify();
		}
	}
	
	public void close(){
		H.quit();
	}
    
    private void heartBeat(){
//        H.getThreadHandler().removeMessages(MESSAGE_HEART_BEAT_TIMEOUT);
//        Message msg =  H.getThreadHandler().obtainMessage(MESSAGE_HEART_BEAT_TIMEOUT);
//        H.sendMessage(msg, HeartBeat.CLIENT_HEART_BEAT_TIMEOUT);
    	Log.e("DADA","heartBeat");
    }

	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		switch(msg.what){
			case MESSAGE_HEART_BEAT_TIMEOUT:
				isConnected = false;
				L.onDisconnected();
				break;
			case MESSAGE_LISTEN:
				//listenInner();
			break;
			default:
				break;
		}
	}   
}
