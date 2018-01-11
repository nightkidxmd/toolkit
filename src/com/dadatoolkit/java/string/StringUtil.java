package com.dadatoolkit.java.string;

import com.dadatoolkit.whitelist.DefaultBWList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
/**
 * 
 * @author maoda.xu@samsung.com
 *
 */
@SuppressLint("DefaultLocale")
public class StringUtil {
	
	public static String string2Unicode(String s) {
	    String unicode = "";
	    char[] charAry = new char[s.length()];
	    for(int i=0; i<charAry.length; i++) {
		    charAry[i] = (char)s.charAt(i);
		    unicode+="\\U" + Integer.toString(charAry[i], 16);
	    }
	    return unicode;
	}

	@SuppressLint("DefaultLocale")
	public static String unicode2String(String unicodeStr){
	   StringBuffer sb = new StringBuffer();
	   String str[] = unicodeStr.toUpperCase().split("\\\\U");
	   for(int i=0;i<str.length;i++){
	    if(str[i].equals("")) continue;
	    char c = (char)Integer.parseInt(str[i].trim(),16);
	    sb.append(c);
	   }
	   return sb.toString();
	}
	
	
	private static DefaultBWList dbwl = DefaultBWList.getInstance();

		
	
	
	
	@SuppressLint("DefaultLocale")
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public static boolean isEngishOnly(String in){
		//1.Check No CHN String
		String s = in.replaceAll("[0-9a-zA-Z]", "");
		s = s.replaceAll("[\\\\_\\-`~!@#$%^&*()+=|{}':;',//[//].<>/?~£¡@#£¤%¡­¡­&*£¨£©¡ª¡ª+|{}¡¾¡¿¡®£»£º¡±¡°¡¯¡££¬¡¢£¿¡æ¨H¡ã\\u2013\\u2025\\u2222\\ua0a0]", "");
		s = s.trim();
		boolean ret = s.isEmpty();
		if(!ret){
			if(dbwl.isBlack(StringUtil.string2Unicode(s).toUpperCase())){
				ret = true;
			}	
		}
		//2.Check number and special character only
		s = in.replaceAll("[0-9\\\\_\\-`~!@#$%^&*()+=|{}':;',//[//].<>/?~£¡@#£¤%¡­¡­&*£¨£©¡ª¡ª+|{}¡¾¡¿¡®£»£º¡±¡°¡¯¡££¬¡¢£¿¡æ¨H¡ã\\u2013\\u2025\\u2222\\ua0a0]", "");
		s.trim();
		ret = !s.isEmpty() && ret;
		//3.Check is url
		ret = !in.startsWith("http:") && !in.startsWith("market:") &&
				!in.startsWith("samsungapps:") && ret;
		//4.Check wl
		s = in.trim();
		ret = !dbwl.isWhite(s.toUpperCase())&& ret;
		
		
		
		return ret;
	}
	
	
	@SuppressLint("DefaultLocale")
	public static boolean hasWiFi(String s){
		s = s.toLowerCase();
		return s.contains("wi-fi") || s.contains("wifi");
	}


}
