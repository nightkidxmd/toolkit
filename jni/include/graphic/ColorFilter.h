/*
 * ColorFilter.h
 *
 *  Created on: 2014-2-11
 *      Author: tstcit
 */

#ifndef COLORFILTER_H_
#define COLORFILTER_H_

#define FILTER_MASK 0xffff
#define FILTER_TOGREY 0x1
#define FILTER_FILLTEROUT_RED       FILTER_TOGREY    << 1
#define FILTER_FILLTEROUT_GREEN     FILTER_TOGREY    << 2
#define FILTER_FILLTEROUT_BLUE      FILTER_TOGREY    << 3
#define FILTER_NEGATIVE             FILTER_TOGREY    << 4
#define FILTER_BLACK_WHITE          FILTER_TOGREY    << 5
#define FILTER_RELIEF               FILTER_TOGREY    << 6
#define FILTER_BLUR_AVG             FILTER_TOGREY    << 7
#define FILTER_MAGNIFY              FILTER_TOGREY    << 8
#define FILTER_HAHA                 FILTER_TOGREY    << 9
#define FILTER_LEFT_TO_RIGHT        FILTER_TOGREY    << 10
#define FILTER_UP_TO_DOWN           FILTER_TOGREY    << 11
#define FILTER_MIRROR_X             FILTER_TOGREY    << 12
#define FILTER_MIRROR_Y             FILTER_TOGREY    << 13
#define FILTER_BRIGHT_AND_SHINNING  FILTER_TOGREY    << 14

int filter(int mode,int * buffer,long position,int w,int h,long size,int x, int y);


#endif /* COLORFILTER_H_ */
