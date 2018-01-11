package com.dadatoolkit.android.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
/**
 * 
 * @author maoda.xu@samsung.com
 *
 */

public class XmlParser {
	public static final int RESULET_NO_ERROR = 0;
	public static final int RESULET_IO_ERROR = -1;
	public static final int RESULT_XMLPULLPARSE_ERROR = -2;
	
	private List<OnParserListener> mListeners = new ArrayList<OnParserListener>();
	private File mXmlFile;
	public interface OnParserListener{
		
		/**   
		 *          
		 * @param eventType XmlPullParser.START_TAG <br>
		 *                  XmlPullParser.END_TAG   <br>
		 *                  XmlPullParser.TEXT
		 * @param parser
		 */
		public void onParse(int eventType,XmlPullParser parser);
		
		/**
		 * 
		 * @param result  RESULET_NO_ERROR = 0 <br>
		 *                RESULET_IO_ERROR = -1 <br>
		 *                RESULT_XMLPULLPARSE_ERROR = -2
		 */
		public void onParseDone(int result);
	}
	
	/**
	 * 
	 * @param fileName   Absolute path name
	 * @throws Exception If file not exist or can't be read
	 */
	
	public XmlParser(String fileName) throws Exception{
		mXmlFile = new File(fileName);
		if(!mXmlFile.exists() || !mXmlFile.canRead()){
				throw new Exception("PLZ make sure there exists such file and can be read!");
		}
	}
	
	public XmlParser(){
	}
	
	/**
	 * 
	 * @param listener add to OnParserListener array list
	 * @return ret Return true if operated
	 */
	
	public boolean setOnParserListener(OnParserListener listener){
		boolean ret = false;
		if(!mListeners.contains(listener))
			ret = mListeners.add(listener);	
		return ret;
	}
	/**
	 * 
	 * @param listener
	 * @return ret   Return true if operated
	 */
	
	public boolean removeOnParserListener(OnParserListener listener){
		return  mListeners.remove(listener);
	}
	
	/**
	 * Start a thread to parse xml file<br>
	 * <b>Note:</b>
	 * If you need to load all the content before operate,<br>
	 * Please wait when parser and notify when parser done
	 * @param fi InputStream
	 */
	
	public void start(InputStream fi){
		new ParserThread(fi).start();
	}
	
	/**
	 * Start a thread to parse xml file<br>
	 * <b>Note:</b>
	 * If you need to load all the content before operate,<br>
	 * Please wait when parser and notify when parser done
	 * @throws FileNotFoundException
	 */
	
	public void start() throws FileNotFoundException{
		if(mXmlFile != null)
			new ParserThread(new FileInputStream(mXmlFile)).start();
	}
		
	private void parse(InputStream fi){
        XmlPullParserFactory factory;
        XmlPullParser parser = null;
        int ret = RESULET_NO_ERROR;
        int eventType = -1;
        try {
	            factory = XmlPullParserFactory.newInstance();
	            factory.setNamespaceAware(true);
	            parser = factory.newPullParser();
	            parser.setInput(fi, null);
	            eventType = parser.getEventType();
	            while (eventType != XmlPullParser.END_DOCUMENT) {
	            	if(mListeners != null){
	            		for(int i=0;i<mListeners.size();i++){
	            			mListeners.get(i).onParse(eventType, parser);
	            		}
	            	}
	            	
	            	if(eventType == XmlPullParser.END_TAG){
	            		eventType = parser.next();
	            	}
	            		eventType = parser.next();
	            }
               
            } catch (IOException e) {
                    e.printStackTrace();
                    ret = RESULET_IO_ERROR;
                
            } catch (XmlPullParserException e) {
					e.printStackTrace();
					ret = RESULT_XMLPULLPARSE_ERROR;
			} finally {
                if (fi != null) {
                    try {
						fi.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
                    fi = null;
                }
                if(mListeners != null){
	        		for(int i=0;i<mListeners.size();i++){
	        			mListeners.get(i).onParseDone(ret);
	        		}
                }
            }
	}
	
	private class ParserThread extends Thread{
		private InputStream fi;
		public ParserThread(InputStream fi){
			this.fi = fi;
		}
		
		public void run(){
			parse(fi);
		}
		
	}
}
