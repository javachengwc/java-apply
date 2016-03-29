package com.arithmetic.a;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * A+寻找最短路径算法
 * @author cwc
 *
 */
public class AFindPath {
	
	
	/**
	 * 查找从起点到终点的最佳路径
	 * @param specMap
	 * @param start
	 * @param end
	 * @return
	 */
	public static List<Point> findPath(int [][] specMap,Point start,Point end)
	{
		
		if(specMap ==null || start==null || end ==null)
		{
			return null;
		}
		int rowNum=specMap.length;
		int colNum=0;
		for(int i=0;i<rowNum;i++)
		{
			int col = specMap[i].length;
			if(col>colNum)
			{
				colNum=col;
			}
		}
		if(start.getX()>=rowNum || end.getX()>= rowNum)
		{
			return null;
		}
		if(start.getY()>=colNum || end.getY()>= colNum)
		{
			return null;
		}
		//////////////
		List<Point> openList =  new ArrayList<Point>();
		List<Point> closeList = new ArrayList<Point>();
		openList.add(start);
		
		Point find = find(specMap,openList ,closeList,start,end);
		
		if(find==null)
		{
			return null;
		}
		List<Point> rt= new ArrayList<Point>();
		rt.add(find);
		while(find.getPrePoint()!=null)
		{
			rt.add(find.getPrePoint());
			find = find.getPrePoint();
		}
		Collections.reverse(rt);
		return rt;
	}
	
	/**
	 * 真正的查询
	 * @param specMap
	 * @param openList
	 * @param closeList
	 * @param cur
	 * @param end
	 * @return
	 */
	public static Point find(int [][] specMap,List<Point> openList ,List<Point> closeList,Point cur,Point end)
	{
		List<Point> around = getAroundAblePoint(specMap,cur);
		
		injectH(around,end);
		
		openList.remove(cur);
		closeList.add(cur);
		
		if(around.contains(end))
		{
			int index =around.indexOf(end);
			Point find = around.get(index);
			return find;
		}
		
		for(Point p:around)
		{
			if(closeList.contains(p))
			{
				continue;
			}
			if(openList.contains(p))
			{
			    int index = openList.indexOf(p);
			    Point ogl= openList.get(index);
			    if(p.getF()<ogl.getF())
			    {
			    	ogl.setG(p.getG());
			    	ogl.setPrePoint(p.getPrePoint());
			    }
			}else
			{
				openList.add(p);
			}
			
		}
		
		
		
		Point next=getMinFPoint(openList);
		
		if(next==null)
		{
			return null;
		}
		return find(specMap,openList ,closeList,next,end);
	}
	
	public static Point getMinFPoint(List<Point> openList)
	{
		if(openList==null || openList.size()<=0)
		{
			return null;
		}
		Point cur=null;
		for(Point p:openList)
		{
			if(cur==null || p.getF()<cur.getF())
			{
				cur=p;
			}
		}
		return cur;
	}

	/**
	 * 获取当前点周围可走的点
	 * @param specMap
	 * @param curPoint
	 * @return
	 */
	public static List<Point> getAroundAblePoint(int [][] specMap,Point curPoint)
	{
		List<Point> list = new ArrayList<Point>();
		int curX=curPoint.getX();
		int curY =curPoint.getY();
		int startX = curX-1;
		if(startX<0)
		{
			startX=curX;
		}
		int endX = curX+1;
		
		if(endX>=specMap.length)
		{
			endX= curX;
		}
		
		int startY =curY-1;
		if(startY<0)
		{
			startY=curY;
		}
		
		for(int i=startX;i<=endX;i++)
		{
			int curCol = specMap[startX].length;
			int endY= curY+1;
			if(endY>=curCol)
			{
				endY=curY;
			}
			for(int j=startY;j<=endY;j++)
			{
				//周围的节点
				if((i!=curX || j!=curY) && specMap[i][j]!=1)
				{
					Point p = new Point(i,j);
				    p.setPrePoint(curPoint);
				    int addG=10;//同一条直线10  对角线14
				    if(i!=curX && j!=curY)
				    {
				    	addG=14;
				    }
				    p.setG(curPoint.getG()+addG);
				    list.add(p);
				    
				}
			}
		}
		return list;
	}
	
	/**
	 * 计算出当前点到终点的开销  横竖移动
	 * @param curPoint
	 * @param end
	 * @return
	 */
	public static int arthH(Point curPoint,Point end)
	{
		int stepX=Math.abs(curPoint.getX()-end.getX());
		int stepY=Math.abs(curPoint.getY()-end.getY());
		return (stepX +stepY)*10 ;
	}
	
	public static void injectH(Point curPoint,Point end)
	{
		int h= arthH(curPoint,end);
		curPoint.setH(h);
	}
	
	public static void injectH(List<Point> pointList,Point end)
	{
		for(Point p :pointList)
		{
			injectH(p,end);
		}
	}
}
