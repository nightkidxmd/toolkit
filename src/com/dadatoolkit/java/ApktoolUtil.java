package com.dadatoolkit.java;
import java.io.File;

import com.dadatoolkit.java.io.CMDUtil;

import brut.apktool.Main;
/**
 * 
 * @author maoda.xu@samsung.com
 *
 */
public class ApktoolUtil {
	public static void installFW(String path){
		try {
			Main.main(new String[]{"if",path});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());		
		}
	}
		
	public static void decompile(String input,String output,String fwdir){
		try {
			Main.main(new String[]{"d","-b","-s","-f","--keep-broken-res","--frame-path",fwdir,input,output});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());		
		}
	}
	/**
	 * @deprecated  Have block issue.Use {@link #decompile(String, String)} instead.
	 *              If occurs sudden exit, plz install all fw needed.
	 * @param input
	 * @param output
	 */
	public static void decompileInNewProcess(String input,String output){
		File file = new File("");
		String path = file.getAbsolutePath();
		if(!path.endsWith("\\")){
			path = path+"\\";
		}
		CMDUtil.execute("java -jar "+path+"tool\\apktool.jar d -b -s -f "+input+" "+output, true);
	}

}
