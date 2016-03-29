package com.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;

public class DeepCopyUtil {

	public static <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream    out     = new ObjectOutputStream(byteOut);
		out.writeObject(src);
		
		ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
		ObjectInputStream    in     = new ObjectInputStream(byteIn);
		List<T> dest = (List<T>)in.readObject();
		return dest;
	}

	public static <K,T> Map<K,T> deepCopy(Map<K,T> src) throws IOException, ClassNotFoundException {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream    out     = new ObjectOutputStream(byteOut);
		out.writeObject(src);
		
		ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
		ObjectInputStream    in     = new ObjectInputStream(byteIn);
		Map<K,T> dest = (Map<K,T>)in.readObject();
		return dest;
	}
}
