package com.dadatoolkit.linux.firewall;

import java.util.ArrayList;

import com.dadatoolkit.android.su.NotRootException;
import com.dadatoolkit.android.su.SuPolicy.OnRootStateListener;

import android.content.Context;

public class DadaFirewall implements OnRootStateListener {
	private static DadaFirewall mInstance;
	private FirewallHelper mHelper;
	private ArrayList<String> mRejectList = new ArrayList<String>();
	private OnWallBuildListener L = null;


	private DadaFirewall(Context context){
		try {
			mHelper = FirewallHelper.getInstance(context);
			mHelper.setOnRootStateListener(this);
			mHelper.startDaemon();
			mHelper.aquireRoot();
		} catch (NotRootException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
	
	public void rebuildWall(){
		mHelper.aquireRoot();
	}

	public static DadaFirewall getInstance(Context context){
		if(mInstance == null){
			mInstance = new DadaFirewall(context);
		}

		return mInstance;
	}

	public void close(){
		mHelper.stopDaemon();
	}

	public void setOnWallBuildListener(OnWallBuildListener l){
		L = l;

	}


	public void reject(int uid){
		String UID = String.valueOf(uid);
		for( String u:mRejectList){
			if(u.equals(UID)){
				return;
			}
		}	
		mRejectList.add(UID);
		mHelper.rejectUID(uid);
	}

	public void accept(int uid){
		String UID = String.valueOf(uid);
		for( String u:mRejectList){
			if(u.equals(UID)){
				mRejectList.remove(UID);
				mHelper.allowUID(uid);
				return;
			}
		}
	}

	@Override
	public void onRoot(boolean success) {
		// TODO Auto-generated method stub
		if(success){
			mHelper.rebuild();
		}
		if(L != null){
			L.onWallBuild(success);
		}
	}
	
	public boolean isRooted(){
		return mHelper.isRooted();
	}


	public interface OnWallBuildListener{
		public void onWallBuild(boolean success);
	}


}
