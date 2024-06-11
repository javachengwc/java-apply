package com.main;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.CopyOnWriteArrayList;

public class MemoryMain {

	public static Instrumentation inst;

	public static void premain(String agentArgs, Instrumentation inst) {
		MemoryMain.inst = inst;
	}

	public static void main(String args[]) {

		System.out.println("size of object ="+sizeOf(new Object()));
		
		System.out.println("size of string ="+sizeOf("a"));
		
		System.out.println("size of string ="+sizeOf(new String("a")));
		
		System.out.println("size of string ="+sizeOf(new String("abcdefg")));
		
		System.out.println("size of Integer ="+sizeOf(2));
		
		System.out.println("size of Integer ="+sizeOf(new Integer(2)));
		
		
		printMemoryInfo();

		buildMap(Integer.parseInt(args[0]));

		buildGroup(Integer.parseInt(args[1]));
		
		buildList(Integer.parseInt(args[2]));
		
		buildCopyOnWriteList(Integer.parseInt(args[3]));

		printMemoryInfo();
		
		while(true)
		{}

	}

	public static void printMemoryInfo() {
		long total = Runtime.getRuntime().totalMemory();
		long free = Runtime.getRuntime().freeMemory();
		System.out.println("total =" + total);
		System.out.println("free = " + free);
		System.out.println("useed = " + (total - free));

	}

	public static void buildGroup(int size) {
		// size=200000 实际4m 理论 4*200000 =800k
		Integer a[] = new Integer[size];

		for (int i = 0; i < size; i++) {
			a[i] = i;
		}
		System.out.println("size of group ="+ sizeOf(a));
	}

	public static void buildMap(int size) {

		// size=100000 实际7m 理论 (4+4)*100000 =800km
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();

		for (int i = 0; i < size; i++) {
			map.put(i, i);
			
		}
		System.out.println("size of map ="+ sizeOf(map));
		for(Map.Entry<Integer, Integer> a:map.entrySet())
		{
			System.out.println("size of map key ="+sizeOf(a));
			break;
		}
	}
	
	public static void buildList(int size)
	{
		List<Integer> list = new ArrayList<Integer>();

		for (int i = 0; i < size; i++) {
			list.add(i);
		}
		System.out.println("size of list ="+ sizeOf(list));
	}
	
	public static void buildCopyOnWriteList(int size)
	{
		List<Integer> list = new CopyOnWriteArrayList<Integer>();

		for (int i = 0; i < size; i++) {
			list.add(i);
		}
		System.out.println("size of copyOnWriteList ="+ sizeOf(list));
	}

	public static long sizeOf(Object o) {
      assert inst != null;
      Map<Object, Object> visited = new IdentityHashMap<Object, Object>();
      Stack<Object> visiting = new Stack<Object>();
      visiting.add(o);
      long size = 0;
      while (!visiting.isEmpty()) {
          size += analysis(visiting, visited);
      }
      return size;
	}
	
	protected static long analysis(Stack<Object> visiting, Map<Object, Object> visited) {
	      Object o = visiting.pop();
	      if (skip(o, visited)) {
	          return 0;
	      }
	      visited.put(o, null);
	      // array.
	      if (o.getClass().isArray() && !o.getClass().getComponentType().isPrimitive()) {
	          if (o.getClass().getName().length() != 2) {
	              for (int i = 0; i < Array.getLength(o); i++) {
	                  visiting.add(Array.get(o, i));
	              }
	          }
	      }
	      // object.
	      else {
	          Class<?> clazz = o.getClass();
	          while (clazz != null) {
	              Field[] fields = clazz.getDeclaredFields();
	              for (Field field : fields) {
	                  if (Modifier.isStatic(field.getModifiers())) {
	                      continue;
	                  }
	                  if (field.getType().isPrimitive()) {
	                      continue;
	                  }
	                  field.setAccessible(true);
	                  try {
	                      visiting.add(field.get(o));
	                  } catch (Exception e) {
	                      assert false;
	                  }
	              }
	              clazz = clazz.getSuperclass();
	          }
	      }
	     return inst.getObjectSize(o);
	 }

	
	protected static boolean skip(Object o, Map<Object, Object> visited) {
	     if (o instanceof String) {
	         if (o == ((String) o).intern()) {
	             return true;
	         }
	     }
	     return o == null || visited.containsKey(o);
	 }
	
}
