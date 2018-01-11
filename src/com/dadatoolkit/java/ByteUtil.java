package com.dadatoolkit.java;

import java.nio.charset.Charset;

import android.util.Log;

public class ByteUtil {

	public static String byte2String(byte[] bytes){
		if(bytes == null)
			return null;
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<bytes.length;i++){
			sb.append((char)bytes[i]);	
		}
		return sb.toString();
	}


	public static byte[] string2Byte(String str){
		return str.getBytes();
	}

	public static byte[] string2CharsetByte(String str,Charset charset){
		return str.getBytes(charset);
	}


	public static String byte2Unicode(byte obyte[],int start,int end){
		StringBuffer sb = new StringBuffer("");
		for(int j = start; j < end; )
		{
			int lw = obyte[j++];
			if (lw < 0) lw += 256;
			int hi = obyte[j++];
			if (hi < 0) hi += 256;
			char c = (char)(lw + (hi << 8));
			sb.append(c);
		}
		return sb.toString();
	}


	public static String byte2Unicode(byte obyte[],int len){
		return byte2Unicode(obyte,0,len);
	}


	public static byte[] unicode2Byte(String s){
		int len = s.length();
		byte obyte[] = new byte[len << 1];
		int j = 0;
		for(int i = 0; i < len; i++)
		{
			char c = s.charAt(i);
			obyte[j++] = (byte)(c & 0xff);
			obyte[j++] = (byte)(c >> 8);
		}

		return obyte;
	}


	public static byte[] int2Byte(int integer){
		byte[] ret = new byte[4];
		for(int i=0;i<4;i++){
			ret[i] = (byte)(integer >> (24-8*i));
		}
		return ret;
	}
	
	
	public static int byte2Int(byte[] buffer){
		int ret = 0;
		for(int i=0;i<4;i++){
			ret |= (buffer[i] & 0xFF) << (24-8*i);
		}
		return ret;
	}
	
	
	public static void logByteArray(byte[] array){
		logByteArray("DADA","ByteArray",array);
	}
	
	public static void logByteArray(String tag,String prefix,byte[] array){
		StringBuilder sb = new StringBuilder();
		for(byte b:array){
			sb.append(String.format("0x%x ", b));
		}
		Log.i(tag,prefix+"---"+array.length+":"+sb.toString());
	}
}
