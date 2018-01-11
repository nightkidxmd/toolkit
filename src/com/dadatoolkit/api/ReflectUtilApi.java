package com.dadatoolkit.api;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.dadatoolkit.java.ReflectUtil;
/**
 * 
 * @author maoda.xu@samsung.com
 *
 */
public class ReflectUtilApi {
	
	public static Method getMethod(String className, String methodName,
			Class<?>... parameterTypes) {
		return ReflectUtil.getMethod(className, methodName, parameterTypes);
	}

	public static Constructor<?> getConstructor(String className,
			Class<?>... parameterTypes) {
		return ReflectUtil.getConstructor(className, parameterTypes);
	}

	public static Constructor<?> getConstructor(Class<?> clazz,
			Class<?>... parameterTypes) {
		return ReflectUtil.getConstructor(clazz, parameterTypes);
	}

	public static Object getInstance(Constructor<?> c, Object... args) {
		return ReflectUtil.getInstance(c, args);
	}

	public static Object inVoke(Object o, Method m, Object... args) {
		return ReflectUtil.inVoke(o, m, args);
	}

	public static int getStaticIntField(String className, String fieldName) {
		return ReflectUtil.getStaticIntField(className, fieldName);
	}

	public static Object getStaticClassField(String className, String fieldName) {
		return ReflectUtil.getStaticClassField(className, fieldName);
	}
	
	
	public static Field[] getDeclaredFields(String className) {
		return ReflectUtil.getDeclaredFields(className);
	}
	
	public static Field[] getInnerDeclaredFields(String outName,String innerName) {
		return ReflectUtil.getInnerDeclaredFields(outName, innerName);
	}
	
	public static Object getInnerStaticClassField(String outName,String innerName, String fieldName) {
		return ReflectUtil.getInnerStaticClassField(outName, innerName, fieldName); 
	}
}
