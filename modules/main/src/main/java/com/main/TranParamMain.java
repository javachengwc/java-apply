package com.main;

import com.entity.Entity;

/**
 * 传参示例
 */
public class TranParamMain {

    public static void main(String [] args) {
        baseTypeTran();
        objectTran();
        integerTran();
        stringTran();
    }

    //基本类型传参
    public static void baseTypeTran() {
        int i=1;
        baeeDo(i);
        System.out.println(i);
    }

    private static void baeeDo(int  i) {
        i=i+1;
    }

    //Integer类型传参
    public static void integerTran() {
        Integer i=1;
        integerDo(i);
        System.out.println(i);
    }

    private static void integerDo(Integer i) {
        i+=1;
    }

    //String类型传参
    public static void stringTran() {
        String str="aa";
        stringDo(str);
        System.out.println(str);
    }

    private static void stringDo(String str) {
        str = str+"1";
    }

    //对象类型传参
    public static void objectTran() {

        Entity entity = new Entity(1,"test");
        objectDo(entity);
        System.out.println(entity);
    }

    private static void objectDo(Entity entity) {
        //entity= new Entity(3,"test");
        entity.setId(2);
    }
}
