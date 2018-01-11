package com.dadatoolkit.android.app;
import android.view.InputEvent;
import com.dadatoolkit.java.ReflectUtil;
/**
 * 
 * @author maoda.xu@samsung.com
 *
 */
public class InputManager {
	private final  Object mInputManager;
	
	private static final String INPUT_MANAGER_CLASS="android.hardware.input.InputManager";
	
	private static InputManager mInstance;
	
	public static InputManager getInstance(){
		if(mInstance == null){
			mInstance = new InputManager();
		}
		return mInstance;
	}
		
	
	private InputManager(){
		mInputManager=ReflectUtil.inVoke(null, ReflectUtil.getMethod(INPUT_MANAGER_CLASS,"getInstance"));
	}
	
	
	public void injectInputEvent(InputEvent inputEvent,int mode){
		ReflectUtil.inVoke(mInputManager,ReflectUtil.getMethod(INPUT_MANAGER_CLASS, "injectInputEvent", InputEvent.class,int.class),inputEvent,mode);
	}

}
