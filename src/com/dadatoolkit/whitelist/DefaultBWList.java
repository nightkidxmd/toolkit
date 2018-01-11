package com.dadatoolkit.whitelist;

import android.annotation.SuppressLint;
import java.util.HashSet;
import java.util.Set;
/**
 * 
 * @author maoda.xu@samsung.com
 *
 */
public class DefaultBWList {
	private static DefaultBWList mInstance;
	
	private static final String[] mWhiteListArray={
		"TB",
		"GB",
		"MB",
		"KB",
		"B",
		"bytes",
		
		"AM",
		"PM",
		
		
		"WLAN",
		"NFC",
		"S Beam",
		
		
		"GPRS",
		"1x",
		"HSPA",
		"LTE",
		"CDMA",
		"3G",
		"3.5G",
		"4G",
		
		
		"WEP",
		"WPA",
		"WPA2",
		"WPA/WPA2",
		"WPA PSK",
		"WPA2 PSK",
		"WPA/WPA2 PSK",
		"802.1x",
		"802.1x EAP",
		"FT",
		"FT/WPA2",
		"FT PSK",
		"FT/WPA2 PSK",
		"CCKM",
		"WAPI PSK",
		"DNS 1",
		"DNS 2",
		"2.4 GHz",
		"5 GHz",
		"DHCP",
		"WAG DNS",
		"WAG IP",
		
		
		"mV",
		"бу C",
		"буC",
		"бу F",
		"буF",
		"AC",
		"(AC)",
		"USB",
		"(USB)",
		"IMEI:",
		"IMEI",
		"TTS",
		
		
	};
	
	
	private static final String[] mBlackListArray={
		"2222",
		"a0a0",
	};
	
	private Set<String> mWhiteList = new HashSet<String>();
	private Set<String> mBlackList = new HashSet<String>();
	
	
	private DefaultBWList(){
		updateList(mWhiteListArray,mWhiteList);
		updateList(mBlackListArray,mBlackList);
	}
	
	public static DefaultBWList getInstance(){
		if(mInstance == null){
			mInstance = new DefaultBWList();
		}
		return mInstance;
		
	}
	
	
	@SuppressLint("DefaultLocale")
	private void updateList(String[] array,Set<String> set){
		for(String s:array){
			s = s.toUpperCase();
			if(!set.contains(s))
				set.add(s);
		}
	}
	
	public boolean isBlack(String content){
		return mBlackList.contains(content);
	}
	
	
	public boolean isWhite(String content){
		return mWhiteList.contains(content);
	}

}
