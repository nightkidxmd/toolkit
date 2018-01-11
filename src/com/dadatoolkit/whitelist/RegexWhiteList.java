package com.dadatoolkit.whitelist;

import android.annotation.TargetApi;
import android.os.Build;
/**
 * 
 * @author maoda.xu@samsung.com
 *
 */
public class RegexWhiteList extends WhiteList {



	public RegexWhiteList(String type, String content) {
		super(type, content);
		// TODO Auto-generated constructor stub
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@Override
	public boolean check(String stringName,String content) {
		// TODO Auto-generated method stub
		String s = content.replaceAll(whitecontent, ""); 
		s = s.replaceAll("[\\u2013\\u2025\\u2222\\ua0a0]", "");
		s = s.trim();
//		if(content.contains("^-$") && !s.isEmpty()){
//			 Log.e("DADA","conent:"+content+" s:"+s+" Unicode:"+StringUtil.string2Unicode(s));
//		}
		return s.isEmpty();
	}

}
