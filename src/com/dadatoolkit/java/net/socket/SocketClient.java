package com.dadatoolkit.java.net.socket;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.Message;

import com.dadatoolkit.android.net.socket.HeartBeat;
import com.dadatoolkit.android.net.socket.protocol.Protocol;
import com.dadatoolkit.android.os.HandlerThread;
import com.dadatoolkit.android.os.HandlerThread.OnHandleListener;
import com.dadatoolkit.java.ByteUtil;

public class SocketClient implements OnHandleListener {
	private String mHostIP;
	private int mPort;
	//private HandlerThread H;
	private static final int MESSAGE_HEART_BEAT_TIMEOUT = 1;
	private StatusListener L;
	private boolean isConnected = true;
	
	public interface StatusListener{
		public void onDisconnected();
	}
	
	
	public SocketClient(String ip,int port,StatusListener l){
		mHostIP = ip;
		mPort = port;
		L = l;
	}
  
    public void listen() {   
        Socket socket = null;   
        DataInputStream in = null;
        
        try {   
            socket = new Socket(mHostIP, mPort);
            System.err.println(socket.isConnected());
            in = new DataInputStream(socket.getInputStream()); 
            while(socket.isConnected()){
            	byte[] buffer = new byte[1024*4];
	            in.read(buffer);
	            String res = ByteUtil.byte2String(buffer);
	            if(res.trim().isEmpty()){
	            	continue;
	            }
	            res = Protocol.decodec(res);

	            if(res.equals(HeartBeat.HEART_BEAT)){
	            	heartBeat();
	            }else{
	            	System.out.println(res);
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
    
    private void heartBeat(){
        //H.getThreadHandler().removeMessages(MESSAGE_HEART_BEAT_TIMEOUT);
        //Message msg =  H.getThreadHandler().obtainMessage(MESSAGE_HEART_BEAT_TIMEOUT);
       // H.sendMessage(msg, HeartBeat.CLIENT_HEART_BEAT_TIMEOUT);
    }

	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		switch(msg.what){
			case MESSAGE_HEART_BEAT_TIMEOUT:
				isConnected = false;
				L.onDisconnected();
			break;
			default:
				break;
		}
	}   
}
