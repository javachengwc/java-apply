package com.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang.RandomStringUtils;


/**
 * 根据此例,得到如下结论
 * 
 * 在一写线程多读线程操作ArrayList，读写线程直接引用共享变量
 * 当写线程clear 或 add 或 remove 变量 ，而读线程正在循环遍历for(s:list)/iterator变量 会报ConcurrentModificationException异常
 * 当写线程不clear,重新赋予一个new ArrayList，如果读线程在赋予前遍历，那它遍历的还是赋予前的副本。
 * 在读线程中用list.get(pos)去拿数据，很可能爆ArrayIndexOutOfBoundsException 数组越界错误
 * 
 * 在一写线程多读线程操作CopyOnWriteArrayList，读写线程直接引用共享变量
 * 当写线程clear 或 add 或 remove 变量 ，而读线程正在循环遍历for(s:list)/iterator变量 莫有问题,
 * 会访问list里面的每一个元素，访问的list是遍历那时候的list,即读线程遍历开始后写线程对list的更新操作在读线程中都不会反映出来
 * CopyOnWriteArrayList 就如其名一样，每次修改(clear,add,remove)都是赋予一个新的数据，再拿的时候就是一个新的数组值，在只有一个
 * 线程写，其他线程都读的情况下，莫必要用CopyOnWriteArrayList，直接用ArrayList,写线程不修改它，把最终结果赋予给它就行了。
 * 当写线程clear后 重新赋予一个new CopyOnWriteArrayList，如果读线程在clear前遍历，那它遍历的还是clear之前的副本。
 * 在读线程中用list.get(pos)去拿数据，很可能爆ArrayIndexOutOfBoundsException 数组越界错误，
 * 
 * list = Collections.unmodifiableList(list3); 这个只能保证对list的修改clear,add,remove 会报UnsupportedOperationException 异常，
 * 但不会限制到list3，就是说直接对list3的修改都是没问题的，而且对list3的修改会反映到list上去；
 *
 * 相比较结论 CopyOnWriteArrayList 比 ArrayList 遍历循环for(s:list)/iterator安全,不会出现ConcurrentModificationException异常
 */
public class ListMain {
	
	private static List<String> list = new ArrayList<String>();
	private static List<String> currentList = new CopyOnWriteArrayList<String>();
	
	private static boolean first=true;
	
	public static void main(String args [])
	{
		
		List<String> a= produceList(100,0);
		//Arrays.copyOf是一个深度复制
		List<String> b = Arrays.asList(Arrays.copyOf(a.toArray(new String [a.size()]), a.size()));
		System.out.println("a size ="+a.size());
		System.out.println("b size ="+b.size());
		a.clear();
		System.out.println("a size ="+a.size());
		System.out.println("b size ="+b.size());
		
		new Thread("Write-Thread-1")
		{
			public void run()
			{
				while(true)
				{
					initList();
					pause(4000l);
				}
			}
		}.start();
		
		pause(2000l);
		
		for(int i=0;i<10;i++)
		{
			new Thread("Read-Thread-"+(i+1))
			{
				public void run()
				{
					while(true)
					{
					    read();
					}
				}
			}.start();
		}
	}
	
	public static void initList()
	{
	
		 System.out.println(Thread.currentThread().getName()+" start clear" );
		 list.clear();
		// currentList.clear();
		 if(!first)
		 {
			 list =produceList(800,0);
			 System.out.println(Thread.currentThread().getName()+" secnd list 500 ="+list.get(500));
			 //currentList=produceList(800,1);
			 currentList.add("aa");
			 currentList.add("abb");
			 
			 System.out.println(Thread.currentThread().getName()+" secnd currentList 500 ="+currentList.get(500));
		 }
		 System.out.println(Thread.currentThread().getName()+" end clear" );
		 if(!first)	pause(20000l);
		 int c=700;
		 if(first)
		 {
			 c=1000;
		 }
		for(int a=0;a<c;a++)
		{
			if(a>=501 && !first)
			{
				
			}
			
			list.add(a+"_"+RandomStringUtils.randomAlphanumeric(6).toLowerCase());
			currentList.add(a+"_"+RandomStringUtils.randomAlphanumeric(6).toLowerCase());
		}
			
		first=false;
		
		System.out.println(Thread.currentThread().getName()+" list size ="+list.size());
		System.out.println(Thread.currentThread().getName()+" list 500 ="+list.get(500));
		System.out.println(Thread.currentThread().getName()+" currentList size ="+currentList.size());
		System.out.println(Thread.currentThread().getName()+" currentList 500 ="+currentList.get(500));
	}
	
	
	public static void read()
	{
		try{
			
		   List<String> list1 = null;
		   List<String> list2 = null;
		   List<String> list3 =null; 
		   
		   list1 = list;
		   list2 = currentList;
		   list3 = Collections.unmodifiableList(list1);
		   
		   list1.add("gogog");
		   
		   System.out.println(Thread.currentThread().getName()+" list1 size ="+list1.size());
		   System.out.println(Thread.currentThread().getName()+" list2 size ="+list2.size());
		   System.out.println(Thread.currentThread().getName()+" list3 size ="+list3.size());
		  
		   pause(1000);
		   
		   int i=0;
		   String name="currentList";
		   for(String ssbb:currentList)
		   {
			   if(i==0)
			   {
				   System.out.println(name+" for 循环中...");
				   pause(2000l);
				   System.out.println(name+" for 循环中.等待2秒结束");
			   }
			   pause(10l);
			   if(i==200)
			   {
				   System.out.println(name+" size="+currentList.size());
				  // System.out.println(name+" s500="+list.get(500));
				   System.out.println(name+" for 莫有问题");
				  // break;
			   }
			   i++;
		   }
		   System.out.println(name+" "+i +" for 循环结束");
		   
		   
         //  doFor(list,"list1"); 
           
		   //doIterator(list2,"list2");
		   
		  // doIterator(list1,"list1");
		   
		   //doFor(list2,"list2");
		   
		   //doIterator(list2,"list2");
		  
		}catch(Exception e)
		{
			e.printStackTrace(System.out);
		}
		
	}
	
	public static void pause(long mt)
	{
		try{
		    Thread.sleep(mt);
		}catch(Exception e)
		{
			
		}
	}
	
	public static void doFor(List<String> list,String name)
	{
		   int i=0;
		   for(String ssbb:list)
		   {
			   if(i==0)
			   {
				   System.out.println(name+" for 循环中...");
				   pause(2000l);
				   System.out.println(name+" for 循环中.等待2秒结束");
			   }
			   pause(10l);
			   if(i>=200)
			   {
				   System.out.println(name+" size="+list.size());
				  // System.out.println(name+" s500="+list.get(500));
				   System.out.println(name+" for 莫有问题");
				   break;
			   }
			   i++;
		   }
		   System.out.println(name+" for 循环结束");
	}
	
	public static void doIterator(List<String> list,String name)
	{
	   int i=0;
	   Iterator<String> it = list.iterator();
	   while(it.hasNext())
	   {
		   if(i==0)
		   {
			   System.out.println(name+" iterator 循环中...");
			   pause(2000l);
			   System.out.println(name+" iterator 循环中.等待2秒结束");
		   }
		   String k= it.next();
		   pause(10l);
		   if(i>=500)
		   {
			   
			   System.out.println(name+" size="+list.size());
			   System.out.println(name=" current ele ="+k);
			   //  System.out.println(name+" s188="+list.get(188));
			   System.out.println(name+" iterator 莫有问题");
			   break;
		   }
		   i++;
		   
	   }
	   System.out.println(name+" iterator 循环结束");
	}
	
	public static List<String> produceList(int size,int flag)
	{
		List<String> result =null;
		if(flag==0)
		{
			result = new ArrayList<String>();
		}else
		{
			result = new CopyOnWriteArrayList<String>();
		}
		for(int i=0;i<size;i++)
		{
			result.add(i+"_"+RandomStringUtils.randomAlphanumeric(6).toLowerCase());
		}
		return result;
	}
}
