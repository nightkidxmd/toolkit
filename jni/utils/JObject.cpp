/*
 * JObject.cpp
 *
 *  Created on: 2014-6-12
 *      Author: maoda.xu@samsung.com
 */
#include "../include/utils/JObject.h"
//#include<cstdio>

//using namespace std;

JObject::JObject(JNIEnv* env):  m_pEnv(env),
		                        m_pObject(0),
                                m_pClazz(0)
{
}


JObject::JObject(JNIEnv* env, jobject obj):m_pEnv(0),
                                           m_pObject(0),
                                           m_pClazz(0)
{
      if(0 == env || 0 == obj){
    	  return;
      }
      m_pEnv = env;
      m_pObject = obj;
      m_pClazz = m_pEnv->GetObjectClass(obj);


}

JObject::JObject(JNIEnv* env, jclass clazz):m_pEnv(0),
                                                      m_pObject(0),
                                                      m_pClazz(0)
{
    if(0 == env || 0 == clazz){
  	  return;
    }

    m_pEnv = env;
    m_pClazz = clazz;
    //m_pObject = env->NewObject(clazz,"<init>","()V");

}



