package com.dadatoolkit.whitelist;
/**
 * Do not mix this class!!!
 * @author maoda.xu@samsung.com
 *
 */

abstract public class WhiteList {
	
	public static final int TYPE_REGEX=0;
	public static final int TYPE_CONTENT=1;
	public static final int TYPE_CONTAIN=2;
	public static final int TYPE_STRING_NAME=3;
	public static final int TYPE_SIZE=4;
	
	
	
	public static final String TYPE_STRING_REGEX="regex";
	public static final String TYPE_STRING_CONTENT="content";
	public static final String TYPE_STRING_CONTAIN="contain";
	public static final String TYPE_STRING_STRING_NAME="stringname";
	

	protected String whitecontent;
	private String type;
	
	
	public WhiteList(String type,String content){
		whitecontent = content;
		this.type = type;
	}
	
	public String getType(){
		return type;
	}
	
	public String getContent(){
		return whitecontent;
	}
	
	public String toString(){
		return "type="+type+" whitecontent="+whitecontent;
	}
	
	abstract public boolean check(String stringName,String content);
	//abstract public boolean compareTo(String stringName,String content);
}
