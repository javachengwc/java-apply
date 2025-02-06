package com.arithmetic.a;

public class Point extends Object {

	private int x;
	
	private int y;
	
	//从起点沿着已生成的路径到一个给定方格的移动开销 同一条直线10  对角线14
	private int g;
	
	//从给定方格到目的方格的估计移动开销  横竖
	private int h;
	
	private Point prePoint;
	
	public Point()
	{
		
	}
	
	public Point(int x,int y)
	{
		this.x=x;
		this.y=y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getF() {
		return g+h;
	}


	public int getG() {
		return g;
	}

	public void setG(int g) {
		this.g = g;
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	public Point getPrePoint() {
		return prePoint;
	}

	public void setPrePoint(Point prePoint) {
		this.prePoint = prePoint;
	}
	
	public boolean equals(Object other)
	{
		if(other==null)
		{
			return false;
		}
		if(other instanceof Point)
		{
			Point o=(Point)other;
			if(this.getX()== o.getX() && this.getY()==o.getY())
			{
				return true;
			}
			return false;
		}	
		return super.equals(other);
	}
	
	public int hashCode()
	{
		return getX()*10000+getY();
	}
	
	public String toString()
	{
		return x+" "+y+" "+g+" "+h+" ";
		
	}
}
