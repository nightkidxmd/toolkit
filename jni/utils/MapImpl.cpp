/*
 * MapImpl.cpp
 *
 *  Created on: 2014-4-7
 *      Author: maoda.xu@samsung.com
 */


#include "../include/utils/MapImpl.h"
#include <vector>

MapImpl::~MapImpl(){

}


MapImpl::MapImpl(uint32_t itemSize):mStorage(0),mCount(0),mItemSize(itemSize){
}

MapImpl::MapImpl(const MapImpl& rhs):
		mStorage(rhs.mStorage),mCount(rhs.mCount),
		mItemSize(rhs.mItemSize){

}

MapImpl& MapImpl::operator = (const MapImpl& rhs){
   if(this != &rhs){
      if(rhs.mCount){
    	mStorage = rhs.mStorage;
    	mCount = rhs.mCount;

      }else{
    	  mStorage = 0;
    	  mCount = 0;
      }

   }
   return *this;
}



void* MapImpl::get(void* key){
   for(int i=0;i<mCount;i++){


   }

   return 0;
}





