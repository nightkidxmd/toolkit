#ifndef __J_OBJECT_H__
#define __J_OBJECT_H__
#include <jni.h>

//namespace DadaToolkit{
   class JObject
   {
   public:
	   JObject(JNIEnv* env);
	   JObject(JNIEnv* env, jobject obj);
	   JObject(JNIEnv* env, jclass clazz);
	   virtual ~JObject();

	   virtual jobject getObject() const;

	   void detachObject();

   protected:
       JNIEnv *m_pEnv;
       jobject m_pObject;
       jclass  m_pClazz;
   };
//}
#endif
