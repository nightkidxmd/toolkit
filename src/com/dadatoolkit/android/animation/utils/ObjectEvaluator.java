package com.dadatoolkit.android.animation.utils;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.view.View;
/**
 * 
 * @author maoda.xu@samsung.com
 *
 */
abstract public class ObjectEvaluator  implements TypeEvaluator<Object> {


	protected float start;
	protected float end;

	public ObjectEvaluator(float start, float end){
		this.start = start;
		this.end = end;
	}


	abstract public void onAnimationUpdate(View view,ValueAnimator animator);

}
