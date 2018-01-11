/*
 * ColorFilter.cpp
 *
 *  Created on: 2014-2-11
 *      Author: maoda.xu@samsung.com
 */
#include "../include/graphic/ColorFilter.h"
#include "../include/graphic/ARGB.h"
#include "../include/utils/log.h"
#include <math.h>
#define MAX(a,b) ((a)>(b)?(a):(b))
#define MIN(a,b) ((a)<(b)?(a):(b))
int filter(int mode,int * buffer,long position,int w,int h,long size,int x, int y){
   int m = mode & FILTER_MASK;
   int a = alpha(buffer[position]);
   int r = red(buffer[position]);
   int g = green(buffer[position]);
   int b = blue(buffer[position]);


   if((m & FILTER_FILLTEROUT_RED) != 0){
	   r = 0;
   }

   if((m & FILTER_FILLTEROUT_GREEN) != 0){
	   g = 0;
   }

   if((m & FILTER_FILLTEROUT_BLUE) != 0){
	   b = 0;
   }

   if((m & FILTER_NEGATIVE) != 0){
	   r = 255 - r;
	   g = 255 - g;
	   b = 255 - b;
   }

   if((m & FILTER_BLACK_WHITE) != 0){
	   int avg = (r+g+b)/3;
	   if(avg >= 128 ){
		   r=g=b=255;
	   }else{
		   r=g=b=0;
	   }

   }




   if((m & FILTER_BLUR_AVG) != 0){
	   int count = 0;
	   int sum[3]={0};
	   int offset[9]={-w-1,-w,-w+1,
	                    -1, 0,   1,
	                   w-1, w, w+1};
	   for(int i=0;i<9;i++){
		   long _offset_p =  position + offset[i];
		   if(_offset_p >= 0 && _offset_p < size){
			   sum[0] += red(buffer[_offset_p]);
			   sum[1] += green(buffer[_offset_p]);
			   sum[2] += blue(buffer[_offset_p]);
			   count++;
		   }
	   }

	   if(count -1 > 0){
		   r = (sum[0] -r)/(count-1);
		   g = (sum[1] -g)/(count-1);
		   b = (sum[2] -b)/(count-1);
	   }else{
		   ALOGE("count -1 <= 0,count=%d",count);
	   }
   }


   if(( m & FILTER_MAGNIFY ) != 0){
       int centerX = w/2;
       int centerY = h/2;
       int radix = MIN(w,h) / 2;

	   int distance = (int) ((centerX - x) * (centerX - x) + (centerY - y) * (centerY - y));
       if(distance  < radix * radix){
    	   int scl_x = (x-centerX)/2+centerX;
    	   int scl_y = (y-centerY)/2+centerY;
    	   int newColor=buffer[scl_y*w+scl_x];
    	   r  = red(newColor);
    	   g  = green(newColor);
    	   b  = blue(newColor);
       }
   }

   if(( m & FILTER_HAHA ) != 0){
       int centerX = w/2;
       int centerY = h/2;
       int radix = MIN(w,h) / 2;
       int real_radix = radix / 2;
	   int distance = (int) ((centerX - x) * (centerX - x) + (centerY - y) * (centerY - y));
       if(distance  < radix * radix){
    	   int scl_x = (x-centerX)/2;
    	   int scl_y = (y-centerY)/2;
    	   scl_x = (int)(scl_x * (sqrt(distance)/real_radix));
    	   scl_y = (int)(scl_y * (sqrt(distance)/real_radix));
    	   scl_x+=centerX;
    	   scl_y+=centerY;
    	   int newColor=buffer[scl_y*w+scl_x];
    	   r  = red(newColor);
    	   g  = green(newColor);
    	   b  = blue(newColor);
       }



   }



   if((m & FILTER_RELIEF) != 0){
	 int pre_color =  position-1 >=0 ? buffer[position-1]:0;
     r = r - red(pre_color) + 128;
     g = g - green(pre_color) + 128;
     b = b - blue(pre_color) + 128;
     m |= FILTER_TOGREY;
   }

   if((m & FILTER_TOGREY) != 0){
	   r=g=b=(r+g+b)/3;
   }


   if((m & FILTER_LEFT_TO_RIGHT) != 0){
       int centerX = w/2;
	   int scl_x = (centerX-x);
	   scl_x+=centerX;
	   int newColor = buffer[y*w+scl_x];
	   r  = red(newColor);
	   g  = green(newColor);
	   b  = blue(newColor);
   }


   if((m & FILTER_UP_TO_DOWN) != 0){
       int centerY = h/2;
	   int scl_y = (centerY-y);
	   scl_y+=centerY;
	   int newColor = buffer[scl_y*w+x];
	   r  = red(newColor);
	   g  = green(newColor);
	   b  = blue(newColor);
   }

   if((m & FILTER_MIRROR_Y) != 0){
         int centerX = w/2;
         if(x > centerX){
        	 int scl_x = 2*centerX - x;
        	 int newColor = buffer[y*w+scl_x];
			 r  = red(newColor);
			 g  = green(newColor);
			 b  = blue(newColor);
         }

   }


   if((m & FILTER_MIRROR_X) != 0){
         int centerY = h/2;
         if(y > centerY){
        	 int scl_y = 2*centerY - y;
        	 int newColor = buffer[scl_y*w+x];
			 r  = red(newColor);
			 g  = green(newColor);
			 b  = blue(newColor);
         }

   }

   if((m & FILTER_BRIGHT_AND_SHINNING) != 0){
	   //int centerX = w/2;
	   //if(x > centerX){
			int h;
			double s,l,v;
			RGB2HSL_V(buffer[position],&h,&s,&l,0);
			RGB2HSL_V(buffer[position],&h,&s,0,&v);
			//ALOGI("%d,%.2lf,%.2lf",h,s,l);
			s = MIN(1.0f,s*1.15f);
			//l = MIN(1.0f,l*1.15f);
			v = MIN(1.0f,v*1.15f);
			int newColor;
			HSL_V2RGB(&newColor,h,s,-1,v);
			//ALOGW("Before:%d,%d,%d",r,g,b);
			r  = red(newColor);
			g  = green(newColor);
			b  = blue(newColor);
			//ALOGW("After:%d,%d,%d",r,g,b);
	   //}
   }


	   r = MIN(255,MAX(0,r));
	   g = MIN(255,MAX(0,g));
	   b = MIN(255,MAX(0,b));

	return update(a,r,g,b);
}


