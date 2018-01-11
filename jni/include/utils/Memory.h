/*
 * Memory.h
 *
 *  Created on: 2014-4-7
 *      Author: maoda.xu@samsung.com
 */

#ifndef MEMORY_H_
#define MEMORY_H_
#include<sys/types.h>
class Memory{
public:
  static Memory* alloc(size_t size);
};


#endif /* MEMORY_H_ */
