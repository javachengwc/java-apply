package com.arithmetic.other;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
/**
 * 模拟根据页面每行容下的图片个数，对图片列表进行排列，在尽量保持原来图片顺序的基础上，对图片顺序进行调整，尽量做到页面中在前面的行的图片排满。
 * @author cwc
 *
 */
public class PlaceArith
{
	
	public static List<String> sort(Map<String,Integer> data,int lineCount)
	{
		List<Integer> specList=new ArrayList<Integer>();
		List<String> list=new ArrayList<String>();
		for(Entry<String,Integer> e:data.entrySet())
		{
			specList.add(e.getValue());
			list.add(e.getKey());
		}
		List<String> result =new ArrayList<String>();
		//当前读到的位置
		int currentPos=0;
		int counter=0;
	    for(int i=0;i<data.size();i++)
	    {
//	    	System.out.println("次数:"+i);
//	    	System.out.println("currentPos:"+currentPos);
//	    	System.out.println("c:"+counter);

	    	int spec=specList.get(currentPos);
		    counter +=spec;
		    if(counter<=lineCount)
		    {
		    	String value=list.get(currentPos);
		    	if(!result.contains(value))
		    	{
			    	result.add(value);
			    	
			    	currentPos++;
		    	}else
		    	{
		    		counter-=spec;
			    	spec=selectEle(result,list,specList,currentPos+1,counter,lineCount);
			    	counter+=spec;
			    	if(spec==0)
			    	{
			    		for(int j=currentPos;j<data.size();j++)
			    		{
			    			if(result.contains(list.get(j)))
			    				continue;
			    			else
			    			{
			    				result.add(list.get(j));
			    				currentPos=j;
			    				spec=specList.get(currentPos);
			    				counter+=spec;
			    				break;
			    			}
			    		}
			    	}
		    	}
		    	
		    }else
		    {
		    	counter-=spec;
		    	spec=selectEle(result,list,specList,currentPos+1,counter,lineCount);
		    	counter+=spec;
		    	if(spec==0)
		    	{
		    		for(int j=currentPos;j<data.size();j++)
		    		{
		    			if(result.contains(list.get(j)))
		    				continue;
		    			else
		    			{
		    				result.add(list.get(j));
		    				currentPos=j;
		    				spec=specList.get(currentPos);
		    				counter+=spec;
		    				break;
		    			}
		    		}
		    	}
		    	
		    }
		    System.out.println(counter+":"+lineCount);
		    if(counter>lineCount)
	    	{
	    		counter=spec;
	    		System.out.println("new counter:"+counter);
	    		System.out.println("不能排满的点位置:"+(i-1));
	    	}
	    	if(counter==lineCount)
	    	{
	    		counter=0;
	    	}	
	    }
	    return result;
	}
	
	public static int selectEle(List<String> result,List<String> list,List<Integer> specList,int pos,int counter,int lineCount)
	{
	
    	int spec=specList.get(pos);
	    counter +=spec;
	    if(counter<=lineCount)
	    {
	    	String value=list.get(pos);
	    	if(!result.contains(value))
	    	{
		    	result.add(value);
		    	return spec;
	    	}else
	    	{
	    		counter -=spec;
	    		if(pos+1<list.size())
	    		{
	    		    return selectEle(result,list,specList,pos+1,counter,lineCount);
	    		}
	    		else
	    		{
	    			return 0;
	    		}
	    	}
	    }else
	    {
	    	counter -=spec;
	    	if(pos+1<list.size())
    		{
	    	   return selectEle(result,list,specList,pos+1,counter,lineCount);
    		}
    		else
    		{
    			return 0;
    		}
	    }
	    
	}
	
	public static String getRankSongRoomListKey(int offset, int pageSize){
		return new StringBuilder()
			.append("rank:songroom:list")
			.append(":o:")
			.append(offset)
			.append(":p:")
			.append(pageSize).toString();
	}
	
	public static void main(String args[])
	{
		int offset = 0;
		int pageSize = 10 << 10;
		System.out.println(getRankSongRoomListKey(offset,pageSize));
		Map<String,Integer> data=new LinkedHashMap<String,Integer>();
		int lineCount=5;
		for(int i=0;i<6;i++)
		{
			data.put("a"+i, 2);
			data.put("b"+i, 2);
			data.put("c"+i, 2);
			data.put("d"+i, 2);
			data.put("e"+i, 2);
			data.put("f"+i, 2);
			data.put("g"+i, 2);
			data.put("h"+i, 2);
			data.put("i"+i, 2);
			data.put("j"+i, 2);
		}

		List<String> result = PlaceArith.sort(data, lineCount);
		int count=0;
		for(String s:result)
		{

			count+=data.get(s);
			if(count<lineCount)
			    System.out.print(s+",");
			if(count==lineCount)
			{
				System.out.println(s);
				count=0;
			}
			if(count>lineCount)
			{
				System.out.print("\n"+s);
				count=data.get(s);
			}
		}

		System.out.println(System.currentTimeMillis());
		long a=System.currentTimeMillis();
		for(int i=0;i<1000;i++)
		{
			for(int j=0;j<1000;j++)
			{
				System.out.println("ooooo");
			}
		}

		System.out.println(System.currentTimeMillis());
		long b=System.currentTimeMillis();
		System.out.println("花费:"+(b-a));
		int aa=_hashIndex("b");
		System.out.println();
		System.out.println(aa);
	}
	
	private static int _hashIndex(String id)
	{ //sdb
		long hash = 5381;
		for(int i = 0; i < id.length(); i++)
		{
			hash = ((hash << 5) + hash);
			System.out.println(hash);
		    hash += id.charAt(i);
		    System.out.println(hash);
			hash = hash & 0xFFFFFFFF;
			 System.out.println(hash);
		}
		
		int index = (int)hash % 5;
		index = Math.abs(index);
		
		return index;
	}
}
