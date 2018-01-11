package com.dadatoolkit.android.app.pm;

import java.util.HashSet;

import android.content.Context;

public class PackageManagerUtil {
	
	private static PackageManagerUtil mInstance;
	
	private static final HashSet<String> mLibs = new HashSet<String>();
	
	private PackageManagerUtil(Context context){
		String[] libs = context.getPackageManager().getSystemSharedLibraryNames();
		for(String l:libs){
			mLibs.add(l);
		}
	}
	
	public static PackageManagerUtil getInstance(Context context){
		if(mInstance == null){
			mInstance = new PackageManagerUtil(context);
		}
		
		return mInstance;
	}
	
	public boolean hasSystemSharedLibrary(String name){
		return mLibs.contains(name);
		
	}
}
