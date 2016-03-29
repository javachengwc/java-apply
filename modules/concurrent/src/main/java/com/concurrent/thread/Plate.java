package com.concurrent.thread;

/**果盘类**/
public  class Plate {
	
	private volatile int count=10;
	
	public static final int MAX_NUM=10;
	
	
	public Plate()
	{
		
	}
	
	public void setCount(int count) {
		this.count = count;
	}

    /**服务员放水果**/
	public synchronized void putCount()
	{
		try
		{
			//一定要用while,而不是if
			while(count>0)
			{
				this.wait();
			}

			this.count=MAX_NUM;
			System.out.println("-------------");
			this.notifyAll();
			
		}catch(Exception e)
		{
			
		}
	}
	
	public synchronized int getCount()
	{
		System.out.println(Thread.currentThread().getName()+" in");
		try{

            //一定要用while,而不是if
            //while能保证唤醒执行的时候 任然检查果盘是否还有水果，没水果的话任然等待
            //而不是if那样，唤醒后，不管还有没水果，都往下执行，数量减一

			while(count<=0)
			{
				this.wait();
			}
			count--;
			System.out.println(Thread.currentThread().getName()+"拿掉1个 还剩"+count+"个水果");
			
			
			if(count<=0)
			{
				this.notifyAll();

			}
		}catch(Exception e)
		{
			
		}
	    return count;
		
	}
}
