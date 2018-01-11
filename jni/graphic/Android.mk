LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE    := BitmapEngine
LOCAL_SRC_FILES := \
   BitmapEngine.cpp \
   ARGB.cpp \
   ColorFilter.cpp
LOCAL_LDLIBS := -lm -llog

include $(BUILD_SHARED_LIBRARY)



