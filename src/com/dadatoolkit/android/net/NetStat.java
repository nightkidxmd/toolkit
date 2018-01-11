package com.dadatoolkit.android.net;

import com.dadatoolkit.java.string.LongString2Strings;

public class NetStat {
	public final String protocal;
	public final String recv_Q;
	public final String send_Q;
	public final String localAddress;
	public final String foreignAddress;
	public final String state;
	
	
	public NetStat(String line){
		LongString2Strings util = new LongString2Strings(line," ");
		protocal = util.next();
		recv_Q = util.next();
		send_Q = util.next();
		localAddress = util.next();
		foreignAddress = util.next();
		state = util.next();
	}
	
	@Override
	public String toString (){
		return "NetStat{"+"protocal="+protocal+" recv_Q="+recv_Q+" send_Q="+send_Q+
				" localAddress="+localAddress+" foreignAddress="+foreignAddress+" state="+state+"}";
	}

	

}
