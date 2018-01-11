#ifndef _ENGINEPOLICY_H_
#define _ENGINEPOLICY_H_


#include <jni.h>
#include "../include/utils/Map.h"
#define TYPE_ZOOM_ENGINE 1

class Engine{
public:
	 Engine():mType(0){};
	//Engine(int32_t type) {mType = type;}
	virtual ~Engine();
    inline int32_t getEngineType() {return mType;}
	virtual bool recogonizing(jobject event,jobject data);
	Engine& operator = (const Engine& rhs);
protected:
	int32_t mType;
};

//class EngineHub{
//public :
//	EngineHub();
//	~EngineHub();
//	void init();
//	bool hasSupported(int type);
//
//
//private:
//	Map<int,Engine> mEngines;
//};

//#ifdef DADA_ZOOM_ENGINE
class ZoomEngine : public Engine{
public:
	 ZoomEngine();
	//ZoomEngine(int32_t type) {mType = type;}
	~ZoomEngine();
	virtual bool recogonizing(jobject event,jobject data);
};
//#endif


#endif

