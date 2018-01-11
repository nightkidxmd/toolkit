package com.dadatoolkit.java.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CMDUtil {


	private static Object mCMDlock = new Object();

	/**
	 * Execute only
	 * @param cmd
	 * @param isSync  need to wait or not
	 */

	public static void execute(String cmd,boolean isSync){
		synchronized(mCMDlock){
			java.lang.Process proc = null;
			BufferedReader inputReader = null;
			BufferedReader errorReader = null;

			try {
				cmd+="\r\n";
				proc = Runtime.getRuntime().exec(cmd);
				proc.getOutputStream().close();
				errorReader = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
				while(errorReader.readLine() != null);
				inputReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				while(inputReader.readLine() != null);
				proc.getInputStream().read();
				if(isSync)
					proc.waitFor();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.println(e.getMessage());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				System.err.println(e.getMessage());
			}finally{
				if(inputReader != null){
					try {

						inputReader.close();
						inputReader = null;

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					}
				}
				if(errorReader != null){
					try {
						errorReader.close();
						errorReader= null;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}


				if(proc != null){
					proc.destroy();
					proc = null;
				}
				Runtime.getRuntime().freeMemory();
			}
		}

	}

	/**
	 * Execute and get result
	 * @param cmd
	 * @param l {@link OnResultListener}
	 */


	synchronized public  static void executeAndGetResult(String cmd,OnResultListener l,boolean isGetSingleLine,boolean needToCleanErrorStream){
		synchronized(mCMDlock){
			BufferedReader inputReader = null;
			BufferedReader errorReader = null;
			java.lang.Process proc = null;
			try { 
				cmd+="\r\n";
				proc = Runtime.getRuntime().exec(cmd);
				proc.getOutputStream().close();
				if(needToCleanErrorStream){
					errorReader = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
					while(errorReader.ready() && errorReader.readLine() != null);
				}
				inputReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				StringBuilder result = new StringBuilder();
				String line = inputReader.readLine();
				if(isGetSingleLine){
					result.append(line);
					while(inputReader.readLine() != null);
				}else{
					while(line != null){
						result.append(line).append("\n");
						line = inputReader.readLine();
					}
				}
				
				if(l!=null){
					l.onDone(result.toString());
				}
				proc.waitFor();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				try {
					if(inputReader != null){
						inputReader.close();
						inputReader = null;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if(errorReader != null){
					try {
						errorReader.close();
						errorReader = null;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				if(proc != null){
					proc.destroy();
					proc = null;
				}
				Runtime.getRuntime().freeMemory();
			}
		}
	}

	/**
	 * 
	 * @param cmd
	 * @param l
	 */

	public  static void executeAndGetResult(String cmd,OnResultListener l){
		executeAndGetResult(cmd,l,true,true);
	}


	/**
	 * 
	 * {@link OnResultListener#onDone(String)}
	 *
	 */
	public interface OnResultListener{
		/**
		 * 
		 * @param result The result got from InputStream
		 *               after executing.
		 */
		public void onDone(String result);

	}
}
