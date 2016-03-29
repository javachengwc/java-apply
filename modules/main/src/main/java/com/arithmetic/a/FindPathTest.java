package com.arithmetic.a;

import java.util.List;

public class FindPathTest {
	
	public static void main(String args [])
	{
		int data[][] = prepareMap();
		Point start = new Point(0,0);
		Point end = new Point(2,5);
		List<Point> list = AFindPath.findPath(data, start, end);
		
		System.out.println("list:"+list);
		
	}

	public static int [][] prepareMap()
	{
		int data[][] =new int [8][8];
		for(int i=0;i<8;i++)
		{
			for(int j=0;j<8;j++)
			{
				data[i][j]=0;
				if(j==4)
				{
					if(i>=1 && i<=3)
					{
						data[i][j]=1;
					}
				}
			}
		}
		return data;
	}
	
}
