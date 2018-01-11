package com.dadatoolkit.android.os;

import com.dadatoolkit.java.ReflectUtil;
/**
 * 
 * @author maoda.xu@samsung.com
 *
 */
public class SystemProperties {
	private static final String SYSTEMPROERTIES="android.os.SystemProperties";
	/**
	 * 
	 * @param propName   system property name
	 * @param defVal     default value if system property not exist
	 * @return value     system property value
	 */
    public static String get(String propName,String defVal){
    	return (String) ReflectUtil.inVoke(null, ReflectUtil.getMethod(SYSTEMPROERTIES, "get", String.class,String.class), propName,defVal);
    }
    /**
     * <b>Note:</b> <br>
     * 1.Only root process can set property.Otherwise this is useless for non-root process.<br>
     * 2.System property begins with ro.* can not be set twice.
     * @param propName system property name
     * @param val      system property value to set
     */   
    public static void set(String propName,String val){
    	ReflectUtil.inVoke(null, ReflectUtil.getMethod(SYSTEMPROERTIES, "set", String.class,String.class), propName,val);
    }

}
