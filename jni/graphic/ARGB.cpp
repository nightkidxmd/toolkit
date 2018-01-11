/*
 * ARGB.cpp
 *
 *  Created on: 2014-2-11
 *      Author: maoda.xu@samsung.com
 */

#include "../include/graphic/ARGB.h"
#include <math.h>
#include <stdio.h>
#define MAX(a,b) ((a)>(b)?(a):(b))
#define MIN(a,b) ((a)<(b)?(a):(b))
#define CONVERT(a,type) ((type)(a))
#define CEIL(a)  CONVERT((a),double)-CONVERT((a),double) > 0 ?CONVERT((a)+1,int):CONVERT(a,int)



int alpha(int input){
   return (input & 0xFF000000) >> 24;
}

int red(int input){
   return (input & 0x00FF0000) >> 16;
}

int green(int input){
   return (input & 0x0000FF00) >> 8;
}

int blue(int input){
   return (input & 0x000000FF);
}

int update(int a,int r,int g,int b){
	return a << 24 | r << 16 | g << 8 | b;
}


/**
 *
 */
int  HSL_V2RGB(int *color_out,int h,double s,double l,double v){
	if(color_out == 0 || h < 0 || h >= 360 || s<0 || s>1)
		return -1;

	if( l*v > 0){
		return -1;
	}

	if(l == -1){//HSV
        int hi;
        double f,p,q,t;
        hi = CEIL((double)h/60) % 6;
        f = (double)h/60 - hi;
        p = v * (1-s);
        q = v * (1-f*s);
        t = v * (1-(1-f)*s);
        double hsv[6][3]={v,t,p,
                         q,v,p,
                         p,v,t,
                         p,q,v,
                         t,p,v,
                         v,p,q};

        *color_out = update(255,CONVERT(hsv[hi][0]*255,int),CONVERT(hsv[hi][1]*255,int),CONVERT(hsv[hi][2]*255,int));
	}else if(v == -1){//HSL
       double q,p,hk,tc[3]={0};
	   if( fabs(s) <0.0001 ){
		   for(int i=0;i<3;i++){
			   tc[i] = l;
		   }
	   }else{
		   if(l<0.5){
    		   q=l*(1+s);
		   }else{
    		   q = l+s-l*s;
		   }

		   p = 2 * l -q;
		   hk = (double)h/360;
		   tc[0] = hk + (double)1/3;
		   tc[1] = hk;
		   tc[2] = hk - (double)1/3;

		   for(int i=0;i<3;i++){
			   if(tc[i] > 1.0){
				   tc[i] = tc[i]-1.0;
			   }else if(tc[i] < 0){
				   tc[i] = tc[i]+1.0;
			   }
		   }


		   for(int i=0;i<3;i++){
    		   if(tc[i] < (double)1/6){
    			   tc[i] = p + ((q-p)*6*tc[i]);
    		   }else if((tc[i] > (double)1/6 || fabs(tc[i] - double(1/6)) <0.0001) && tc[i] < (double)1/2){
				   tc[i]= q;
    		   }else if((tc[i] > 0.5|| fabs(tc[i]-0.5) < 0.0001 ) && tc[i] < (double)2/3){
    			   tc[i] = p + ((q-p)*6*((double)2/3-tc[i]));
    		   }else{
    			   tc[i] = p;
    		   }

		   }
	   }
       *color_out = update(255,CONVERT(tc[0]*255,int),CONVERT(tc[1]*255,int),CONVERT(tc[2]*255,int));
	}else{
		return -1;
	}



	return 0;
}

/**
 *
 */

int RGB2HSL_V(int color_in,int* h,double* s,double* l,double *v){
    double r = (double)red(color_in)/255;
    double g = (double)green(color_in)/255;
    double b = (double)blue(color_in)/255;
    double max = MAX(r,MAX(g,b));
    double min = MIN(r,MIN(g,b));

    if(h == 0 || s == 0){
    	return -1;
    }

   if( max == min){
	   *h = 0;
   }else if (max == r && g >= b){
	   *h = CONVERT(60 * (g-b)/(max-min),int);
   }else if (max == r && g <b ){
	   *h = CONVERT(60 * (g-b)/(max-min) + 360,int);
   }else if( max == g){
	   *h = CONVERT(60 * (b-r)/(max-min) + 120,int);
   }else if(max == b){
	   *h = CONVERT (60 * (r-g)/(max-min) + 240,int);
   }

   if(l != 0){
	   *l = 0.5*(max+min);
		if(*l == 0 || max == min){
			*s = 0;
		}else if( *l > 0 && (*l <0.5 || fabs(*l-0.5) < 0.000001 )){
			*s = (max-min)/(2*  (*l));
		}else if( *l > 0.5){
			*s = (max-min)/(2-2* (*l));
		}
   }else if(v != 0){
        *v =  max;
        if( max == 0){
        	*s = 0;
        }else{
        	*s =  (1-min/max);
        }
   }else{
	   return -1;
   }

   return 0;


}


