package com.dadatoolkit.java.secure;

import java.util.ArrayList;
import java.util.UUID;

public class UUIDGenerator {
	
	public static UUID generate(){
		return UUID.randomUUID();
		
	}
	
	
	public static ArrayList<UUID> generate(int num){
		if(num <= 0 )
			return null;
		
		ArrayList<UUID>  uuids = new ArrayList<UUID>();
		
		for(int i=0;i<num;i++){
			uuids.add(UUID.randomUUID());
		}

		return uuids;

	}
	
	
	public static UUID fromString(String uuid) {
		
		return UUID.fromString(uuid);
	}
	
	
	public static UUID nameUUIDFromBytes(byte[] name){
		return UUID.nameUUIDFromBytes(name);
	}

}
