package com.dadatoolkit.android.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.widget.EditText;
/**
 * 
 * @author maoda.xu@samsung.com
 *
 */

public class AutoResizeEditText extends EditText implements TextWatcher {
	
	private int mLimitedWidth;
	private float mScaledDensity;
	private float mScaledTextSize = 0.5f;
	private int mMaxTextSize = 36;
	private int mOldOrientation;
	private Context mContext;

	public AutoResizeEditText(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
		mContext = context;
		addTextChangedListener(this);
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		
		
		mLimitedWidth  = displayMetrics.widthPixels - getPaddingLeft()- getPaddingRight();
		mScaledDensity = displayMetrics.density;
		mOldOrientation = context.getResources().getConfiguration().orientation;
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}
	
	
	@Override
	public void onTextChanged (CharSequence text, int start, int before, int count) {
		Update(before < count,start);
	}
	
	public void setMaxTextSize(int maxSize){
		mMaxTextSize = maxSize;
	
	}
	
	@Override
	protected void onConfigurationChanged (Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		mLimitedWidth = mContext.getResources().getDisplayMetrics().widthPixels - getPaddingLeft()- getPaddingRight();

		Update(mOldOrientation - newConfig.orientation > 0 ,1);
		mOldOrientation = newConfig.orientation;
	}

	private void Update(boolean isL2P, int start){
		float textSize = getTextSize();
		CharSequence s = getText();
		TextPaint textPaint = getPaint();
		float textWidth = textPaint.measureText(s, 0, s.length());
		if(isL2P){ //Add && L2P
			while(textWidth > mLimitedWidth){
				textSize -= mScaledTextSize;
				setTextSize(convertPixelToDp(textSize));
				textWidth = textPaint.measureText(s, 0, s.length());
			}
		}else{//Delete && P2L
			do{
				textSize += mScaledTextSize;
				setTextSize(convertPixelToDp(textSize));
				textWidth = textPaint.measureText(s, 0, s.length());
			}while(start !=0 && textWidth <= mLimitedWidth && textSize <= mMaxTextSize);
			textSize -= mScaledTextSize;
		}
		setTextSize(convertPixelToDp(textSize));

	}

    private float convertPixelToDp(float px){
    	float dp = (px / mScaledDensity );
    	return dp;
    }

}
