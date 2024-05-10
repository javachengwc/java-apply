package com.arithmetic.array;

/**
 * 找出2个数组中连续重叠元素的最大个数
 * @author cwc
 *
 */
public class ArrayOverlay {
	
	public static int f(int a[],int b[])
	{
		int max=0;
		for(int i=a.length;i>0;i--)
		{
			System.out.println("--------iiiiii="+i+"--------");
			if(i>b.length)
			{
				continue;
			}
			System.out.println("--------i="+i+"--------");
			int lenth = i;
			int index=0;
			for(int j=0;j<=a.length-i;j++)
			{
				int count=0;
				int d=0;
				System.out.println("--------j="+j+"--------");
				for(int k=0;k<i && d+k<=b.length-1;k++)
				{
					if(a[k+j]==b[d+k])
					{
						//System.out.println("--------k="+k+",j="+j+",d="+d+"--------");
						count++;
						index= k+j;
					}
					else
					{
						count=0;
						d++;
						k=-1;
					}
				}
				if(lenth==count)
				{
					System.out.println("----index---"+index);
					return count;
				}
			}
			
		}
		return max;
	}

	public static void main(String args [])
	{
		int a[] ={2,2,2,4,3,6,7,8,5};
		int b[] ={3,4,2,7,7,2,4,3,5,7,3,8,2};
//		int a[] ={1,2,3};
//		int b[] ={1,2,3};
		System.out.println("-------------");
		int c = f(a,b);
		System.out.println("c="+c);
		
	}
}
