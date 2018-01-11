package com.dadatoolkit.java.string;

import java.util.HashSet;
/**
 * 
 * @author maoda.xu@samsung.com
 *
 */
public class Filter {
	private HashSet<String> mFilter = new HashSet<String>();
	
	
	/**
	 * 
	 * @param filter  format: filter1;filter2;...;
	 */
	public Filter(String filter){
		String[] filters = filter.split(";");
		if(filters != null)
			for(String f:filters){
				mFilter.add(f);
			}
	}
	
	
	
	/**
	 * 
	 * @param input strings needed to check
	 * @return true if filters contains,otherwise false.
	 */
	public boolean isFilterred(String input){
	
		if(mFilter.size() == 0){
			return true;
		}
		
		
		if(mFilter.contains(input)){
			return true;
		}
		
		for(String f:mFilter){
			if(input.contains(f)){
				return true;
			}
		}
				
		return false;
	}
	

}
