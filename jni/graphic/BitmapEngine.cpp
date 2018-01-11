#include "com_dadatoolkit_android_graphic_BitmapEngine.h"
#include "../include/utils/log.h"
#include "../include/graphic/ARGB.h"
#include "../include/graphic/ColorFilter.h"
#include <pthread.h>
#include <sched.h>




pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;
pthread_cond_t  cond  = PTHREAD_COND_INITIALIZER;

struct Data{
	int* oringal;
	int* modifed;
	long from;
	long to;
	int w;
	int h;
};



void* handle(void * data){
	ALOGD("enter handle!");
	pthread_mutex_lock(&mutex);
	ALOGD("start handle!");
    for(long i=0;i<((Data*)data)->w;i++){
    	for(long j=0;j<((Data*)data)->h;j++){
    		long position = j*((Data*)data)->w+i;
    		((Data*)data)->modifed[position] = filter(FILTER_RELIEF,
    				((Data*)data)->oringal,position,((Data*)data)->w,((Data*)data)->h,((Data*)data)->to,i,j);
    	}
    }
    ALOGD("broadcast!");
    pthread_cond_broadcast(&cond);
    pthread_mutex_unlock(&mutex);
}
JNIEXPORT jintArray JNICALL Java_com_dadatoolkit_android_graphic_BitmapEngine_nativeEffect
  (JNIEnv *env, jclass clazz, jintArray buffer,jint width,jint height){

	ALOGE("width=%d,height=%d,size=%d,%d",width,height,width*height,env->GetArrayLength(buffer));
    ALOGE("before GetIntArrayElements!");
    int *temp = env->GetIntArrayElements(buffer,0);
    int w = width;
    int h = height;
    long size = width * height;
    ALOGE("before Genew_buffer!");
    int new_buffer[size];

    pthread_t threadA;
    Data data;
    data.oringal = temp;
    data.modifed = new_buffer;
    data.from = 0;
    data.to = size;
    data.w = w;
    data.h = h;
    ALOGE("before create thread!");
    pthread_create(&threadA,0,handle,&data);
    pthread_mutex_lock(&mutex);
    ALOGD("wait!");
    pthread_cond_wait(&cond,&mutex);
    pthread_mutex_unlock(&mutex);
    pthread_join(threadA,0);


    ALOGD("DONE!");

    env->SetIntArrayRegion(buffer,0,size,new_buffer);
    //jintArray result = env->NewIntArray(size);
   // env->SetIntArrayRegion(result,0,size,new_buffer);
    env->ReleaseIntArrayElements(buffer,temp,0);
	return 0;
}
