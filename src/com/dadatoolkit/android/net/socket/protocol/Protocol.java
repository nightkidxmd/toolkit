package com.dadatoolkit.android.net.socket.protocol;

import com.dadatoolkit.compressutils.ByteCompressUtil;
import com.dadatoolkit.java.ByteUtil;
import com.dadatoolkit.java.secure.DESUtils;
import com.dadatoolkit.java.string.StringUtil;
/**
 * 
 * |数据长度值位数(2GB)|数据长度(字节)|数据|
 * @author maoda.xu@samsung.com
 *
 */
public class Protocol {
	public static String SOCKET_STOP_CMD="SOCKET_STOP_CMD";
	
	
	
	/**
	 * @deprecated
	 * @param original
	 * @return
	 */
	public static String encodec(String original){
		StringBuilder codecstr = new StringBuilder();
		original = StringUtil.string2Unicode(original);
		int strlength  = original.length();
		int length = Integer.toHexString(strlength).length();
		codecstr.append(Integer.toHexString(length));
		codecstr.append(Integer.toHexString(strlength));
		codecstr.append(original);
		return codecstr.toString();
	}
	
	/**
	 * @deprecated
	 * @param original
	 * @return
	 */
	
	public static String decodec(String original){
        int temp = Integer.parseInt(original.substring(0, 1),16); 
        int dataLenth = Integer.parseInt(original.substring(1, 1+temp),16);
        original = original.substring(temp+1,temp+1+dataLenth);
        original = StringUtil.unicode2String(original);
		return original;
	}
	
	/**
	 * Use DES to encrypt data
	 * @param original
	 * @return
	 */
	public static byte[] encodeWithCrypt(String original){
		original = StringUtil.string2Unicode(original);
		byte[] encrypted = DESUtils.encrypt(original, "12345678");
		ByteUtil.logByteArray("DADA","encrypted",encrypted);
		byte[] zipped = ByteCompressUtil.gZip(encrypted);
		ByteUtil.logByteArray("DADA","zipped",zipped);
		byte[] lengthbyte =  ByteUtil.int2Byte(zipped.length);
		byte[] finaldata = new byte[zipped.length+4];
		System.arraycopy(lengthbyte, 0, finaldata, 0, lengthbyte.length);
		System.arraycopy(zipped, 0, finaldata, 4, zipped.length);
		return finaldata;
	}
	
	

	/**
	 * decrypt
	 * @param original
	 * @return
	 */
	
	public static String decodeWithCrypt(byte[] original){
		//Log.e("DADA","raw:"+ByteUtil.byte2String(original));
		//ByteUtil.logByteArray("DADA","raw",original);
		byte[] unzipped = ByteCompressUtil.unGzip(original);
		byte[] decrypted = DESUtils.decrypt(unzipped, "12345678");
		return StringUtil.unicode2String(ByteUtil.byte2String(decrypted));
	}
	

}
