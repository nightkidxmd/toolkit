package com.dadatoolkit.whitelist;
/**
 * 
 * @author maoda.xu@samsung.com
 *
 */
public class StringNameWhiteList extends WhiteList{


	public StringNameWhiteList(String type, String content) {
		super(type, content);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean check(String stringName, String content) {
		// TODO Auto-generated method stub
		return stringName.equals(whitecontent);
	}

}
