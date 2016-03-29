package com.util.asm;

import java.io.IOException;

import java.io.InputStream;
import java.io.PrintWriter;

import org.objectweb.asm.Type;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.ASMifierClassVisitor;
import org.objectweb.asm.util.TraceClassVisitor;

public class AsmClassUtil {
	
	public static String getInternalPackageName(Class<?> clazz){
		String internalName = Type.getInternalName(clazz);
		int endIndex = internalName.lastIndexOf("/") + 1;
		return internalName.substring(0, endIndex);
	}
	
	public static String getInternalClassName(String className){
		String internalName = className.replaceAll("\\.", "/");
		return internalName;
	}
	
	public static String getShortName(Class<?> clazz){
		String className = clazz.getName();
		
		return getShortClassName(className);
	}
	public static String getShortClassName(String className){
		int index = className.lastIndexOf(".");
		if(index == -1){
			return className;
		}
		return className.substring(index + 1);
	}
	
	public static String getShortName(String internalName){
		int index = internalName.lastIndexOf("/");
		if(index == -1){
			return internalName;
		}
		return internalName.substring(index + 1);
	}
	
	public static String getByteCodeName(Class<?> clazz){
		return Type.getInternalName(clazz);
	}
	
	public static String formatVariableName(String name){
		String firstChar = String.valueOf(name.charAt(0));
		return firstChar.toLowerCase() + name.substring(1);
		
	}
	
	public static boolean isConstructor(String name){
		return name.equals("<init>");
	}
	
	public static void mifer(Class<?> clazz)throws Exception{
		ASMifierClassVisitor cv = new ASMifierClassVisitor(new PrintWriter(System.out));
		ClassReader cr = new ClassReader(clazz.getName());
		cr.accept(cv, ClassReader.SKIP_DEBUG);
	}
	
	public static void mifer(InputStream is)throws Exception{
		ASMifierClassVisitor cv = new ASMifierClassVisitor(new PrintWriter(System.out));
		ClassReader cr = new ClassReader(is);
		cr.accept(cv, ClassReader.SKIP_DEBUG);
	}
	
	public static void trace(Class<?> clazz)throws Exception{
		TraceClassVisitor cv = new TraceClassVisitor(new PrintWriter(System.out));
		ClassReader cr = new ClassReader(clazz.getName());
		cr.accept(cv, ClassReader.SKIP_DEBUG);
	}
	
	public static void trace(InputStream is)throws Exception{
		TraceClassVisitor cv = new TraceClassVisitor(new PrintWriter(System.out));
		ClassReader cr = new ClassReader(is);
		cr.accept(cv, ClassReader.SKIP_DEBUG);
	}
	
	public static byte[] getByteCode(Class<?> clazz){
		ClassReader cr = null;
		try {
			cr = new ClassReader(clazz.getName());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cr.b;
	}
	
	public static byte[] getByteCode(InputStream is){
		ClassReader cr = null;
		try {
			cr = new ClassReader(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cr.b;
	}
}
