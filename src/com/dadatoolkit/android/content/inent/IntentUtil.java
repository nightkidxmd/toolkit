package com.dadatoolkit.android.content.inent;

import java.io.File;

import android.content.Intent;
import android.net.Uri;

public class IntentUtil {
	
	
	public  static Intent getHtmlFileIntent(String path){
		Uri uri = Uri.parse(path).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content")
				.encodedPath(path).build();
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(uri, "text/html");
		return intent;
	}
	
	
	
	
	
	public  static Intent getImageFileIntent(String path){
		Uri uri = Uri.fromFile(new File(path));
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(uri, "image/*");
		return intent;
	}
	
	public  static Intent getPdfFileIntent(String path){
		Uri uri = Uri.fromFile(new File(path));
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(uri, "application/pdf");
		return intent;
	}
	
	public  static Intent getTextFileIntent(String param,boolean isFromFile){
		Uri uri = null;
		if(isFromFile){
			uri = Uri.fromFile(new File(param));
		}else{
			uri = Uri.parse(param);
		}
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(uri, "text/plain");
		return intent;
	}
	
	public  static Intent getAudioFileIntent(String param){
		Uri uri = Uri.fromFile(new File(param));

		Intent intent = new Intent(Intent.ACTION_VIEW);
		
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot",0);
		intent.putExtra("configchange", 0);
		intent.setDataAndType(uri, "audio/*");
		return intent;
	}
	
	public  static Intent getVedioFileIntent(String param){
		Uri uri = Uri.fromFile(new File(param));

		Intent intent = new Intent(Intent.ACTION_VIEW);
		
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot",0);
		intent.putExtra("configchange", 0);
		intent.setDataAndType(uri, "video/*");
		return intent;
	}
	
	
	public  static Intent getChmFileIntent(String param){
		Uri uri = Uri.fromFile(new File(param));

		Intent intent = new Intent(Intent.ACTION_VIEW);
		
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(uri, "application/x-chm");
		return intent;
	}
	
	public  static Intent getWordFileIntent(String param){
		Uri uri = Uri.fromFile(new File(param));

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(uri, "application/ms-word");
		return intent;
	}
	
	
	public  static Intent getExcelFileIntent(String param){
		Uri uri = Uri.fromFile(new File(param));

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(uri, "application/ms-excel");
		return intent;
	}
	
	public  static Intent getPptFileIntent(String param){
		Uri uri = Uri.fromFile(new File(param));

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(uri, "application/ms-powerpoint");
		return intent;
	}

}
