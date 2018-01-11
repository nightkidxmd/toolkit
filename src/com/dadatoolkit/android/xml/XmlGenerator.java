package com.dadatoolkit.android.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.xmlpull.v1.XmlSerializer;

import com.dadatoolkit.whitelist.WhiteList;

import android.util.Xml;

/**
 * 
 * @author maoda.xu@samsung.com
 * 
 */
public class XmlGenerator {

	public OnGeneratorListener mListener;

	// private Object mLock = new Object();
	// private XmlElement e;
	// @SuppressWarnings("unused")
	// private Handler H = new Handler(Looper.getMainLooper()){
	// public void handleMessage(Message msg){
	// mListener.onNext();
	// }
	//
	// };

	public interface OnGeneratorListener {
		public void onGeneratedDone();
		// public void onNext();
	}

	public XmlGenerator(OnGeneratorListener l) {
		mListener = l;
	}

	public boolean generate(String rootPath,
			HashMap<String, ArrayList<WhiteList>> data) {
		boolean ret = false;

		if (data == null) {
			mListener.onGeneratedDone();
			return ret;
		}

		Iterator<Entry<String, ArrayList<WhiteList>>> it = data.entrySet()
				.iterator();
		while (it.hasNext()) {
			Entry<String, ArrayList<WhiteList>> entry = it.next();
			String pkg = entry.getKey();
			ArrayList<WhiteList> list = entry.getValue();
			String path = rootPath + pkg + "/whitelist.xml";
			File f = new File(path);
			XmlSerializer serializer = null;
			FileOutputStream fos = null;
			try {
				if (!f.exists()) {
					f.getParentFile().mkdirs();
					f.createNewFile();
				} else {
					f = new File(rootPath + ".temp");
				}
				fos = new FileOutputStream(f);
				serializer = Xml.newSerializer();
				serializer.setOutput(fos, "UTF-8");
				serializer.startDocument("UTF-8", null);
				serializer
						.setFeature(
								"http://xmlpull.org/v1/doc/features.html#indent-output",
								true);

				serializer.startTag(null, "whitelist");

				for (WhiteList w : list) {
					serializer.startTag(null, "item");
					serializer.attribute(null, "type", w.getType());
					serializer.text(w.getContent());
					serializer.endTag(null, "item");
					serializer.text("\n");
				}
				serializer.endTag(null, "whitelist");

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					if (serializer != null) {
						serializer.endDocument();
						serializer.flush();
					}
					if (fos != null) {
						fos.close();
					}

					if (f.getName().contains(".temp")) {
						File of = new File(path);
						of.delete();
						f.renameTo(of);
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		if (mListener != null) {
			mListener.onGeneratedDone();
		}

		return ret;
	}

	// public void generate(String path){
	// new GeneratorThread(path).start();
	// }
	//
	// public boolean generateInner(String path) {
	// boolean ret = false;
	//
	// File f = new File(path);
	// XmlSerializer serializer = null;
	// FileOutputStream fos = null;
	// try {
	// if (!f.exists()) {
	// f.getParentFile().mkdirs();
	// f.createNewFile();
	// } else {
	// f = new File(path + ".temp");
	// }
	// fos = new FileOutputStream(f);
	// serializer = Xml.newSerializer();
	// serializer.setOutput(fos, "UTF-8");
	// serializer.startDocument("UTF-8", null);
	// serializer.setFeature(
	// "http://xmlpull.org/v1/doc/features.html#indent-output",
	// true);
	//
	// synchronized(mLock){
	// try {
	// mLock.wait();
	// } catch (InterruptedException e1) {
	// // TODO Auto-generated catch block
	// e1.printStackTrace();
	// }
	// }
	//
	// while(e.type != XmlElement.TYPE_END_DOCUMENT){
	// synchronized(mLock){
	// switch (e.type) {
	// case XmlElement.TYPE_START_TAG:
	// serializer.startTag(null, e.data[0]);
	// break;
	// case XmlElement.TYPE_ATTRIBUTE:
	// serializer.attribute(null, e.data[0], e.data[1]);
	// break;
	// case XmlElement.TYPE_TEXT:
	// serializer.text(e.data[0]);
	// break;
	// case XmlElement.TYPE_END_TAG:
	// serializer.endTag(null, e.data[0]);
	// serializer.text("\n");
	// break;
	// default:
	// break;
	// }
	// mLock.notify();
	// try {
	// mLock.wait();
	// } catch (InterruptedException e1) {
	// // TODO Auto-generated catch block
	// e1.printStackTrace();
	// }
	// }
	// }
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } finally {
	// try {
	// if (serializer != null) {
	// serializer.endDocument();
	// serializer.flush();
	// }
	// if (fos != null) {
	// fos.close();
	// }
	//
	// if (f.getName().contains(".temp")) {
	// File of = new File(path);
	// of.delete();
	// f.renameTo(of);
	// }
	//
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	//
	// if (mListener != null) {
	// mListener.onGeneratedDone();
	// }
	//
	// return ret;
	// }
	//
	// public void next(XmlElement... es){
	// for(XmlElement ee:es){
	// e = ee;
	// synchronized(mLock){
	// mLock.notify();
	// try {
	// mLock.wait();
	// } catch (InterruptedException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	//
	//
	//
	// }
	// }
	//
	//
	// private class GeneratorThread extends Thread{
	// private String path;
	//
	// public GeneratorThread(String path){
	// this.path = path;
	//
	// }
	// public void run(){
	// generateInner(path);
	// }
	// }
}
