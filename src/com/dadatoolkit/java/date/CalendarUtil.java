package com.dadatoolkit.java.date;

import android.annotation.SuppressLint;


import java.text.SimpleDateFormat;
import java.util.Date;



@SuppressLint("DefaultLocale")
public class CalendarUtil {
	private static final String DEFAULT_FORMAT="yyyy-MM-dd-HH-mm-ss";
	
	/**
	 * 
	 * @param template 
	 * <b>Symbol Meaning Presentation Example</b><br>
			D day in year (Number) 189<br>
			E day of week (Text) Tuesday 
			F day of week in month (Number) 2 (2nd Wed in July)<br>
			G era designator (Text) AD<br>
			H hour in day (0-23) (Number) 0<br>
			K hour in am/pm (0-11) (Number) 0<br>
			L stand-alone month (Text/Number) July / 07<br>
			M month in year (Text/Number) July / 07<br>
		    S fractional seconds (Number) 978<br>
			W week in month (Number) 2<br>
			Z time zone (RFC 822) (Timezone) -0800<br>
			a am/pm marker (Text) PM<br>
			c stand-alone day of week (Text/Number) Tuesday / 2<br>
			d day in month (Number) 10<br>
			h hour in am/pm (1-12) (Number) 12<br>
			k hour in day (1-24) (Number) 24<br>
			m minute in hour (Number) 30<br>
			s second in minute (Number) 55<br>
			w week in year (Number) 27<br>
			y year (Number) 2010<br>
			z time zone (Timezone) Pacific Standard Time<br>
			' escape for text (Delimiter) 'Date='<br>
			'' single quote (Literal) 'o''clock'<br>
	 * @return time_stamp formated date string
	 */

	 public static String getTimeStamp(String template){
		 SimpleDateFormat s= (SimpleDateFormat) SimpleDateFormat.getInstance();
		 s.applyPattern(template);
		 Date date = new Date(System.currentTimeMillis());
		 String time_stamp =  s.format(date);
		 return time_stamp;
	 }
	 
	 /**
	  * Use defualt format:"yyyy-MM-dd-HH-mm-ss"
	  * @return time_stamp formated date string
	  */
	 public static String getTimeStamp(){
		 SimpleDateFormat s= (SimpleDateFormat) SimpleDateFormat.getInstance();
		 s.applyPattern(DEFAULT_FORMAT);
		 Date date = new Date(System.currentTimeMillis());
		 String time_stamp =  s.format(date);
		 return time_stamp;
	 }
	 
	 /**
	  * 
	  * @param time   millis time  long
	  * @return Date.toString()  "" if null
	  */
	 
	 
	 public static String timeMillis2String(long time){
		 Date date = new Date(time);
		 return date == null?"":date.toString();
	 }
	 
	 /**
	  * 
	  * @param time     millis time string
	  * @param radix    the radix to use when parsing
	  * @return Date.toString()  "" if null
	  */
	 
	 public static String timeMillis2String(String time,int radix ){
		 long longtime = Long.parseLong(time, radix);
		 Date date = new Date(longtime);
		 return date == null?"":date.toString();
	 }	 
}
