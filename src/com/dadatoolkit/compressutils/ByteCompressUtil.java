package com.dadatoolkit.compressutils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;

public class ByteCompressUtil {

	public static byte[] gZip(byte[] data){
		byte[] zipped = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		GzipCompressorOutputStream gcos = null;
		try {
			gcos = new GzipCompressorOutputStream(bos);
			gcos.write(data);
			gcos.finish();
			zipped = bos.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(gcos!= null){
				try {
					gcos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			try {
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
		return zipped;
	}
	
	public static byte[] unGzip(byte[] data){
		byte[] unzipped = null;
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		GzipCompressorInputStream gcis = null;
		ByteArrayOutputStream bos = null;
		try {
			gcis = new GzipCompressorInputStream(bis);
			byte[] buffer = new byte[1024];
			int num = -1;
			bos = new ByteArrayOutputStream();
			while((num = gcis.read(buffer, 0, buffer.length)) != -1){
				bos.write(buffer,0,num);
			}
			unzipped = bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(gcis != null){
				try {
					gcis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if(bos != null){
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
		
		return unzipped;
	}

	
	
	public static byte[] bZip2(byte[] data){
		byte[] zipped = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		BZip2CompressorOutputStream gcos = null;
		try {
			gcos = new BZip2CompressorOutputStream(bos);
			gcos.write(data);
			gcos.finish();
			zipped = bos.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(gcos!= null){
				try {
					gcos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			try {
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
		return zipped;
	}
	
	public static byte[] unBZip2(byte[] data){
		byte[] unzipped = null;
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		BZip2CompressorInputStream gcis = null;
		ByteArrayOutputStream bos = null;
		try {
			gcis = new BZip2CompressorInputStream(bis);
			byte[] buffer = new byte[1024];
			int num = -1;
			bos = new ByteArrayOutputStream();
			while((num = gcis.read(buffer, 0, buffer.length)) != -1){
				bos.write(buffer,0,num);
			}
			unzipped = bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(gcis != null){
				try {
					gcis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if(bos != null){
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
		
		return unzipped;
	}
}
