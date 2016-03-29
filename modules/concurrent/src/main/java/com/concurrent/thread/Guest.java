package com.concurrent.thread;

/**客户群类**/
public class Guest extends Thread{

	private Plate plate;
	
	public Guest (String name,Plate plate){
		super(name);
		this.plate=plate;
	}
	
	public  void run(){
		for(int i=0;i<100;i++)
		{
			int count=plate.getCount();
			
		}
		
	}
}
