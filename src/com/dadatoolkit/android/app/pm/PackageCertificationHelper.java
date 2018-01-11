package com.dadatoolkit.android.app.pm;

import com.dadatoolkit.java.secure.CertificateUtil;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;

public class PackageCertificationHelper {

	public static String getPackageCertificationIssue(Context context,String pkg){
		String issue="";
		
		PackageManager pm = context.getPackageManager();
		
		try {
			PackageInfo pi=pm.getPackageInfo(pkg, PackageManager.GET_SIGNATURES);
			Signature[] signatures=pi.signatures;
			issue = new CertificateUtil(signatures).getCertificationIssue();
			
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return issue;
	}
}
