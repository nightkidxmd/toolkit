/*
 * Map.h
 *
 *  Created on: 2014-4-7
 *      Author: maoda.xu@samsung.com
 */

#ifndef Map_H_
#define Map_H_
#include "MapImpl.h"

template <class K,class T>
class Map : private MapImpl{
public:
//	          typedef T value_type;
	          Map();
              Map(const Map<K,T>& rhs);
    virtual   ~Map();


    const Map<K,T>& operator = (const Map<K,T>& rhs)const;
    Map<K,T>&       operator = (const Map<K,T>& rhs);



    inline bool             isEmpty() const  {return MapImpl::isEmpty();}
    inline void             clear();
    inline size_t           size()    const  {return MapImpl::size();}
    inline T                get(K key);
    inline ssize_t          add(K key,T value);
    inline T                remove(K key);
};


template <class K,class T> inline
Map<K,T>::Map():MapImpl(sizeof(T)){

}

template <class K,class T> inline
Map<K,T>::Map(const Map<K,T>& rhs):MapImpl(rhs){

}
//template <class K,class T> inline
//const Map<K,T>& operator = (const Map<K,T>& rhs){
//
//}


#endif /* Map_H_ */

