package com.dadatoolkit.java.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.HashSet;

import com.dadatoolkit.java.string.StringUtil;

import android.os.Environment;
import android.util.Log;

public class FileUtil {
	public static void write(String filepath, byte[] buffer) {
		boolean success = false;
		FileOutputStream fos = null;
		File tempF = null;
		try {
			tempF = new File(filepath+".temp");
			if(tempF.createNewFile()){
				fos = new FileOutputStream(tempF);
				fos.write(buffer);
				fos.flush();
				success = true;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		File f = new File(filepath);
		if (success) {
			f.delete();
			tempF.renameTo(f);
		}

	}

	public static ArrayList<String> read(String filepath) {
		ArrayList<String> result = null;
		File f = getFile(filepath);
		BufferedReader reader = null;
//		CharBuffer buffer = CharBuffer.allocate(1024);
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(f),"gbk"));
			
			String line = reader.readLine();
			result = new ArrayList<String>();
			while (line != null) {
				//Log.i("DADA",line);
				result.add(line);
				line = reader.readLine();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return result;
	}

	private static File getFile(String filepath) {

		File file = null;

		if (isExistAndCanRead(filepath)
				&& isExistAndCanRead(filepath + ".temp")
				|| (isExistAndCanRead(filepath) && !isExistAndCanRead(filepath
						+ ".temp"))) {
			file = new File(filepath);
		} else if (!isExistAndCanRead(filepath)
				&& isExistAndCanRead(filepath + ".temp")) {
			file = new File(filepath + ".temp");
		} else if (!isExistAndCanRead(filepath)
				&& !isExistAndCanRead(filepath + ".temp")) {
			file = new File(filepath);
			file.mkdirs();
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return file;

	}

	private static boolean isExistAndCanRead(String filepath) {
		File f = new File(filepath);
		return f.exists() && f.canRead();
	}

	private static boolean isExistAndCanWrite(String filepath) {
		File f = new File(filepath);
		return f.exists() && f.canRead();
	}

	/**
	 * This is for StringChecker Only.
	 * @param l
	 * @param bfilters    black list
	 * @param tfilters    target list
	 * @param isParseXml
	 */

	public static void scan(OnFileFoundListner l,String[] bfilters,String[] tfilters,boolean isParseXml){
		File root  = new File(Environment.getExternalStorageDirectory()+"/StringChecker/");
		Object lock = new Object();
		if(root == null || !root.exists()){
			l.onFoundDone();
			return;
		}else{
			HashSet<String> _bfilters = new HashSet<String>();
			HashSet<String> _tfilters = null;
			if(bfilters != null){
				for(String f:bfilters){
					_bfilters.add(f);
				}
			}


			if(tfilters != null){
				_tfilters = new HashSet<String>();
				for(String f:tfilters){
					_tfilters.add(f);
				}
			}

			File[]  files = root.listFiles();
			for(File f:files){
				File[] rf = f.listFiles();
				if(rf != null && rf.length > 0){
					String path = f.getName();
					if(_bfilters.contains(path)){
						continue;
					}

					if(_tfilters != null && !_tfilters.contains(path)){
						continue;
					}

					if(isParseXml){
						try {
							l.onFound(path, new FileInputStream(rf[0]));
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else{
						l.onFound(path, lock);
					}
				}
			}


		}

		l.onFoundDone();
		return;
	}

	/**
	 * Recurse into the directories under root dir.
	 * @param root       Root dir
	 * @param l          {@link OnFileFoundListner}
	 * @param suffixs    scan file suffixs, null for all type.
	 * @param isDone     Set as true indeed.Then will call {@link OnFileFoundListner#onFoundDone()} after done.
	 */


	public static void scan(File root,OnFileFoundListner l,String[] suffixs,boolean isDone){
		Object lock = new Object();
		HashSet<String> _filters = new HashSet<String>();
		if(suffixs != null){
			for(String s:suffixs){
				_filters.add(s.toLowerCase());
			}

		}
		if(root == null || !root.exists()){
			l.onFoundDone();
			return;
		}else{
			File[]  files = root.listFiles();
			if(files == null){
				l.onFoundDone();
				return;
			}
			for(File f:files){
				if(f.isDirectory()){
					scan(f,l,suffixs,false);
				}else if(f.isFile()){
					String path = f.getAbsolutePath();
					String[] suffixT = f.getName().split("\\.");
					if(_filters.contains(suffixT[suffixT.length-1].toLowerCase()))
						l.onFound(path,lock);
				}

			}


		}
		if(isDone)
			l.onFoundDone();
		return;
	}


	public static String getCurrentPath(){
		return new File("").getAbsolutePath();
	}



	public interface OnFileFoundListner{
		public void onFound(String path,InputStream is);
		public void onFound(String path,Object lock);
		public void onFoundDone();
	}



}
