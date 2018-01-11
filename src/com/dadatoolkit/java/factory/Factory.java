package com.dadatoolkit.java.factory;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Factory {
	
	private static final int DEFAULT_WORKER_NUMBER = 10;
	
	private ExecutorService mWorkers;
	
	private static Factory mFactory;	
	
	
	public static Factory getInstance(){
		return getInstance(DEFAULT_WORKER_NUMBER);
	}
	
	public static Factory getInstance(int worker_number){
		if(mFactory == null){
			mFactory = new Factory(worker_number);
		}
		return mFactory;
	}
	
	
	private Factory(int worker_number){
		mWorkers = Executors.newFixedThreadPool(DEFAULT_WORKER_NUMBER);
	}
	
	
	public void submitWork(Runnable work){
		mWorkers.submit(work);
	}
	
	public void submitWork(ArrayList<Runnable> works){
		for(Runnable w:works){
			mWorkers.submit(w);
		}
	}
	
	public void shutdownNow(){
		if(!mWorkers.isShutdown()){
			mWorkers.shutdownNow();
		}
	}
}
