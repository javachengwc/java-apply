package com.main;

import org.apache.commons.lang.math.NumberUtils;

import java.math.BigDecimal;

public class NumberMain {
	
	public static void main(String args [])
	{
        System.out.println("null to number:"+ NumberUtils.toLong(null) );
        //经典的带小数的值计算为非精准计算
        double ad=2.3d;
        System.out.println(NumberUtils.isNumber("0.2222"));
        System.out.println("............................");
        System.out.println(new Double(ad*100).intValue());

        //小数的精准计算
        //不能传float或double作为BigDecimal构造参，只能用String作为构造参数
        //如果用float或double作为BigDecimal构造参，multiply后计算的结果也是不精准的
        BigDecimal vv=new BigDecimal("3333333.33333333333");
        System.out.println(vv.toString());
        BigDecimal rt =vv.multiply(new BigDecimal(100));
        System.out.println(rt.intValue());
        System.out.println(new BigDecimal(vv.toString()).multiply(new BigDecimal(100)).intValue()+"--------------");

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
