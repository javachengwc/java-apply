package com.main;

import java.math.BigDecimal;

public class NumberMain {
	
	public static void main(String args [])
	{


        double percent = 599 /(3600*1.0);
        BigDecimal bd = new BigDecimal("" + percent);
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);

        System.out.println("---percent="+percent +" "+new Double(bd.doubleValue()).toString() +" ---bd="+bd.doubleValue());


        String commission="5.0";

        Float f = Float.parseFloat(commission);
        int cmms = new Float(f*100).intValue();
        System.out.println(cmms);


        doDecimal();

		Integer a=1;Integer b=3;
		double c= (a*1.0)/(b*1.0);
		if(c>0.1)
		{
		    System.out.println(c);
		}
		
		double d= a.doubleValue()/b.doubleValue();
		System.out.println(d);
	}


    public static void  doDecimal()
    {
        Double price =252525.7;
        BigDecimal bDprice =new BigDecimal(price.toString());
        BigDecimal one = new BigDecimal(100);
        System.out.println(bDprice.divide(one, 2, BigDecimal.ROUND_HALF_UP));
    }

}
