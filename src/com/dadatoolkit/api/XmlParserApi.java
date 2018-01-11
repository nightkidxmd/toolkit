package com.dadatoolkit.api;

import java.io.InputStream;

import com.dadatoolkit.android.xml.XmlParser;
import com.dadatoolkit.android.xml.XmlParser.OnParserListener;
/**
 * 
 * @author maoda.xu@samsung.com
 *
 */
public class XmlParserApi {
	private XmlParser parser;
	public XmlParserApi(){
		parser = new XmlParser();
	}
	
	
	public boolean setOnParserListener(OnParserListener listener){
		return parser.setOnParserListener(listener);
	}
	
	public void start(InputStream fi){
		parser.start(fi);
	}

}
