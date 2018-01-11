package com.dadatoolkit.java.net.socket;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;

import android.util.Log;

import com.dadatoolkit.android.net.socket.protocol.Protocol;

public class SocketServer extends Thread {
	//private HandlerThread H;

	private OnClientConnectionListener mListener;

	private static final int MESSAGE_LISTEN        = 0;
	private static final int MESSAGE_CONNECTED     = 1;
	private static final int MESSAGE_DISCONNECTED  = 2;
	private static final int MESSAGE_HEART_BEAT    = 3;
	// private static final int MESSAGE_CLOSE = 3;

	// private String mIP;
	private int mLocalPort = 0;
	private int mRemotePort = 0;

	private ServerSocket mServerSocket;
	private Socket mClient;

	private Object mMsgLock = new Object();
	private byte[] mMsg;

	public interface OnClientConnectionListener {
		public void onInitialDone();

		public void onClientConnected();

		public void onClientDisConnected();

		public void onError(int error, String errorMsg);
	}

	public SocketServer(int localPort, int remotePort) {
		mLocalPort = localPort;
		mRemotePort = remotePort;
		//H = new HandlerThread(new Initial());
		//H.setOnHandleListener(this);
		//H.start();
	}

	public void listen() {
		//H.getThreadHandler().obtainMessage(MESSAGE_LISTEN).sendToTarget();
		listenInner();
	}

	private void listenInner() {
		System.out.println("Start listening...");
		try {
			if (mServerSocket == null) {
				//System.err.println("Opps~ mServerSocket:" + mServerSocket);
				return;
			}
			mClient = mServerSocket.accept();

			System.out.println("Client connected:" + mClient.getInetAddress());
			if (mListener != null)
				mListener.onClientConnected();
			


			//H.getThreadHandler().obtainMessage(MESSAGE_CONNECTED)
					//.sendToTarget();
			
			//Message heartBeat = H.getThreadHandler().obtainMessage(MESSAGE_HEART_BEAT);
			//H.sendMessage(heartBeat, HeartBeat.SERVER_HEART_BEAT_TIMEOUT);
			
		} catch (IOException e) {
			System.err.println("IO error:" + e.getMessage());
			mListener.onError(-1, e.getMessage());
		}
	}

	public void close() {
		if (mServerSocket != null) {
			try {
				mServerSocket.close();
				mServerSocket = null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.println("IO error:" + e.getMessage());
			}
		}
		//H.quit();
	}

	public void run() {
		init();
		
		
		
		BufferedOutputStream out = null;
		try {
			if (mClient != null)
				out = new BufferedOutputStream(mClient.getOutputStream());
			while (mClient != null && mClient.isConnected()
					&& !mClient.isClosed()) {
				synchronized (mMsgLock) {
					mMsgLock.wait();
				}
				out.write(mMsg);
				out.flush();
			}
		} catch (Exception e) {
			System.err.println("IO error:" + e.getMessage());
			mListener.onClientDisConnected();

		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.err.println("IO error:" + e.getMessage());
				}

			}
			close();
		}
	}

	public void setOnClientConnectedListener(OnClientConnectionListener l) {
		mListener = l;
	}

	public void sendMessage(String msg) {
		String codecstr = Protocol.encodec(msg);
		try {
			sendMessage(codecstr.toString().getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
	
	
	private void init(){
		try {
			Runtime.getRuntime()
					.exec("adb forward tcp:" + mLocalPort + " tcp:"
							+ mRemotePort);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			mServerSocket = new ServerSocket(mRemotePort);
			
			//mListener.onInitialDone();
			listen();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
