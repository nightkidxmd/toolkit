#include <stdio.h>
#include "com_dadatoolkit_android_view_GestureDetectorNative.h"
#include "../include/utils/log.h"
#include "../include/gesture/EnginePolicy.h"
#include <malloc.h>


JNIEXPORT jboolean JNICALL Java_com_dadatoolkit_android_view_GestureDetectorNative_getResult
  (JNIEnv *env, jclass, jint, jobject event, jobject data){


   jclass  eventClass = env->FindClass("android/view/MotionEvent");
   jmethodID method = env->GetMethodID(eventClass,"getX","()F");
   jfloat x = env->CallFloatMethod(event,method);
   ALOGE("x=%f",x);
   //ZoomEngine engine = ZoomEngine();


return true;
}
