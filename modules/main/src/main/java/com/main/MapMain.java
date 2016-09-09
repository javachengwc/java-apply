package com.main;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.RandomStringUtils;
/**
 * 根据此例,得到如下结论
 * Map的存储键值对数据，是分组链表结构，它的一个缺点是创建的对象初始大小比较大，容易占内存
 * 
 * 在多线程下操作hashMap,如果hashMap改变比较频繁，且读取也很频繁，可能出现死循环，导致进程cpu占用跑满，
 * 就其原因数据扩容时将数据从旧容器转移到新容器，可能链表节点之间形成了闭环，造成了死循环
 * 在一写线程多读线程操作HashMap，读写线程直接引用共享变量
 * 当写线程clear 或 remove 或 put 变量 ，而读线程正在循环遍历for(s:list)/iterator map的EntitySet变量 会报ConcurrentModificationException异常
 * 当写线程不clear,重新赋予一个new HashMap，如果读线程在赋予前遍历，那它遍历的还是赋予前的副本。
 * 
 * 
 * 在一写线程多读线程操作ConcurrentHashMap，读写线程直接引用共享变量
 * 当写线程clear 或 remove 或 put 变量 ，而读线程正在循环遍历for(s:list)/iterator map的EntitySet变量 莫有问题,
 * 会访问EntitySet里面的每一个元素，访问的EntitySet是随写线程更改及时更改的,即读线程遍历开始后写线程对map的更新操作在读线程中都会反映出来,
 * 写线程clear ,读线程就跳出遍历，写线程put,或remove，读线程的遍历的个数就相应的增加或减少多少个，读线程跟写线程都是直接操作同数据存储，所
 * 以才有别于CopyOnWriteArrayList。
 * 当写线程不clear,重新赋予一个new HashMap，如果读线程在赋予前遍历，那它遍历的还是赋予前的副本。
 *
 * 相比较结论 ConcurrentHashMap 比 HashMap 遍历循环for(s:list)/iterator map的EntitySet变量安全,不会出现ConcurrentModificationException异常
 * ConcurrentHashMap 遍历for(s:list)/iterator map的EntitySet变量，写线程对map的更新操作在读线程中都会反映出来
 * 且ConcurrentHashMap莫有闭环的隐患
 *
 * 多线程下操作Set是线程不安全的，除了可能出现与上面hashMap同样的问题外
 * 如果多个线程中往set添加同一个对象，也有可能出现对象重复存在set中的情况，就会出现set中的元素可能不唯一的情况出现。
 */
public class MapMain {
	
	private static Map<Integer,String> map = new HashMap<Integer,String>();
	private static Map<Integer,String> currentMap = new ConcurrentHashMap<Integer,String>();

	private static boolean first = true;

	public  static int mapHash(int h) {
        // Spread bits to regularize both segment and index locations,
        // using variant of single-word Wang/Jenkins hash.
        h += (h <<  15) ^ 0xffffcd7d;
        h ^= (h >>> 10);
        h += (h <<   3);
        h ^= (h >>>  6);
        h += (h <<   2) + (h << 14);
        return h ^ (h >>> 16);
    }
	
	public static void main(String args[]) {
		
		System.out.println(mapHash(15));
		System.out.println(Integer.toBinaryString(15));
		
        System.out.println((mapHash(15) >>> 28) & 3);
        
		
		
		new Thread("Write-Thread-1") {
			public void run() {
				while (true) {
					initList();
					pause(4000l);
				}
			}
		}.start();

		pause(2000l);

		for (int i = 0; i < 10; i++) {
			new Thread("Read-Thread-" + (i + 1)) {
				public void run() {
					while (true) {
						read();
					}
				}
			}.start();
		}
	}

	public static void initList() {

		System.out.println(Thread.currentThread().getName() + " start clear");
	    //map.clear();
		
		if (!first) {
			//map.put(1002,"22");
			//map.put(2,"222");
			//pause(3000l);
			//currentMap.clear();
			//map = produceMap(800, 0);
			currentMap = produceMap(800, 1);
		}
		System.out.println(Thread.currentThread().getName() + " end clear");
		if (!first) pause(200000l);
		int c = 700;
		if (first) c = 1000;
		for(int a=0;a<c;a++)
		{
			if(a>=501 && !first)
			{
				
			}
			map.put(a,a+"_f_"+RandomStringUtils.randomAlphanumeric(6).toLowerCase());
			currentMap.put(a,a+"_f_"+RandomStringUtils.randomAlphanumeric(6).toLowerCase());
		}		

		first = false;

		System.out.println(Thread.currentThread().getName() + " map size ="
				+ map.size());
		System.out.println(Thread.currentThread().getName() + " map 500 ="
				+ map.get(500));
		System.out.println(Thread.currentThread().getName()
				+ " currentMap size =" + currentMap.size());
		System.out.println(Thread.currentThread().getName()
				+ " currentMap 500 =" + currentMap.get(500));
	}

	public static void read() {
		try {

			Map<Integer,String> map1 = null;
			Map<Integer,String> map2 = null;

			map1 = map;
			map2 = currentMap;

			System.out.println(Thread.currentThread().getName()
					+ " map1 size =" + map1.size());
			System.out.println(Thread.currentThread().getName()
					+ " map2 size =" + map2.size());
			
			pause(1000);

			int i = 0;
			String name="map2";
			
			for (Map.Entry<Integer, String> ssbb : currentMap.entrySet()) {
				if (i == 0) {
					System.out.println(name + " for 循环中...");
					pause(2000l);
					System.out.println(name + " for 循环中.等待2秒结束");
				}
				pause(10l);
				if (i == 200) {
					System.out.println(name + " size=" + currentMap.size());
					System.out.println(name = " for current ele =" + ssbb.getKey() +" "+ ssbb.getValue());
					// System.out.println(name+" s500="+list.get(500));
					System.out.println(name + " for 莫有问题");
					//break;
				}
				i++;
			}
			System.out.println("\n"+name + " len="+ i+" for 循环结束");
			
			// doFor(map1,"map1");

			// doIterator(map2, "map2");

			//doIterator(map1,"map1");

			// doFor(list2,"list2");

			// doIterator(list2,"list2");

		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

	}

	public static void pause(long mt) {
		try {
			Thread.sleep(mt);
		} catch (Exception e) {

		}
	}

	public static void doFor(Map<Integer,String> map, String name) {
		int i = 0;
		for (Map.Entry<Integer, String> ssbb : map.entrySet()) {
			if (i == 0) {
				System.out.println(name + " for 循环中...");
				pause(2000l);
				System.out.println(name + " for 循环中.等待2秒结束");
			}
			pause(10l);
			if (i >= 200) {
				System.out.println(name + " size=" + map.size());
				System.out.println(name = " for current ele =" + ssbb.getKey() +" "+ ssbb.getValue());
				// System.out.println(name+" s500="+list.get(500));
				System.out.println(name + " for 莫有问题");
				break;
			}
			i++;
		}
		System.out.println(name + " for 循环结束");
	}

	public static void doIterator(Map<Integer,String> map, String name) {
		int i = 0;
		Iterator<Map.Entry<Integer, String>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			if (i == 0) {
				System.out.println(name + " iterator 循环中...");
				pause(2000l);
				System.out.println(name + " iterator 循环中.等待2秒结束");
			}
			Map.Entry<Integer, String> k = it.next();
			pause(10l);
			if (i >= 1) {

				System.out.println(name + " size=" + map.size());
				System.out.println(name = " iterator current ele =" + k.getKey() +" "+ k.getValue());
				// System.out.println(name+" s188="+list.get(188));
				System.out.println(name + " iterator 莫有问题");
				break;
			}
			i++;

		}
		System.out.println(name + " iterator 循环结束");
	}

	public static Map<Integer,String> produceMap(int size, int flag) {
		
		Map<Integer,String> result = null;
		if (flag == 0) {
			result = new HashMap<Integer,String>();
		} else {
			result = new ConcurrentHashMap<Integer,String>();
		}
		for (int i = 0; i < size; i++) {
			result.put(i,i + "_"+ RandomStringUtils.randomAlphanumeric(6).toLowerCase());
		}
		return result;
	}
}
