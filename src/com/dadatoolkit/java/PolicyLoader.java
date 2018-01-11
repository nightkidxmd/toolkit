package com.dadatoolkit.java;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.util.Log;

import com.dadatoolkit.android.xml.XmlParser.OnParserListener;
import com.dadatoolkit.api.XmlParserApi;
import com.dadatoolkit.java.io.FileUtil;
import com.dadatoolkit.java.io.FileUtil.OnFileFoundListner;
/**
 * 
 * @author maoda.xu@samsung.com
 *
 */
public class PolicyLoader implements OnFileFoundListner {

	private PackageManager mPm = null;
	private OnLoadDoneListener mListener;
	private ArrayList<String> mWork = new ArrayList<String>();

	private String[] filter;

	public interface OnLoadDoneListener{
		public void onLoadDone(String[] data);
		public void onLoadDone(String pkg);
		public void onLoadDone();
	}

	public PolicyLoader(Context context,OnLoadDoneListener l,String[] filter){
		mPm = context.getPackageManager();
		mListener = l;
		this.filter = filter;

	}


	public void load(){
		FileUtil.scan(this,new String[]{"whitelist","Excel","OngoingWork","TGConfig"},filter,true);
	}


	@Override
	public void onFound(String pkg ,Object lock) {
		// TODO Auto-generated method stub

	}

	public boolean hasWork(){
		boolean hasWork = false;
		synchronized(mWork){
			hasWork =  mWork.size() > 0;
		}
		return hasWork;
	}



	@Override
	public void onFoundDone() {
		// TODO Auto-generated method stub
		mListener.onLoadDone();

	}
	@Override
	public void onFound(String pkg, InputStream is) {
		// TODO Auto-generated method stub
		Log.e("DADA","onFound:"+pkg);
		executorService.submit(new LoadWork(pkg,is));

	}


	private class LoadWork implements Runnable{
		private String pkg;
		private InputStream is;
		private Object mLock = new Object();
		public LoadWork(String pkg,InputStream is){
			this.pkg = pkg;
			this.is = is;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			synchronized(mWork){
				mWork.add(pkg);
			}

			XmlParserApi parser = new XmlParserApi();
			parser.setOnParserListener(new MyParserListener(mLock,pkg));
			parser.start(is);
			synchronized(mLock){
				try {
					mLock.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	private ExecutorService executorService = Executors.newFixedThreadPool(10);


	public void exit(){
		if(!executorService.isTerminated())
			executorService.shutdownNow();
	}


	private class MyParserListener implements OnParserListener{
		private String pkg;
		private Resources R;
		private Object mLock;

		public MyParserListener(Object lock,String pkg){
			this.pkg = pkg;
			this.mLock = lock;
			try {
				R = mPm.getResourcesForApplication(pkg);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void onParse(int eventType, XmlPullParser parser) {
			// TODO Auto-generated method stub
			switch(eventType){
			case XmlPullParser.START_TAG:
				if(parser.getName().equals("string")){
					for(int i =0 ; i<parser.getAttributeCount();i++){
						if(parser.getAttributeName(i).equals("name") && R != null){
							String name = parser.getAttributeValue(i);
							int resId = R.getIdentifier(name, "string", pkg);
							try{
								String[] data = new String[]{pkg,name,R.getString(resId)};
								mListener.onLoadDone(data);
							}catch(NotFoundException e){
								Log.w("DADA", "Res Not Found:" + name);
							}

						}
					}
				}
				break;
			case XmlPullParser.END_TAG:
				break;
			default:
				break;
			}   
		}

		@Override
		public void onParseDone(int result) {
			// TODO Auto-generated method stub
			synchronized(mWork){
				mWork.remove(pkg);
			}
			mListener.onLoadDone(pkg);
			synchronized(mLock){
				mLock.notifyAll();
			}

		}

	}

}
