package com.dadatoolkit.android.net.socket;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;

import android.os.Message;
import android.util.Log;

import com.dadatoolkit.android.net.socket.protocol.Protocol;
import com.dadatoolkit.android.os.HandlerThread;
import com.dadatoolkit.android.os.HandlerThread.OnHandleListener;

public class SocketServer implements OnHandleListener {
	private HandlerThread H;

	private OnClientConnectionListener mListener;

	private static final int MESSAGE_LISTEN        = 0;
	private static final int MESSAGE_CONNECTED     = 1;
	private static final int MESSAGE_DISCONNECTED  = 2;
	private static final int MESSAGE_HEART_BEAT    = 3;

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
		H = new HandlerThread(new Initial());
		H.setOnHandleListener(this);
		H.start();
	}

	public void listen() {
		H.getThreadHandler().obtainMessage(MESSAGE_LISTEN).sendToTarget();
	}

	private void listenInner() {
		Log.i("DADA", "Start listening..."); 
		try {
			if (mServerSocket == null) {
				Log.e("DADA", "Opps~ mServerSocket:" + mServerSocket);
				return;
			}
			mClient = mServerSocket.accept();

			Log.i("DADA", "Client connected:" + mClient.getInetAddress());
			if (mListener != null)
				mListener.onClientConnected();
			


			H.getThreadHandler().obtainMessage(MESSAGE_CONNECTED)
					.sendToTarget();
			
			Message heartBeat = H.getThreadHandler().obtainMessage(MESSAGE_HEART_BEAT);
			H.sendMessage(heartBeat, HeartBeat.SERVER_HEART_BEAT_TIMEOUT);
			
		} catch (IOException e) {
			mListener.onError(-1, e.getMessage());
		}
	}

	public void close() {
		if (mServerSocket != null) {
			try {
				mServerSocket.close();
				mServerSocket = null;
			} catch (IOException e) {
				Log.e("DADA", "IO error:" + e.getMessage()+" port:"+mRemotePort);
			}
		}
		H.quit();
	}

	private void run() {
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
			Log.e("DADA", "IO error:" + e.getMessage());
			mListener.onClientDisConnected();

		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					Log.e("DADA", "IO error:" + e.getMessage());
				}

			}
			close();
		}
	}

	public void setOnClientConnectedListener(OnClientConnectionListener l) {
		mListener = l;
	}

	public void sendMessage(String msg) {
		sendMessage(Protocol.encodeWithCrypt(msg));//codecstr.toString().getBytes("UTF-8"));
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

	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		switch (msg.what) {
		case MESSAGE_LISTEN:
			listenInner();
			break;
		case MESSAGE_CONNECTED:
			run();
			break;
		case MESSAGE_HEART_BEAT:
			sendMessage(HeartBeat.HEART_BEAT);
			Message heartBeat = H.getThreadHandler().obtainMessage(MESSAGE_HEART_BEAT);
			H.sendMessage(heartBeat, HeartBeat.SERVER_HEART_BEAT_TIMEOUT);
			break;
		default:
			break;
		}
	}

	private class Initial implements Runnable {

		@Override
		public void run() {
			try {
//				Runtime.getRuntime()
//						.exec("adb forward tcp:" + mLocalPort + " tcp:"
//								+ mRemotePort);
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}

				mServerSocket = new ServerSocket(mRemotePort);
				if(mListener != null){
					mListener.onInitialDone();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
