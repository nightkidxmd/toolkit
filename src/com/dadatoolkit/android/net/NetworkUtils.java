package com.dadatoolkit.android.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class NetworkUtils {
	
	
	public static int inetAddressToInt(Inet4Address inetAddr){
		byte[] addr = inetAddr.getAddress();
		return ((addr[3] & 0xff) << 24) | ((addr[2] & 0xff) << 16) |
				((addr[1] & 0xff) << 8) | (addr[0] & 0xff);
		
	}
	
	
	public static InetAddress intToInetAddress(int hostAddress){
		byte[] addr = {
				(byte)(0xff  & hostAddress),
				(byte)((0xff & hostAddress) >> 8),
				(byte)((0xff & hostAddress) >> 16),
				(byte)((0xff & hostAddress  >> 24)),
				
		};
		try {
			return InetAddress.getByAddress(addr);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static ArrayList<NetStat> netStat(){
		
		ArrayList<NetStat> list = new ArrayList<NetStat>();
		java.lang.Process proc = null;
		BufferedReader reader = null;
		try {
			proc = Runtime.getRuntime().exec("netstat");
			reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            while((line = reader.readLine())!=null){
            	if(line.startsWith("Proto")){
            		continue;
            	}
            	
            	list.add(new NetStat(line));
            
            }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					reader = null;
				}
				
			}
		}
		
		
		return list;
		
	}

}
