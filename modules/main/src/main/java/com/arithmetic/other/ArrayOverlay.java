package com.arithmetic.other;

/**
 * 找出2个数组中连续重叠元素的最大个数
 * @author cwc
 *
 */
public class ArrayOverlay {
	
	public static int f(int a[],int b[])
	{
		int max=0;
		for(int i=a.length-1;i>=0;i--)
		{
			if(i>b.length-1)
			{
				continue;
			}
			System.out.println("--------i="+i+"--------");
			for(int j=0;j<=a.length-1-i;j++)
			{
				int lenth = i+1;
				int count=0;
				int d=0;
				System.out.println("--------j="+j+"--------");
				for(int k=0;k<=i && d+k<=b.length-1;k++)
				{
					if(a[k+j]==b[d+k])
					{
						count++;
					}else
					{
						count=0;
						d++;
						k=-1;
					}
				}
				if(lenth==count)
				{
					return count;
				}
			}
			
		}
		return max;
	}

	public static void main(String args [])
	{
		int a[] ={2,2,2,4,3,5,7,8,5};
		int b[] ={3,4,2,7,7,2,4,3,5,7,3,8,2};
		System.out.println("-------------");
		int c = f(b,a);
		System.out.println("c="+c);
		
	}
}
