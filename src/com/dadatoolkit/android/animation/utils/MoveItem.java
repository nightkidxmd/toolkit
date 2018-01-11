package com.dadatoolkit.android.animation.utils;

import android.animation.Animator.AnimatorListener;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.view.View;

/**
 * @author maoda.xu@samsung.com
 *
 */
abstract public class MoveItem implements AnimatorUpdateListener, AnimatorListener  {

	private View mItemView;
	private float mStart;
	private float mEnd;
	private AnimatorSet mAnimatorSet;
	private ObjectEvaluator mEvaluator;

	protected void initAnimator(View itemView,float start, float end, long startDelay,
			long duration,int repeatMode,int repeatCount,ObjectEvaluator evaluator){
		mItemView = itemView;
		mItemView.setVisibility(View.INVISIBLE);
		mStart = start;
		mEnd = end;
		mEvaluator = evaluator;


		ValueAnimator animator = ValueAnimator.ofObject(mEvaluator, mStart,mEnd);
		animator.setRepeatMode(repeatMode);
		animator.setRepeatCount(repeatCount);
		animator.setDuration(duration);
		animator.setStartDelay(startDelay);
		animator.addUpdateListener(this);
		animator.addListener(this);
		mAnimatorSet = new AnimatorSet();
		mAnimatorSet.playTogether(animator);
	}


	@Override
	public void onAnimationUpdate(ValueAnimator animator) {
		mEvaluator.onAnimationUpdate(mItemView, animator);
	}



	protected View getItemView(){
		return mItemView;
	}

	public AnimatorSet getAnimatorSet(){
		return mAnimatorSet;
	}

	@Override
	public void onAnimationCancel(Animator arg0) {

	}
	@Override
	public void onAnimationEnd(Animator arg0) {

	}
	@Override
	public void onAnimationRepeat(Animator arg0) {

	}
	@Override
	public void onAnimationStart(Animator arg0) {
		getItemView().setVisibility(View.VISIBLE);
	}
}
