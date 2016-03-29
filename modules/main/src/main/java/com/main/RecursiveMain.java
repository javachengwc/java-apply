package com.main;

/**
 * 
 * @author cwc
 * 递归函数中，递归调用过深报java.lang.StackOverflowError 栈溢出错误
 * 在Xss256k下，一般递归4300左右就会溢出
 * 在Xss1024k下，一般递归24240左右就会溢出
 * 而for循环，循环个10亿都没问题
 */
public class RecursiveMain {

	public static void main(String args[])
	{
		
		System.out.println("----------------");
		
		//System.out.println("f(i)="+f(1000));//没溢出
		
		System.out.println("f(i)="+f(6000));//溢出
		
		//System.out.println("------over----------");
		
		//System.out.println("l(i)="+ l(8900));
		
		//System.out.println("l(i)="+ l(1000000000));
		
		//System.out.println("l(i)="+ l(30000));
		
		for(int i=0;i<200000;i++)
		{
			l(100);
			System.out.println(i);
		}
		
		System.out.println("------over----------");
	}
	
	public static long f(long i)
	{
		if(i<=1)
		{
			return i;
		}
		return i+f(i-1);
	}
	
	public static long l(long i)
	{
		if(i<=1)
		{
			return i;
		}
		long sum=0;
		for(long j=1;j<=i;j++)
		{
			sum +=j;
		}
		return sum;
	}
}
