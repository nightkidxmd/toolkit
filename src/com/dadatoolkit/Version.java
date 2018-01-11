package com.dadatoolkit;

public class Version {
    public static final String DATE = "2014-01-23";
     
     
     
 	private static final int VERSION_MAJOR = 1;
 	private static final int VERSION_MINOR = 0;
 	private static final int VERSION_MICRO = 0;
 	
 	public static final String VRESION_STRING=VERSION_MAJOR+"."+VERSION_MINOR+"."+VERSION_MICRO;
 	public static boolean versionCheck(int major,int minor,int micro){
 		return (major*1000+minor*100+micro) >= VERSION_MAJOR*1000+VERSION_MINOR*100+VERSION_MICRO;
 	}
}
