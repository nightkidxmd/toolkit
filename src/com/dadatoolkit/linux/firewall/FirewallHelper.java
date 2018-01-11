package com.dadatoolkit.linux.firewall;

import com.dadatoolkit.android.os.SystemProperties;
import com.dadatoolkit.android.su.NotRootException;
import com.dadatoolkit.android.su.SuHelper;
import android.content.Context;

/**
 *
 * <b>Notice:</b>Need root permission
 * @author maoda.xu@samsung.com
 * @hide
 */

public class FirewallHelper extends SuHelper {

	private static final String SYSTEM_PROPERTY= "dada.firewall";
	private static FirewallHelper mInstance;
	private String CMD="iptables ";
	private final String DADA_R = "dada_r";//Reject
	private final String DADA_F = "dada_f";//Firewall
//	private final String DADA_O = "dada_o";//Output
	

	private FirewallHelper(Context context) throws NotRootException{
		super(context);
	}

	public static FirewallHelper getInstance(Context context) throws NotRootException{

		if(mInstance == null){
			mInstance = new FirewallHelper(context);
		}
		


		return mInstance;

	}
	
	/**
	 * 
	 * @param force true force to build wall, make sure you've torn down the last one.
	 */
	
	public void build(boolean force){
		boolean isInitialed = Boolean.parseBoolean(SystemProperties.get(SYSTEM_PROPERTY, "false"));
		if(!isInitialed || force){
//			su.execute(CMD+"-N "+DADA_O);
			su.execute(CMD+"-N "+DADA_R);
			su.execute(CMD+"-N "+DADA_F);
			su.execute(CMD+"-A OUTPUT -p all -j "+DADA_F);//DADA_O);
			su.execute(CMD+"-A INPUT -p all -j "+DADA_F);
//			su.execute(CMD+"-A "+DADA_O+" -p all -j "+DADA_R);
			su.execute(CMD+"-A "+DADA_R+" -p all -j REJECT");
			su.execute("setprop "+SYSTEM_PROPERTY+" true");
		}
	}


	/**
	 * build wall, if not built.
	 */

	public void build(){
		build(false);
	}

	/**
	 * This way shall not pass for you.
	 * @param uid
	 */

	public void rejectUID(int uid){
		StringBuilder sb = new StringBuilder();
		sb.append(CMD)
		.append("-A ")
		.append(DADA_F+" ")
		.append("-p all ")
		.append("-j "+DADA_R+" ")
		.append("-m owner ")
		.append("--uid-owner ")
		.append(uid);
		su.execute(sb.toString());
	}

	/**
	 * 
	 * @param uid
	 */

	public void allowUID(int uid){
		StringBuilder sb = new StringBuilder();
		sb.append(CMD)
		.append("-D ")
		.append(DADA_F+" ")
		.append("-p all ")
		.append("-j "+DADA_R+" ")
		.append("-m owner ")
		.append("--uid-owner ")
		.append(uid);
		su.execute(sb.toString());
	}

	/**
	 * Clear all policy not only dada policy
	 */
	public void clearAllPolicy(){
		StringBuilder sb = new StringBuilder();
		sb.append(CMD)
		.append("-F");
		su.execute(sb.toString());

	}

	/**
	 * Just delete the list on the wall, build if not built.
	 */

	public void rebuild(){
		su.execute(CMD+"-F "+DADA_F);
		build();
	}
	
	/**
	 * Tear down the wall
	 */
	
	public void tearDown(){
//		su.execute(CMD+"-F "+DADA_O);
		su.execute(CMD+"-F "+DADA_R);
		su.execute(CMD+"-F "+DADA_F);
		su.execute(CMD+"-D OUTPUT -p all -j "+DADA_F);//DADA_O);
		su.execute(CMD+"-D INPUT -p all -j "+DADA_F);//DADA_O);
//		su.execute(CMD+"-X "+DADA_O);
		su.execute(CMD+"-X "+DADA_R);
		su.execute(CMD+"-X "+DADA_F);
	}
	


}
