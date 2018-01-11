/*
 * ARGB.h
 *
 *  Created on: 2014-2-11
 *      Author: tstcit
 */

#ifndef ARGB_H_
#define ARGB_H_


int alpha(int input);
int red(int input);
int green(int input);
int blue(int input);
int update(int a,int r,int g,int b);


int RGB2HSL_V(int,int*,double*,double*,double*);

int  HSL_V2RGB(int*,int,double,double,double);

#endif /* ARGB_H_ */
