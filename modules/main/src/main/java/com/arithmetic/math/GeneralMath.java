package com.arithmetic.math;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class GeneralMath {

    public static void main(String [] args) {
        System.out.println(Math.min(1,2));
        System.out.println(Math.max(1,2));
        //向上取整
        System.out.println(Math.ceil(1.2));
        //向下取整
        System.out.println(Math.floor(1.6));
        //四舍五入
        System.out.println(Math.round(1.3));
        System.out.println(Math.round(1.6));
        //n次方
        System.out.println(Math.pow(2,3));
        //平方根
        System.out.println(Math.sqrt(5));
        //立方根
        System.out.println(Math.cbrt(27));
        //log10
        System.out.println(Math.log10(100));
        //log
        System.out.println(Math.log(2));

        BigDecimal cc = new BigDecimal("25.25");
        DecimalFormat format = new DecimalFormat("#.#");
        System.out.println(format.format(cc));
        System.out.println(Math.random());
    }
}
