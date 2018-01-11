package com.dadatoolkit.java;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.util.Log;

/**
 * 
 * @author maoda.xu@samsung.com
 * 
 */
public class ReflectUtil {
	
	/**
	 * 
	 * @param className
	 */
	public static void printAllDeclaredMethods(String className){
		try {
			Class<?> clazz = Class.forName(className);
			Method[] methods = clazz.getDeclaredMethods();
			
			for(Method m:methods){
				StringBuilder sb = new StringBuilder();
				Class<?> returnType = m.getReturnType();
				Class<?>[] types = m.getParameterTypes();
				if(types != null){
					for(Class<?> t:types){
						sb.append(t.getName()).append(",");
					}
				}
				
				Log.e("DADA", returnType.getName()+" "+ 
				m.getName()+"("+sb.toString()+")");
		
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 
	 * @param className
	 * @param methodName
	 * @param parameterTypes
	 * @return
	 */
	public static Method getMethod(String className, String methodName,
			Class<?>... parameterTypes) {
		Method m = null;
		try {
			Class<?> clazz = Class.forName(className);
			m = clazz.getMethod(methodName, parameterTypes);
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		} catch (NoSuchMethodException e) {

			e.printStackTrace();
		}
		return m;
	}
	/**
	 * 
	 * @param className
	 * @param methodName
	 * @param parameterTypes
	 * @return
	 */
	public static Method getDeclaredMethod(String className, String methodName,
			Class<?>... parameterTypes) {
		Method m = null;
		try {
			Class<?> clazz = Class.forName(className);
			m = clazz.getDeclaredMethod(methodName, parameterTypes);
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		} catch (NoSuchMethodException e) {

			e.printStackTrace();
		}
		return m;
	}
	
	/**
	 * 
	 * @param className
	 * @param methodName
	 * @param parameterTypes
	 * @return
	 */
	public static Method getAccessibleDeclaredMethod(String className, String methodName,
			Class<?>... parameterTypes) {
		Method m = null;
		try {
			Class<?> clazz = Class.forName(className);
			m = clazz.getDeclaredMethod(methodName, parameterTypes);
			if(!m.isAccessible()){
				m.setAccessible(true);
			}
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		} catch (NoSuchMethodException e) {

			e.printStackTrace();
		}
		return m;
	}

	/**
	 * 
	 * @param className
	 * @param parameterTypes
	 * @return
	 */
	public static Constructor<?> getConstructor(String className,
			Class<?>... parameterTypes) {
		try {
			Class<?> clazz = Class.forName(className);
			return getConstructor(clazz, parameterTypes);
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 
	 * @param clazz
	 * @param parameterTypes
	 * @return
	 */
	public static Constructor<?> getConstructor(Class<?> clazz,
			Class<?>... parameterTypes) {
		Constructor<?> c = null;
		try {
			c = clazz.getConstructor(parameterTypes);
		} catch (NoSuchMethodException e) {

			e.printStackTrace();
		}

		return c;
	}

	/**
	 * 
	 * @param c
	 * @param args
	 * @return
	 */
	public static Object getInstance(Constructor<?> c, Object... args) {
		Object o = null;
		if (c != null) {
			try {
				o = c.newInstance(args);

			} catch (IllegalArgumentException e) {

				e.printStackTrace();
			} catch (InstantiationException e) {

				e.printStackTrace();
			} catch (IllegalAccessException e) {

				e.printStackTrace();
			} catch (InvocationTargetException e) {

				e.printStackTrace();
			}
		}
		return o;

	}

	
	/**
	 * 
	 * @param o
	 * @param m
	 * @param args
	 * @return
	 */
	public static Object inVoke(Object o, Method m, Object... args) {
		if (m != null) {
			try {
				return m.invoke(o, args);
			} catch (IllegalArgumentException e) {

				e.printStackTrace();
			} catch (IllegalAccessException e) {

				e.printStackTrace();
			} catch (InvocationTargetException e) {

				e.printStackTrace();
			}
		}

		return null;
	}

	/**
	 * 
	 * @param className
	 * @param fieldName
	 * @return
	 */
	public static int getStaticIntField(String className, String fieldName) {
		int ret = 0;
		try {
			Class<?> clazz = Class.forName(className);
			Field f = clazz.getField(fieldName);
			ret = f.getInt(null);
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		} catch (NoSuchFieldException e) {

			e.printStackTrace();
		} catch (IllegalArgumentException e) {

			e.printStackTrace();
		} catch (IllegalAccessException e) {

			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * 
	 * @param className
	 * @param fieldName
	 * @return
	 */
	public static Object getStaticClassField(String className, String fieldName) {
		Object ret = null;
		try {
			Class<?> clazz = Class.forName(className);
			Field f = clazz.getField(fieldName);
			ret = f.get(null);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {

			e.printStackTrace();
		} catch (IllegalArgumentException e) {

			e.printStackTrace();
		} catch (IllegalAccessException e) {

			e.printStackTrace();
		}
		return ret;
	}
	
	
	
	/**
	 * 
	 * @param className
	 * @return
	 */
	public static Field[] getDeclaredFields(String className) {
		Field[] ret = null;
		try {
			Class<?> clazz = Class.forName(className);
			ret = clazz.getDeclaredFields();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * 
	 * @param className
	 * @param fieldName
	 * @return
	 */
	public static Field getDeclaredField(String className,String fieldName) {
		Field ret = null;
		try {
			Class<?> clazz = Class.forName(className);
			ret = clazz.getDeclaredField(fieldName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * 
	 * @param className
	 * @param fieldName
	 * @return
	 */
	public static Field getAccessibleDeclaredField(String className,String fieldName) {
		Field ret = null;
		try {
			Class<?> clazz = Class.forName(className);
			ret = clazz.getDeclaredField(fieldName);
			if(!ret.isAccessible()){
				ret.setAccessible(true);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * 
	 * @param className
	 * @param fieldName
	 * @return
	 */
	public static Field getField(String className,String fieldName){
		Field ret = null;
		try {
			Class<?> clazz = Class.forName(className);
			ret = clazz.getField(fieldName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	
	/**
	 * 
	 * @param outName
	 * @param innerName
	 * @return
	 */
	public static Field[] getInnerDeclaredFields(String outName,String innerName) {
		return getDeclaredFields(outName+"$"+innerName);
	}
	
	
	/**
	 * 
	 * @param outName
	 * @param innerName
	 * @param fieldName
	 * @return
	 */
	public static Object getInnerStaticClassField(String outName,String innerName, String fieldName) {
		return getStaticClassField(outName+"$"+innerName,fieldName);
	}
	
	
	/**
	 * 
	 * @param className
	 * @param fieldName
	 * @param o
	 * @param value
	 */
	public static void setDeclaredFieldBoolean(String className,String fieldName,Object o,boolean value) {
		try {
			Class<?> clazz = Class.forName(className);
			Field f = clazz.getDeclaredField(fieldName);
			if(!f.isAccessible()){
				f.setAccessible(true);
				f.setBoolean(o, value);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param className
	 * @param fieldName
	 * @param o
	 * @param value
	 */
	public static void setDeclaredFieldObject(String className,String fieldName,Object o,Object value) {
		try {
			Class<?> clazz = Class.forName(className);
			Field f = clazz.getDeclaredField(fieldName);
			if(!f.isAccessible()){
				f.setAccessible(true);
				f.set(o, value);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param className
	 * @param fieldName
	 * @param o
	 * @param value
	 */
	public static void setDeclaredFieldByte(String className,String fieldName,Object o,byte value) {
		try {
			Class<?> clazz = Class.forName(className);
			Field f = clazz.getDeclaredField(fieldName);
			if(!f.isAccessible()){
				f.setAccessible(true);
				f.setByte(o, value);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param className
	 * @param fieldName
	 * @param o
	 * @param value
	 */
	public static void setDeclaredFieldChar(String className,String fieldName,Object o,char value) {
		try {
			Class<?> clazz = Class.forName(className);
			Field f = clazz.getDeclaredField(fieldName);
			if(!f.isAccessible()){
				f.setAccessible(true);
				f.setChar(o, value);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 
	 * @param className
	 * @param fieldName
	 * @param o
	 * @param value
	 */
	public static void setDeclaredFieldDouble(String className,String fieldName,Object o,double value) {
		try {
			Class<?> clazz = Class.forName(className);
			Field f = clazz.getDeclaredField(fieldName);
			if(!f.isAccessible()){
				f.setAccessible(true);
				f.setDouble(o, value);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
