package com.dadatoolkit.android.app;
/**
 * 2015-05-27 maoda.xu@samsung.com add touchable check
 * 2014-03-26 maoda.xu@samsung.com reconstruct GestureDetector Feature
 * 2014-02-08 maoda.xu@samsung.com fix autoAsign position error when rotating.
 */
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.dadatoolkit.android.view.DoubleTapGestureDetector;
import com.dadatoolkit.android.view.DoubleTapGestureDetector.OnDoubleTapListener;
import com.dadatoolkit.android.view.GestureDetectorService;
import com.dadatoolkit.android.view.LongPressGestureDetector;
import com.dadatoolkit.android.view.LongPressGestureDetector.OnLongPressListener;
import com.dadatoolkit.android.view.ShortPressGestureDetector;
import com.dadatoolkit.android.view.ShortPressGestureDetector.OnShortPressListener;
import com.dadatoolkit.android.view.ZoomGestureDetector;
import com.dadatoolkit.android.view.ZoomGestureDetector.OnZoomListener;
/**
 * <b>Note:</b> Need android.permission.SYSTEM_ALERT_WINDOW
 * @author maoda.xu@samsung.com
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class FloatingWindowFactory  {
	
	/**
	 * When release,the window will auto assign to the edge of the screen
	 */
	public static final int FEATURE_AUTO_ASSIGN = 0x1;
	/**
	 * Switch width and height when rotating
	 */
	public static final int FEATURE_ROTATION = FEATURE_AUTO_ASSIGN << 1;
	
	/**
	 * Can move window position
	 */
	public static final int FEATURE_MOVE = FEATURE_AUTO_ASSIGN << 2;
	
	
	
	// GestureDetector Feature Start
	
	
	public static final int FEATURE_GESTURE_MASK = 0xf0;
	
	
	/**
	 * 
	 */
	public static final int FEATURE_GESTURE_LONGPRESS = FEATURE_AUTO_ASSIGN << 4;
	public static final int FEATURE_GESTURE_SHORTPRESS = FEATURE_AUTO_ASSIGN << 5;
	public static final int FEATURE_GESTURE_ZOOM = FEATURE_AUTO_ASSIGN << 6;
	public static final int FEATURE_GESTURE_DOUBLETAP = FEATURE_AUTO_ASSIGN << 7;
	
	
	public static final int GESTURE_FLAG_OVERRIDE_ZOOM = 0x1;
	public static final int GESTURE_FLAG_OVERRIDE_DOUBLE_TAP = GESTURE_FLAG_OVERRIDE_ZOOM << 1;
	public static final int GESTURE_FLAG_OVERRIDE_LONG_PRESS = GESTURE_FLAG_OVERRIDE_ZOOM << 2;
	public static final int GESTURE_FLAG_OVERRIDE_SHORT_PRESS = GESTURE_FLAG_OVERRIDE_ZOOM << 3;
	
	
	
	public static final int GESTURE_TYPE_ZOOM = 1;
	public static final int GESTURE_TYPE_DOUBLE_TAP = 2;
	public static final int GESTURE_TYPE_LONG_PRESS = 3;
	public static final int GESTURE_TYPE_SHORT_PRESS = 4;
	
	
	
	private final String GESTURE_KEY_TYPE = "type";

	
	
	public static final String GESTURE_KEY_ZOOM = "zoom";
	
	
	
	
	private final int MESSAGE_UPDATE_WINDOW = 1;
	private final int MESSAGE_GESTURE_CALLBACK = 2;
	
	private ViewGroup mContainer;
    private Context mContext;
    private boolean mAutoAsign = false;
    private FloatingWindowFactory.OnTouchListener mOnTouchListener;
    private int mFeatureFlag = 0;
    private GestureDetectorService mGestureDetectorService;
    private GestureCallback mGestureCallback;
    private int mGestureOverrideFlags = 0;
    
    private boolean mRemoved = false;
    private Object mLock = new Object();
    
    private WindowManager.LayoutParams mCustomerParams=null;
    
   
    private Handler mHandler = new Handler(Looper.getMainLooper()){
    	public void handleMessage(Message msg){
    		switch(msg.what){
    		case MESSAGE_UPDATE_WINDOW:
    			double scale = msg.getData().getDouble(GESTURE_KEY_ZOOM);
    			if(mContainer != null){
	    			((Container)mContainer).updateWindowSize(scale*2);
    				
    			}
    			break;
    		case MESSAGE_GESTURE_CALLBACK:
    			Bundle data = msg.getData();
    			int type = data.getInt(GESTURE_KEY_TYPE);
    			mGestureCallback.onGestureRecognized(type, data);
    			break;
    		default:
    			break;
    		}
    	}
    };
    
    
    /**
     * 
     * Override default operation of {@link Container#onZoom(double)}
     *
     */
    
    public interface GestureCallback{
    	public void onGestureRecognized(int type, Bundle data);
    }
    
    
    public interface OnTouchListener{
    	public boolean onTouchEvent (MotionEvent event);
    }
    
    /**
     * 2015-02-25 add
     * @author maoda.xu@samsung.com
     *
     */
    private OnAttachToSideListener mOnAttachToSideListener;
    public interface OnAttachToSideListener{
    	public void onAttachToSide();
    }
    
    public void setOnAttachToSideListener(OnAttachToSideListener l){
    	mOnAttachToSideListener = l;
    }

    
    /**
     * Use default LinearLayout as container
     * @param context
     * @param orientation  LinearLayout.VERTICAL <br>
     *                     LinearLayout.HORIZONTAL (Default)
     */
	
	public FloatingWindowFactory(Context context,int orientation,WindowManager.LayoutParams wmparams) {
		this(context,orientation,wmparams,true);
	}
	
	
	/**
	 * Use default LinearLayout as container
	 * @param context
	 * @param orientation  LinearLayout.VERTICAL <br>
     *                     LinearLayout.HORIZONTAL (Default)
	 * @param wmparams
	 * @param touchable
	 */
	public FloatingWindowFactory(Context context,int orientation,WindowManager.LayoutParams wmparams,boolean touchable) {
		mContext = context;
		mContainer = new Container(context);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		mContainer.setLayoutParams(lp);
		((LinearLayout) mContainer).setOrientation(orientation);
		mCustomerParams = wmparams;
		addWindow(touchable);
	}
	/**
	 * 
	 * @param context
	 * @param container ViewGroup with default layout parameters: <br>
	 *                  ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
	 */
	
	public FloatingWindowFactory(Context context, ViewGroup container,WindowManager.LayoutParams wmparams) {
		mContext = context;
		mContainer = container;
		ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
		mContainer.setLayoutParams(lp);
		mCustomerParams = wmparams;
		addWindow();
	}
	/**
	 * 
	 * @param context
	 * @param container   
	 * @param lp          Customer's layout parameters
	 */
	
	public FloatingWindowFactory(Context context, ViewGroup container,ViewGroup.LayoutParams lp,WindowManager.LayoutParams wmparams) {
		mContext = context;
		mContainer = container;
		mContainer.setLayoutParams(lp);
		mCustomerParams = wmparams;
		addWindow();
	}
		
	/**
	 * Adds a child view. If no layout parameters are already set on the child,<br>
	 *  the default parameters for this ViewGroup are set on the child
	 * @param view the child view to add
	 */
	public void addView(View view){
		mContainer.addView(view);
	}
	
	/**
	 * Adds a child view. If no layout parameters are already set on the child,<br>
	 * the default parameters for this ViewGroup are set on the child. 
	 * @param view  the child view to add
	 * @param index the position at which to add the child 
	 */
	public void addView(View view, int index){
		mContainer.addView(view, index);
		
	}
	/**
	 * Adds a child view with the specified layout parameters.
	 * @param view  the child view to add
	 * @param params the layout parameters to set on the child 
	 */
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	public void addView(View view,ViewGroup.LayoutParams params){
		mContainer.addView(view, params);
	}
	
	
	/**
	 * Call this method to remove all child views from the ViewGroup <br>
	 * and then remove itself from the window.
	 */
	
	@SuppressLint("NewApi")
			
	public void removeAllViews(){
		synchronized(mLock){
			 mRemoved = true;
		}
		 if(mGestureDetectorService != null){
			 mGestureDetectorService.destroy();
			 mGestureDetectorService = null;
		 }
		 removeAllChildViews();
		 ((Container)mContainer).onRemove();
		 WindowManager wm = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
		 wm.removeView(mContainer);
		 mContainer = null;


	}
	
	public void removeChildView(View childView){
		if(mContainer != null){
			mContainer.removeView(childView);
		}
	}
	
	/**
	 * Call this method to remove all child views from the ViewGroup
	 */
	
	public void removeAllChildViews(){
		
		if(mContainer != null){
			mContainer.removeAllViews();
		}
		

	}
	
	/**
	 * 
	 * @deprecated
	 * @param autoAsign true enabled otherwise false disabled using {@link FloatingWindowFactory#addFeatures(FloatingWindowFactory.FEATURE_AUTO_ASSIGN)}
	 *        instead.
	 */
	public void setAutoAsignEnabled(boolean autoAsign){
		mAutoAsign = autoAsign;
	}
	
	/**
	 * 
	 * @param feature {}
	 */
	
	
	public void addFeatures(int feature){
		if( (mFeatureFlag & feature) == 0 ){
			mFeatureFlag |= feature;
			if ((mFeatureFlag & FEATURE_GESTURE_MASK) != 0){
				if(mGestureDetectorService == null){
					mGestureDetectorService = new GestureDetectorService();
				}
				
				
		        
                

				if( (mFeatureFlag & FEATURE_GESTURE_DOUBLETAP) != 0){
					DoubleTapGestureDetector mDoubleTapGestureDetector = new DoubleTapGestureDetector(mGestureDetectorService);
					mDoubleTapGestureDetector.setListener((OnDoubleTapListener) mContainer);
					mGestureDetectorService.registerDetector(mDoubleTapGestureDetector);
				}
				
				
				
				if( (mFeatureFlag & FEATURE_GESTURE_SHORTPRESS) != 0){
					ShortPressGestureDetector mShortPressGestureDetector = new ShortPressGestureDetector(mGestureDetectorService);
					mShortPressGestureDetector.setListener((OnShortPressListener) mContainer);
					mGestureDetectorService.registerDetector(mShortPressGestureDetector);
					
			    }
				
				
				if( (mFeatureFlag & FEATURE_GESTURE_LONGPRESS) != 0){
					LongPressGestureDetector mLongPressGestureDetector = new LongPressGestureDetector(mGestureDetectorService);
					mLongPressGestureDetector.setListener((OnLongPressListener) mContainer);
					mGestureDetectorService.registerDetector(mLongPressGestureDetector);
					
			    }
				
				
				if( (mFeatureFlag & FEATURE_GESTURE_ZOOM) != 0){
					ZoomGestureDetector mZoomGestureDetector = new  ZoomGestureDetector(mGestureDetectorService);
					mZoomGestureDetector.setListener((OnZoomListener) mContainer);
					mGestureDetectorService.registerDetector(mZoomGestureDetector);
					
			    }
			}
		}
	}
	
	private void addWindow(){
		addWindow(true);
	}
	
	private void addWindow(boolean touchable){
		WindowManager.LayoutParams wmParams = getWindowLayoutParams(touchable);
        WindowManager wm = (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
        mContainer.setVisibility(View.INVISIBLE);
        wm.addView(mContainer, wmParams);
	}
	
	
	private WindowManager.LayoutParams getWindowLayoutParams(boolean touchable){
		WindowManager.LayoutParams wmParams = null;
		if(mCustomerParams == null){
			wmParams = new WindowManager.LayoutParams();
		}else{
			wmParams = mCustomerParams;
		}
		
		
		Log.e("DADA","wmParams.flags:"+wmParams.flags);
		if(wmParams.type == WindowManager.LayoutParams.TYPE_APPLICATION){
			wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
		}

		wmParams.format = PixelFormat.RGBA_8888;
		
		if(wmParams.flags == 0){
			wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL; 
		}
		
		if(!touchable){
			wmParams.flags |= WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
		}
		
        wmParams.width= WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height= WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.gravity |= Gravity.TOP | Gravity.LEFT;
        return wmParams;
	}
	
	public void setWindowLayoutParams(WindowManager.LayoutParams params){
		mCustomerParams = params;
	}
	
	public void show(){
		mContainer.setVisibility(View.VISIBLE);
	}
	
	public void hide(){
		mContainer.setVisibility(View.INVISIBLE);
	}
	
	
    
    public void setOnTouchListener(FloatingWindowFactory.OnTouchListener l){
    	mOnTouchListener = l;
    }
    
    
    public ViewGroup getContainer(){
    	return mContainer;
    }
    
	public void registerGestureCallBack(GestureCallback callback,int flags){
		mGestureCallback = callback;
		mGestureOverrideFlags = flags;
	}
	
	public void unRegisterGestureCallBack(){
		mGestureCallback = null;
		mGestureOverrideFlags = 0;
	}
	
	/**
	 * 
	 * The container of floating window.
	 * @author maoda.xu@samsung.com
	 *
	 */
	private class Container extends LinearLayout implements OnZoomListener, OnLongPressListener, OnShortPressListener,OnDoubleTapListener, AnimatorUpdateListener, AnimatorListener{
	    private int[] mScreen = new int[]{0,0,0};
		private float mRawX;
		private float mRawY;
		private float mStartX;
		private float mStartY;
		private int mOldOrientation;
		private boolean bIsResizing = false;
	    private int mOriginalWidth = 0;
	    private int mOriginalHeight = 0;
	    private int tempX;
	    private int tempY;
	    private AnimatorSet mAnimatorSet;
	    private boolean isRemoved = false;
		
		public Container(Context context) {
			super(context);
			DisplayMetrics outMetrics = new DisplayMetrics();
		    ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(outMetrics);
		    mScreen[Configuration.ORIENTATION_PORTRAIT] = Math.min(outMetrics.widthPixels, outMetrics.heightPixels);
		    mScreen[Configuration.ORIENTATION_LANDSCAPE] = Math.max(outMetrics.widthPixels, outMetrics.heightPixels);
		    mOldOrientation = mContext.getResources().getConfiguration().orientation;
		}
		
		@Override
		protected void onConfigurationChanged (Configuration newConfig) {
			if(mOldOrientation != newConfig.orientation){
				if(mAutoAsign || (mFeatureFlag & FEATURE_AUTO_ASSIGN) != 0){
					WindowManager.LayoutParams lp = (android.view.WindowManager.LayoutParams) this.getLayoutParams();
					lp.x = (lp.x + getWidth()) >= mScreen[mOldOrientation] ? mScreen[newConfig.orientation]:lp.x;
					((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).updateViewLayout(this, lp);
				}
				
				
	        	if((mFeatureFlag & FEATURE_ROTATION) != 0 && getChildCount() > 0){
	        		//-----------------------------------------------
	        		mOriginalWidth  = mOriginalWidth^mOriginalHeight;
	        		mOriginalHeight = mOriginalWidth^mOriginalHeight;
	        		mOriginalWidth  = mOriginalWidth^mOriginalHeight;
	        		//-----------------------------------------------
	        		View child = getChildAt(0);
	        		ViewGroup.LayoutParams lp =  child.getLayoutParams();
	        		lp.width  = lp.width ^ lp.height;
	        		lp.height = lp.width ^ lp.height;
	        		lp.width  = lp.width ^ lp.height;
	        		child.requestLayout();	        		
	        	}

				
				mOldOrientation = newConfig.orientation;
				
			}
			super.onConfigurationChanged(newConfig);
		}
		


        @SuppressLint("ClickableViewAccessibility") @Override
		public boolean onTouchEvent (MotionEvent event) {
        	if(mAnimatorSet != null && mAnimatorSet.isRunning()){
        		mAnimatorSet.cancel();
        	}
        	
        	if(mGestureDetectorService != null)
        		mGestureDetectorService.recogonizing(event);
        	
        	
			int statusbarHeight =  getStatusBarHeightIner();
			mRawX = event.getRawX();
			mRawY = event.getRawY() - statusbarHeight;
			switch(event.getAction()){
				case MotionEvent.ACTION_DOWN:
					mStartX = event.getX();
					mStartY = event.getY();
					break;
				case MotionEvent.ACTION_MOVE:
					if(Math.abs(mStartX - event.getX()) < 20.0f && Math.abs(mStartY - event.getY()) < 20.0f){
						return true;
					}
					
					if(bIsResizing){
						break;
					}

					updateWindowPosition(false,false);
					break;
				case MotionEvent.ACTION_UP:
					if(bIsResizing){
						bIsResizing = false;
					}else{
						updateWindowPosition(true,false);		
					}
					break;
				default:
					break;
			}
			
			
			if(mOnTouchListener != null){
				mOnTouchListener.onTouchEvent(event);
			}
				
			return true;	
		}
        
        
        private void updateWindowSize(double scale){
        	if(getChildCount() > 0){	
        		View child = getChildAt(0);
        		ViewGroup.LayoutParams lp =  child.getLayoutParams();
        		if(mOriginalWidth == 0){
        			mOriginalWidth = lp.width;
        			mOriginalHeight = lp.height;
        		}
        		DisplayMetrics outMetrics = new DisplayMetrics();
        		((WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(outMetrics);
        		double ratio =(double) outMetrics.heightPixels/outMetrics.widthPixels;
        		int scal_w = (int) (lp.width + scale);
        		lp.width =Math.min(outMetrics.widthPixels, Math.max(mOriginalWidth, scal_w));
        		lp.height = (int) (lp.width * ratio);
        		child.requestLayout();
        	}
        	
        }
        

		@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
		private void updateWindowPosition(boolean bDropped,boolean fromAnimation){ 
			if((mFeatureFlag & FEATURE_MOVE) == 0)
				 return;
			 
			synchronized(mLock){
				if(mRemoved)
					return;
			}
			
			WindowManager.LayoutParams lp = (android.view.WindowManager.LayoutParams) getLayoutParams();
			if(!fromAnimation){
				tempX = (int)(mRawX - mStartX);
				tempY = (int)(mRawY - mStartY);
			}
			if((mAutoAsign || (mFeatureFlag & FEATURE_AUTO_ASSIGN) != 0)&& bDropped){
				int tempWith = mScreen[mContext.getResources().getConfiguration().orientation] - getWidth();
			
				
				tempX = tempX < tempWith/2 ? 0 : tempWith;
				startAnimation(lp.x,tempX);
				return;
			}

			
			lp.x = tempX;
			lp.y = tempY;
			((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).updateViewLayout(this, lp);
			
		}
			
		private int getStatusBarHeightIner() {
			Rect frame = new Rect();
			getWindowVisibleDisplayFrame(frame);
			
			return frame.top;
		}
		
		
		public void onRemove(){
			isRemoved = true;
			if(mAnimatorSet != null){
				mAnimatorSet.removeAllListeners();
				mAnimatorSet = null;
			}
			
		}
		
		
		/**
		 * 2014-06-03 maoda.xu@samsung.com added
		 * @param from
		 * @param to
		 */

		private void startAnimation(int from,int to){
			ValueAnimator animation = ValueAnimator.ofObject(new AnimationPath(), from, to);
			animation.addUpdateListener(this);
			animation.addListener(this);
			if(mAnimatorSet != null){
				mAnimatorSet.cancel();
				mAnimatorSet.removeAllListeners();
				mAnimatorSet = null;
			}
			
			mAnimatorSet = new AnimatorSet();
			mAnimatorSet.setDuration(1000);
			mAnimatorSet.setInterpolator(AnimationUtils.loadInterpolator(mContext, android.R.anim.bounce_interpolator));
			mAnimatorSet.playTogether(animation);
			mAnimatorSet.start();
		}
		
	

		@Override
		public void onLongPress() {
			Bundle data = new Bundle();
			if((mGestureOverrideFlags & GESTURE_FLAG_OVERRIDE_LONG_PRESS) != 0 && mGestureCallback != null ){
				data.putInt(GESTURE_KEY_TYPE, GESTURE_TYPE_LONG_PRESS);
				sendMessage(MESSAGE_GESTURE_CALLBACK,data);
			}else{
				Log.i("DADA","onLongPress");
			}
			
		}

		@Override
		public void onShortPress() {
			Bundle data = new Bundle();
			if((mGestureOverrideFlags & GESTURE_FLAG_OVERRIDE_SHORT_PRESS) != 0 && mGestureCallback != null ){
				data.putInt(GESTURE_KEY_TYPE, GESTURE_TYPE_SHORT_PRESS);
				sendMessage(MESSAGE_GESTURE_CALLBACK,data);
			}else{
				Log.i("DADA","onShortPress");
			}
		}
		
		
		/**
		 * Default operation is child 0 Zoom In and Zoom Out
		 * If set {@link GestureCallback}
		 */


		@Override
		public void onZoom(double scale) {
			
			Bundle data = new Bundle();
			data.putDouble(GESTURE_KEY_ZOOM, scale);
			if((mGestureOverrideFlags & GESTURE_FLAG_OVERRIDE_ZOOM) != 0 && mGestureCallback != null ){
				data.putInt(GESTURE_KEY_TYPE, GESTURE_TYPE_ZOOM);
				sendMessage(MESSAGE_GESTURE_CALLBACK,data);
			}else{
				if(mContainer != null){
					sendMessage(MESSAGE_UPDATE_WINDOW,data);
				}
					

			}
			bIsResizing = true;
		}
		
		
		private void sendMessage(int type, Bundle data){
			Message msg = mHandler.obtainMessage(type);
			msg.setData(data);
			mHandler.sendMessage(msg);
		}

		@Override
		public void onDoubleTap() {

			Bundle data = new Bundle();
			if((mGestureOverrideFlags & GESTURE_FLAG_OVERRIDE_DOUBLE_TAP) != 0 && mGestureCallback != null ){
				data.putInt(GESTURE_KEY_TYPE, GESTURE_TYPE_DOUBLE_TAP);
				sendMessage(MESSAGE_GESTURE_CALLBACK,data);
			}else{
				Log.i("DADA","onDoubleTap");
			}
		}

	

		@Override
		public void onAnimationUpdate(ValueAnimator animation) {
			if(isRemoved){
				return;
			}
		    float x = (Float) animation.getAnimatedValue();
            WindowManager.LayoutParams lp = (android.view.WindowManager.LayoutParams) getLayoutParams();
            lp.x = (int) x;
			((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).updateViewLayout(this, lp);
		}

		@Override
		public void onAnimationCancel(Animator arg0) {
			
			
		}

		@Override
		public void onAnimationEnd(Animator arg0) {
	
			if(mOnAttachToSideListener != null){
				mOnAttachToSideListener.onAttachToSide();
			}
		}

		@Override
		public void onAnimationRepeat(Animator arg0) {
			
			
		}

		@Override
		public void onAnimationStart(Animator arg0) {

			
		}
	

	}
	
	private class AnimationPath implements TypeEvaluator<Object>{

		@Override
		public Object evaluate(float fraction, Object startValue,
				Object endValue) {
			float startFloat = ((Number) startValue).floatValue();
			float endFloat = ((Number) endValue).floatValue();
			float x = startFloat + fraction * (endFloat - startFloat);
			return x;
		}
		
	}
}
	
