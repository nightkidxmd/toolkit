package com.dadatoolkit.android.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public abstract class WelcomActivity extends Activity {

	private Handler H = new Handler(Looper.getMainLooper()){
		@Override
		public void handleMessage(Message msg){
			Intent start = getLauchingIntent();
			if(start != null)
				startActivity(getLauchingIntent());
			finish();
		}
	};

	@Override
	protected void onResume(){
		super.onResume();
		H.sendEmptyMessageDelayed(0, 5000);
	}

	@Override
	public void onBackPressed(){

	}


	public abstract Intent getLauchingIntent();
}
