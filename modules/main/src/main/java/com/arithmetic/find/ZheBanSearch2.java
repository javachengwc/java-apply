package com.arithmetic.find;

/**
 * 折半查找
 * 递归，循环实现
 */
public class ZheBanSearch2 {

	private int rCount = 0;
	private int lCount = 0;
	
	public int getrCount() {
		return rCount;
	}
    
	public int getlCount() {
		return lCount;
	}

	public static Integer find(int [] data,int target) {
		if(data==null || data.length<=0) {
			return null;
		}
		int len = data.length;
		int begin = 0;
		int end = len-1;
		while(begin<=end) {
			int mid = begin+(end-begin)/2;
			System.out.println("mid= "+mid);
			if(target==data[mid]) {
				return mid;
			}
			if(target<data[mid]) {
				end = mid-1;
			}
			if(target>data[mid]) {
				begin =mid+1;
			}
			System.out.println(begin+" "+end);
		}
		return null;
	}

	public static Integer find2(int [] data,int target) {
		if(data==null || data.length<=0) {
			return null;
		}
		return _innerFind(data,0,data.length-1,target);
	}

	public static Integer _innerFind(int [] data,int begin ,int end ,int target) {
		int mid = begin+(end-begin)/2;
		System.out.println(begin+" "+end+" "+mid);
		if(target==data[mid]) {
			return mid;
		}
		if(mid<=begin || mid>=end) {
			return null;
		}
		if(target<data[mid]) {
			end = mid-1;
			return _innerFind(data,begin,end,target);
		}
		if(target>data[mid]) {
			begin =mid+1;
			return _innerFind(data,begin,end,target);
		}
		return null;
	}

	
	public static void main(String args [])
	{
		ZheBanSearch2 bs=new ZheBanSearch2();  
        
        int[] sortedData={1,2,3,4,5,6,6,7,8,8,9,10};  
        int findValue=9;  
        int length=sortedData.length;  
          
        int pos=bs.find2(sortedData, findValue);
        System.out.println("Recursice:"+findValue+" found in pos "+pos+";count:"+bs.getrCount());

        int pos2=bs.find(sortedData, findValue);
        System.out.println("Loop:"+findValue+" found in pos "+pos2+";count:"+bs.getlCount());
	}

}
