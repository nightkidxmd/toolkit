package com.dadatoolkit.android.xml;

public class XmlElement {
	
	
	public static final int TYPE_START_TAG = 0;
	public static final int TYPE_ATTRIBUTE = 1;
	public static final int TYPE_TEXT = 2;
	public static final int TYPE_END_TAG = 3;
	public static final int TYPE_END_DOCUMENT = 4;
	
	public final int type;
	public final String[] data;
	
    public XmlElement(int type , String... args) throws IllegalArgumentException{
    	this.type = type;
    	this.data = args;
    	if(!valid()){
    		throw new IllegalArgumentException("type:"+type+" data.length:"+data.length);
    	}
	}
    
    private boolean valid(){
    	switch(type){
		case XmlElement.TYPE_START_TAG:
		case XmlElement.TYPE_TEXT:
		case XmlElement.TYPE_END_TAG:
			if(data.length < 1){
				return false;
			}
			break;
		case XmlElement.TYPE_ATTRIBUTE:
			if(data.length < 2){
				return false;
			}
			break;
		default:
			break;
    	}
    	
    	return true;
    	
    }

}
