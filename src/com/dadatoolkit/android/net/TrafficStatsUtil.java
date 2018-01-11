package com.dadatoolkit.android.net;

import android.net.TrafficStats;

public class TrafficStatsUtil extends TrafficStats {
	
	public static long getUidTxBytes(int uid){
		long bytes = TrafficStats.getUidTxBytes(uid);
		return bytes < 0 ?0:bytes;
	}
	
	
	
	public static long getUidRxBytes(int uid){
		long bytes = TrafficStats.getUidRxBytes(uid);
		return bytes < 0 ?0:bytes;
	}
	
}
