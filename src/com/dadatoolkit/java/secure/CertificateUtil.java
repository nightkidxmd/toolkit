package com.dadatoolkit.java.secure;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;

import android.content.pm.Signature;

public class CertificateUtil {
	private Signature[] mSignatures;
	
	
	public CertificateUtil(Signature[] signatures){
		mSignatures = signatures;
	}
	
	
	public String getCertificationIssue(){
		String issue = "";
	    X509Certificate c = parse();
	    issue = c.getIssuerDN().toString();
		return issue;
		
	}
	
	public Date getStartDate(){
		X509Certificate c = parse();
		return c==null?null:c.getNotBefore();
		
	}
	
	public Date getEndDate(){
		X509Certificate c = parse();
		return c==null?null:c.getNotAfter();
		
	}
	
	public boolean isValidate(Date date){
		X509Certificate c = parse();
	    return c == null ? false:date.before(c.getNotAfter()) && date.after(c.getNotBefore());
	}
	
	
	public PublicKey getPulicKey(){
		X509Certificate c = parse();
		return c==null?null:c.getPublicKey();	
	}
	
	
	private X509Certificate parse(){
		X509Certificate c = null;
		try {
			if(mSignatures != null && mSignatures.length > 0){
				byte[] cert = mSignatures[0].toByteArray();
				InputStream input = new ByteArrayInputStream(cert);
				CertificateFactory cf = CertificateFactory.getInstance("X509");
				c = (X509Certificate)cf.generateCertificate(input);	
			}
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return c;
		
		
	}

}
