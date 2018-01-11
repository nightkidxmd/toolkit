package com.dadatoolkit.whitelist;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.xmlpull.v1.XmlPullParser;

import android.annotation.SuppressLint;
import android.util.Log;

import com.dadatoolkit.android.xml.XmlGenerator;
import com.dadatoolkit.android.xml.XmlParser;
import com.dadatoolkit.android.xml.XmlGenerator.OnGeneratorListener;
import com.dadatoolkit.android.xml.XmlParser.OnParserListener;
import com.dadatoolkit.java.ReflectUtil;
import com.dadatoolkit.java.io.FileUtil;
/**
 * 
 * @author maoda.xu@samsung.com
 *
 */
@SuppressLint("SdCardPath")
public class WhiteListUtil implements OnParserListener,FileUtil.OnFileFoundListner {
	private XmlParser mParser;
	private static final String WHITE_LIST_PATH = "/sdcard/StringChecker/whitelist/";
	private static final String[] SUFFIX = {"xml"};

	private HashMap<String,ArrayList<WhiteList>> mWhiteListMap = new HashMap<String,ArrayList<WhiteList>>();
	private String mType = null;
	private String mTagName;
    private OnInitialLisener mInitialListener;
    private String mCurrentPkg = "";
    private Object mLock;
    
    private HashMap<String,ArrayList<String>> mResultOnProgress = new HashMap<String,ArrayList<String>>();
	
	private static final Class<?>[] mWhiteClass= new Class[WhiteList.TYPE_SIZE];
	
	public interface OnInitialLisener{
		public void onInitialDone();
	}
	

	public WhiteListUtil() {
		mParser = new XmlParser();
		mParser.setOnParserListener(this);
		mWhiteClass[WhiteList.TYPE_REGEX]=RegexWhiteList.class;
		mWhiteClass[WhiteList.TYPE_CONTENT]=FullMatchWhiteList.class;
		mWhiteClass[WhiteList.TYPE_CONTAIN]=ContainWhiteList.class;
		mWhiteClass[WhiteList.TYPE_STRING_NAME]=StringNameWhiteList.class;
	}

	public void load() {
		loadResult();
		FileUtil.scan(new File(WHITE_LIST_PATH), this, SUFFIX, true);
	}
	
	
	
	
	@Deprecated
	@SuppressLint("DefaultLocale")
	private void loadResult(){
		File resultFile = new File("/sdcard/result.txt");
		if(resultFile.exists()){
			ArrayList<String> rawData = FileUtil.read("/sdcard/result.txt");
			for(String s:rawData){
				String[] temp = s.split("::::");
				String pkg = temp[0];
				String id = temp[temp.length == 4?2:1];
				if(mResultOnProgress.containsKey(pkg)){
					mResultOnProgress.get(pkg).add(id.trim().toLowerCase());
				}else{
					ArrayList<String> data = new ArrayList<String>();
					data.add(id);
					mResultOnProgress.put(pkg, data);
				}
			}
		}
	}
	
	

	@SuppressLint("DefaultLocale")
	public boolean check(String pkg,String stringName,String content) {
		ArrayList<WhiteList> whitelist = mWhiteListMap.get(pkg);
		
		if(stringName.trim().equalsIgnoreCase("stms_version") || stringName.trim().equalsIgnoreCase("hello_world")){
			return true;
		}
		
		if(whitelist != null)
			for(WhiteList w:whitelist){
				if(w.check(stringName,content)){
					return true;
				}
		}
		if(mResultOnProgress.containsKey(pkg) && mResultOnProgress.get(pkg).contains(stringName.trim().toLowerCase())){
			return true;
		}
		return false;
	}
	
	
	private WhiteList newWhiteList(String stype,String content){
		int type = getType(stype);
		
		if(type == -1)
			return null;
		WhiteList w = (WhiteList) ReflectUtil.getInstance(ReflectUtil.getConstructor(mWhiteClass[type], String.class,String.class
				), stype,content);
		return w;
	}
	

	private InputStream getFile(String path) {
		File whiteListFile = new File(path);
		FileInputStream fi = null;
		if (!whiteListFile.exists()) {
			return fi;
		}
		
		try {
			fi = new FileInputStream(whiteListFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return fi;

	}

	public void add(String pkg,String stype,String content) {
		WhiteList w = newWhiteList(stype,content);
		if(w!=null){
			if(mWhiteListMap.containsKey(pkg)){
				  mWhiteListMap.get(pkg).add(w);
			}else{
				ArrayList<WhiteList> list = new ArrayList<WhiteList>();
				list.add(w);
				mWhiteListMap.put(pkg, list);
			}
		}
	}
	


	@Override
	public void onParse(int eventType, XmlPullParser parser) {
		// TODO Auto-generated method stub
		switch (eventType) {
		case XmlPullParser.START_TAG:
			mTagName = parser.getName();
			if (mTagName.equals("item")) {
				for (int i = 0; i < parser.getAttributeCount(); i++) {
					if (parser.getAttributeName(i).equals("type")) {
						mType = parser.getAttributeValue(i);		
					}
				}
			}
			break;
		case XmlPullParser.END_TAG:
			mType=null;
			break;
		case XmlPullParser.TEXT:
			
			String text = parser.getText();
			if(mType != null){
				add(mCurrentPkg,mType,text);
			}
			
			break;
		default:
			break;
		}
	}

	@Override
	public void onParseDone(int result) {
		// TODO Auto-generated method stub
		synchronized(mLock){
			mLock.notify();
		}
		



	}
	
	public void setOnInitialLisener(OnInitialLisener l){
		mInitialListener = l;
	}
	
	
	
	private int getType(String stype){
		int type = -1;
		if(stype.equals(WhiteList.TYPE_STRING_REGEX)){
			type = WhiteList.TYPE_REGEX;
		}else if(stype.equals(WhiteList.TYPE_STRING_CONTENT)){
			type = WhiteList.TYPE_CONTENT;
		}else if(stype.equals(WhiteList.TYPE_STRING_CONTAIN)){
			type = WhiteList.TYPE_CONTAIN;
		}else if(stype.equals(WhiteList.TYPE_STRING_STRING_NAME)){
			type = WhiteList.TYPE_STRING_NAME;
		}
		
		return type;
	}
	
	public void save(OnGeneratorListener l){
		new XmlGenerator(l).generate("/sdcard/StringChecker/whitelist/", mWhiteListMap);
	}

	@Override
	public void onFound(String path, Object lock) {
		// TODO Auto-generated method stub
		mLock = lock;
		String[] temp = path.split("/");
	    mCurrentPkg = temp[temp.length-2];
	    Log.e("DADA","mCurrentPkg:"+mCurrentPkg);
		InputStream fi = getFile(path);
		if (fi != null){
			synchronized(mLock){
				mParser.start(fi);
				try {
					mLock.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void onFoundDone() {
		// TODO Auto-generated method stub
		File file = new File("/sdcard/appfw_stringcheckerlist.xls");
		if(file.exists()){
			Workbook book = null ;
			try {
				book = Workbook.getWorkbook(file);
				Sheet sheet = book.getSheet("frameworks");
				Cell[] cell_2=sheet.getColumn(2);
				Cell[] cell_5=sheet.getColumn(5);
				for(int i=0;i<cell_5.length;i++){
					if(cell_5[i].getContents().trim().equalsIgnoreCase("N")){
						add("framework",WhiteList.TYPE_STRING_STRING_NAME,cell_2[i].getContents());
					}
				}
			} catch (BiffException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
		
		}
		
		if(mInitialListener != null){
			mInitialListener.onInitialDone();
		}

	}

	@Override
	public void onFound(String path, InputStream is) {
		// TODO Auto-generated method stub
		if(is != null){
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
