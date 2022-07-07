package com.main;

import com.entity.Entity;
import com.entity.SubEntity;

public class ObjectMain {

    public static void main(String [] args ) {
        Entity en = new Entity(1,"test");
        //错误的转换
//        SubEntity sub = (SubEntity) en; //java.lang.ClassCastException:
//        sub.setSubId(10);
//        System.out.println(sub);
    }
}
