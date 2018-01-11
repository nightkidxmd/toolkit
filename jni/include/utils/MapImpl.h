/*
 * MapImpl.h
 *
 *  Created on: 2014-4-7
 *      Author: maoda.xu@samsung.com
 */

#ifndef MapIMPL_H_
#define MapIMPL_H_
#include <sys/types.h>
class MapImpl{
public:
	MapImpl(uint32_t itemSize);
	MapImpl(const MapImpl& rhs);
	virtual ~MapImpl();

    MapImpl& operator = (const MapImpl& rhs);



    inline const void* arrayImpl() const {return mStorage;}



    inline bool             isEmpty() const  {return mCount==0;}
    inline void             clear();
    inline uint32_t           size()    const  {return mCount;}
    inline void*            get(void* key);
    inline uint32_t          add(void* key,void* value);
    inline void*            remove(void* key);

protected:
    void release_storage();
private:
		void * mStorage;
		uint32_t mCount;
  const uint32_t mItemSize;


};


#endif /* MapIMPL_H_ */
