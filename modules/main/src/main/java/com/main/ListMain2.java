package com.main;
import java.util.*;


public class ListMain2 {

	public static void main(String args [])
	{
        checkListEleAbleNull();

        checkMixList();

        testRemove();

        checkClear();

        //listSetSub();

		//checkSubList();

	}

    /**subList相关**/
    public static void checkListEleAbleNull() {
        List<String> listA = new ArrayList<String>();

        String abc = new String("abc");
        String a = "a";
        String b = "b";
        String c = "c";
        String d=null;

        listA.add(abc);
        listA.add(null);
        listA.add(a);
        listA.add(b);
        listA.add(c);
        listA.add(d);

        System.out.println(listA.size());
        System.out.println(" list second ele is "+ listA.get(1));
    }

	/**subList相关**/
	public static void checkSubList()
	{
		List<String> listA = new ArrayList<String>();
    
    	String abc= new String("abc");
    	String a = "a";
    	String b="b";
    	String c="c";
    	
    	listA.add(abc);
    	listA.add(a);
    	listA.add(b);
    	listA.add(c);
    	
    	List<String> listB= listA.subList(0, 1);
    	printList(listB);
    	listB= listA.subList(2,3);
    	System.out.println("-------------");
    	printList(listB);
    	
    	listB= listA.subList(3,4);
    	System.out.println("-------------");
    	printList(listB);

        checkSubList2(listA);

	}

    public static void testRemove()
    {
        List<Double> list = new ArrayList<Double>();
        list.add(1d);
        list.add(2d);
        list.add(1d);
        System.out.println(list.size());
        list.remove(1d);
        //list.remove("1");
        System.out.println(list.size());

    }

	
	/**clear相关**/
	public static void checkClear()
	{
		List<String> listA = new ArrayList<String>();
    	List<String> listB = new ArrayList<String>();
        List<String> listC = new ArrayList<String>();
        List<String> listD = new ArrayList<String>();
    	String abc= new String("abc");
    	listB.add(abc);
    	listA.addAll(listB);
        listB.clear();
        System.out.println(listA.size()+" "+listB.size());
        listC=listA;
        listD =new ArrayList<String>(listA);
        listA.clear();
        System.out.println(listA.size()+" "+listB.size()+" "+listC.size()+" "+listD.size());
	}

    public static void checkSubList2(List<String> all )
    {
        int total = all.size();
        int count= total/2;
        if(total%2>0)
        {
            count+=1;
        }
        for(int i=0;i<count;i++)
        {
            int start =i*2;
            int end = (i+1)*2;
            if((i+1)==count)
            {
                //最后一次
                end=total;
            }
            System.out.println("-----------分批处理数据:start=" + start + ",end=" + end + "----------------");
            List<String> perList = all.subList(start,end);
            perDeal(perList);
        }
    }

    public static void perDeal(List<String> list)
    {

        Iterator<String> it = list.iterator();
        while(it.hasNext())
        {
            String conf =it.next();
            if(conf=="a")
            {
                //此处如果执行了it.remove会影响父list
               // it.remove();
            }
        }
    }

	public static void printList(List<?> list)
	{
		if(list==null)
		{
			return;
		}
		for(Object obj:list)
		{
			System.out.println(obj.toString());
		}
	}

    public static void listSetSub()
    {
        Set<Integer> ids= new HashSet<Integer>();
        for(int i=0;i<10;i++)
        {
            ids.add(i);
        }

        List<Integer> idList =Arrays.asList(ids.toArray( new Integer [ids.size()]));
        ids.clear();

        List<Integer> subList1 = idList.subList(0,5);
        for(Integer p:subList1)
        {
            System.out.println("--------------p1:"+p);
        }

        List<Integer> subList2 = idList.subList(5,10);
        for(Integer p:subList2)
        {
            System.out.println("--------------p2:"+p);
        }
    }

    public static void checkMixList()
    {
        List<String> a=new ArrayList<String>();
        for(int i=90000;i<100000;i++)
        {
            a.add("a"+i);
        }
        List<String> b=new ArrayList<String>();
        for(int i=50000;i<150000;i++)
        {
            b.add("a"+i);
        }
        long now =System.currentTimeMillis();
        System.out.println(now);
        List<String> rtList =getMix(a,b);
        System.out.println("cost:"+(System.currentTimeMillis()-now)/1000);
        int rtCnt =rtList==null?0:rtList.size();
        System.out.println(rtCnt);
    }

    public static  List<String> getMix(List<String>  a,List<String> b)
    {
        int cnt1=a==null?0:a.size();
        int cnt2=b==null?0:b.size();
        if(cnt1<=0 ||cnt2<=0)
        {
            return Collections.EMPTY_LIST;
        }
        Set<String> rtSet =new HashSet<String>();
        int cnt=0;
        for(String per:a)
        {
            cnt++;
            if(b.contains(per))
            {
                rtSet.add(per);
            }
            if(cnt%1000==0)
            {
                System.out.println("对比了:"+cnt);
            }
        }
        List<String> mixList= new ArrayList<String>(rtSet);
        rtSet.clear();
        return mixList;
    }
}
