package com.dadatoolkit.api;

import java.io.File;

import com.dadatoolkit.java.io.FileUtil;
import com.dadatoolkit.java.io.FileUtil.OnFileFoundListner;
/**
 * 
 * @author maoda.xu@samsung.com
 *
 */

public class FileUtilApi {
	public static void scan(OnFileFoundListner l,String[] bfilter,String[] tfilter,boolean isParseXml){
		FileUtil.scan(l, bfilter, tfilter,isParseXml);
	}
	
	/**
	 * 
	 * @param root       Root directory
	 * @param l          {@link OnFileFoundListner}
	 * @param suffixs    Scan file suffix
	 * @param isDone     Must set as true!!!
	 */
	
	public static void scan(File root,OnFileFoundListner l,String[] suffixs,boolean isDone){
		FileUtil.scan(root, l, suffixs, isDone);
	}
}
