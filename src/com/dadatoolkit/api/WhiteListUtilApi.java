package com.dadatoolkit.api;

import com.dadatoolkit.android.xml.XmlGenerator.OnGeneratorListener;
import com.dadatoolkit.whitelist.WhiteList;
import com.dadatoolkit.whitelist.WhiteListUtil;
import com.dadatoolkit.whitelist.WhiteListUtil.OnInitialLisener;
/**
 * 
 * @author maoda.xu@samsung.com
 *
 */
public class WhiteListUtilApi {
	public static final int TYPE_REGEX=WhiteList.TYPE_REGEX;
	public static final int TYPE_CONTENT=WhiteList.TYPE_CONTENT;
	public static final int TYPE_CONTAIN=WhiteList.TYPE_CONTAIN;
	public static final int TYPE_STRING_NAME=WhiteList.TYPE_STRING_NAME;
	public static final int TYPE_SIZE=WhiteList.TYPE_SIZE;
	
	
	
	public static final String TYPE_STRING_REGEX=WhiteList.TYPE_STRING_REGEX;
	public static final String TYPE_STRING_CONTENT=WhiteList.TYPE_STRING_CONTENT;
	public static final String TYPE_STRING_CONTAIN=WhiteList.TYPE_STRING_CONTAIN;
	public static final String TYPE_STRING_STRING_NAME=WhiteList.TYPE_STRING_STRING_NAME;
	
	
	
	
	
	private WhiteListUtil wlu;
	public WhiteListUtilApi(){
		wlu = new WhiteListUtil();
	}
	
	
	public void load(){
		wlu.load();
	}
	
	
	public boolean check(String pkg,String stringName,String content) {
		return wlu.check(pkg,stringName, content);
	}
	
	
	public void add(String pkg,String stype,String content) {
        wlu.add(pkg,stype, content);
	}
	
	public void save(OnGeneratorListener l){
		wlu.save(l);
	}
	
	
	public void setOnInitialLisener(OnInitialLisener l){
		wlu.setOnInitialLisener(l);
	}

}
