package com.dadatoolkit.android.su;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.dadatoolkit.android.os.HandlerThread;
import com.dadatoolkit.android.os.HandlerThread.OnHandleListener;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Message;
/**
 * @hide
 * @author maoda.xu@samsung.com
 *
 */
public class SuImpl extends SuPolicy implements OnHandleListener {

	private boolean mIsSuAvailable = false;
	private final String SU = "/system/bin/su";
	private String mVersionName;
	private int  mVersionCode = -1;
	private static java.lang.Process proc = null;
	private OnRootStateListener L = null;
	private static final int MESSAGE_AQUIRE_ROOT = 0;

	private boolean isRooted = false;

	private HandlerThread H = null;

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public SuImpl(Context context) throws NotRootException {
		super(context);
		// TODO Auto-generated constructor stub
		init();
		if(!validSu()) throw new NotRootException();
		
		
		H = new HandlerThread("SU");
		H.setOnHandleListener(this);
	}
	
	private void init(){
		File suFile = new File(SU);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
			mIsSuAvailable = suFile.exists() && suFile.canExecute();
		else{
			mIsSuAvailable = suFile.exists();
		}
	}

	@Override
	public String getVersionName() {
		// TODO Auto-generated method stub
		if(mVersionName == null)
			mVersionName = execute("su -v",true);

		return mVersionName;
	}

	@Override
	public int getVersionCode() {
		// TODO Auto-generated method stub
		if(mVersionCode == -1){
			String versionCode = execute("su -V",true);
			mVersionCode = versionCode == null ?-1:Integer.parseInt(versionCode);
		}

		return mVersionCode;
	}



	public String execute(String cmd){
		return execute(cmd,false);
	}

	private boolean validSu(){
		return mIsSuAvailable;
	}

	@Override
	public String execute(String cmd, boolean isGetResult) {
		String line = null;
		BufferedReader reader = null;
		try {
			OutputStream out = proc.getOutputStream();
			cmd +="\n";
			out.write(cmd.getBytes());
			out.flush();
			if(isGetResult){
				reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				line = reader.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(reader != null ){
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
				}
			}

		}

		return line;
	}

	private void handleAquireRoot() {
		// TODO Auto-generated method stub

		boolean success = false;

		if(validSu()){
			try {
				proc = Runtime.getRuntime().exec("su");
				String uid = execute("id",true);
				if(uid != null && uid.contains("root")){
					success = true;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if(L!=null){
			L.onRoot(success);
		}

		isRooted = success;
	}

	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		switch(msg.what){
		case MESSAGE_AQUIRE_ROOT:
			handleAquireRoot();
		default:
			break;

		}
	}

	@Override
	public void stopDaemon() {
		// TODO Auto-generated method stub
		if(H.isAlive()){
			H.quit();
		}

	}

	@Override
	public void startDaemon() {
		// TODO Auto-generated method stub
		if(!H.isAlive()){
			H.start();
		}
	}

	@Override
	public void aquireRoot() {
		// TODO Auto-generated method stub

		if(isRooted){
			return;
		}

		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		H.sendMessage(H.getThreadHandler().obtainMessage(MESSAGE_AQUIRE_ROOT));
	}

	@Override
	public void setOnRootStateListener(OnRootStateListener l) {
		// TODO Auto-generated method stub
		L = l;
	}

	@Override
	public boolean isRooted() {
		// TODO Auto-generated method stub
		return isRooted;
	}


}
