package com.dadatoolkit.android.animation.utils;

import android.animation.ValueAnimator;
import android.view.View;
/**
 * 
 * @author maoda.xu@samsung.com
 *
 */
public class LinearEvaluatorX  extends ObjectEvaluator {


	private float b;
	private float a;

	public LinearEvaluatorX(float a,float b,float start,float end){
		super(start, end);
		this.a = a;
		this.b = b;

	}
	@Override
	public Object evaluate(float fraction, Object startValue, Object endValue) {
		float startFloat = ((Number) startValue).floatValue();
		float endFloat = ((Number) endValue).floatValue();
		
		float x = startFloat + fraction*(endFloat - startFloat);
		float y = a*x+b;

		return y;
	}
	@Override
	public void onAnimationUpdate(View view, ValueAnimator animator) {
		float x = start + animator.getAnimatedFraction() * (end - start);
		float y = (Float) animator.getAnimatedValue();
		view.setX(x);
		view.setY(y);	
	}
}
