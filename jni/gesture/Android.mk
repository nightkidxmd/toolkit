LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := DadaGestureEngine
LOCAL_SRC_FILES := GestureEngine.cpp EngineHub.cpp ZoomEngine.cpp
LOCAL_LDLIBS := -lm -llog

LOCAL_CPPFLAGS += -DDADA_ZOOM_ENGINE

include $(BUILD_SHARED_LIBRARY)




