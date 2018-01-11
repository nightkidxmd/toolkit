package com.dadatoolkit.android.net.socket;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.dadatoolkit.android.net.socket.protocol.Protocol;
import com.dadatoolkit.java.ByteUtil;

public class RXThread extends Thread{
	private InputStream mInput;
	private OnReceiveListener l;
	
	
	public RXThread(InputStream is,OnReceiveListener l){
		mInput = is;
		this.l = l;
	}
	
	
	
	public void run(){
		DataInputStream in = null;
	     try {
	    	 in = new DataInputStream(mInput);
			 while(true){
				   byte[] buffer = new byte[1024*4];
				   in.read(buffer);
				   String s = ByteUtil.byte2String(buffer);
				   s = s.trim();
				   if(s.isEmpty()){
					   continue;
				   }
			       l.receive(buffer);       
			       if(s.equals(Protocol.SOCKET_STOP_CMD)){
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
					if(mInput != null){
						mInput.close();
						mInput = null;
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
			if(mInput != null){
				mInput.close();
			}
			
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	
	public interface OnReceiveListener{
		public void receive(byte[] buffer);
	}
	
	

}
