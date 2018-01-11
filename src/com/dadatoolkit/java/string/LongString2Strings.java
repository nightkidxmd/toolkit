package com.dadatoolkit.java.string;
/**
 * 
 * @author maoda.xu@samsung.com
 *
 */
public class LongString2Strings {
	private String[] mStrings;
	private int mIndex = 0;
	
	/**
	 * 
	 * @param longString
	 * @param split
	 */
	public LongString2Strings(String longString,String split){
		mStrings = longString.split(split);
	}
	
	
	public boolean hasNext(){
		return mIndex < mStrings.length;
	}
	
	
	public void setStringAndSplit(String longString,String split){
		mStrings = longString.split(split);
	}
	
	public String next(){
		if(mStrings == null){
			return null;
		}
		while(mStrings[mIndex++].trim().equals(""));
		int index = mIndex-1;
		if(index >= mStrings.length){
			return null;
		}
		return mStrings[index];
	}
	
	public void reset(){
		mIndex = 0;
	}
	
	public void clear(){
		mStrings = null;
	}

}
