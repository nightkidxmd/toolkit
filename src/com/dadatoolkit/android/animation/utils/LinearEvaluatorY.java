package com.dadatoolkit.android.animation.utils;

import android.animation.ValueAnimator;
import android.view.View;
/**
 * 
 * @author maoda.xu@samsung.com
 *
 */
public class LinearEvaluatorY  extends ObjectEvaluator {


	private float b;
	private float a;


	public LinearEvaluatorY(float a,float b,float start,float end){
		super(start, end);
		this.a = a;
		this.b = b;
	}
	
	@Override
	public Object evaluate(float fraction, Object startValue, Object endValue) {
		float y = start + fraction*(end - start);
		float x = b+a*y;

		return x;
	}
	@Override
	public void onAnimationUpdate(View view, ValueAnimator animator) {
		float y = start + animator.getAnimatedFraction() * (end - start);
		float x = (Float) animator.getAnimatedValue();
		view.setX(x);
		view.setY(y);	
	}
	
	

}
