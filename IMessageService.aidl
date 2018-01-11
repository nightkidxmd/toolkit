package com.tuyou.framework.core.msg;
import android.os.IBinder;
interface IMessageService{ 
   oneway void registerCallback(in String types,in IBinder binder);
   oneway void unregisterCallback(in String type,in IBinder binder);
   oneway void unregisterAllCallback(in IBinder binder);
   oneway void sendMessage(String jsonMsg);
   
   
}