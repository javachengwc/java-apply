package com.concurrent.thread;

/**服务员类**/
public class Servant extends Thread {
	
	private Plate plate;
	
	public Servant (String name,Plate plate){
		super(name);
		this.plate=plate;
	}
	
	public void run(){
		while(true)
		{
			plate.putCount();
		}
	}

}
