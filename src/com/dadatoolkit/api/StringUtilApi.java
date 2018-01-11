package com.dadatoolkit.api;

import com.dadatoolkit.java.string.StringUtil;
/**
 * 
 * @author maoda.xu@samsung.com
 *
 */
public class StringUtilApi {
	public static String string2Unicode(String s) {
	    return StringUtil.string2Unicode(s);
	}

	public static String unicode2String(String unicodeStr){
	   return StringUtil.unicode2String(unicodeStr);
	}
	
	public static boolean isEngishOnly(String in){
		return StringUtil.isEngishOnly(in);
	}
	
	public static boolean hasWiFi(String s){
		return StringUtil.hasWiFi(s);
	}
}
