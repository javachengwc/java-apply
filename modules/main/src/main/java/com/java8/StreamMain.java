package com.java8;

import com.entity.Entity;
import org.apache.commons.lang.math.NumberUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamMain {

    public static void main(String args [] ) {
        List<String> list = new ArrayList<String>();
        list.add("aa");
        list.add("11");
        Stream<String> listStream = list.stream();
        //筛选
        List<String> rtList =listStream
                .filter(NumberUtils::isNumber)
                .collect(Collectors.toList());
        printList(rtList);

        String[] names = {"a","c","c","b","e","f","g"};
        Stream<String> arrayStream = Arrays.stream(names);
        //去重
        rtList= arrayStream.distinct().collect(Collectors.toList());
        printList(rtList);

        Stream<String> stream = Stream.of("ab","bc","cc","11","22","33");
        //截取
        rtList= stream.limit(3).collect(Collectors.toList());
        printList(rtList);
        //跳过
        rtList= Arrays.stream(names).skip(5).collect(Collectors.toList());
        printList(rtList);

        List<Entity> entityList = new ArrayList<Entity>();
        entityList.add(new Entity(1,"a"));
        entityList.add(new Entity(2,"b"));
        entityList.add(new Entity(3,"c",true));
        //映射
        List<String> nameList = entityList.stream()
                .map(Entity::getName)
                .collect(Collectors.toList());
        printList(nameList);

        List<String> jList = new ArrayList<String>();
        jList.add("I come here");
        jList.add("I do what");
        //合并多个流
        rtList=jList.stream().map(a->a.split(" "))
                .flatMap(Arrays::stream)
                .distinct()
                .collect(Collectors.toList());
        printList(rtList);

        //anyMatch---是否匹配任一元素
        boolean result = entityList.stream().anyMatch(Entity::isGood);
        System.out.println(result);

        //allMatch---是否匹配所有元素
        result = entityList.stream().allMatch(Entity::isGood);
        System.out.println(result);

        //noneMatch---是否没有一个元素匹配
        result = entityList.stream().allMatch(Entity::isGood);
        System.out.println(result);

        //indAny---获取任一元素
        Optional<Entity> entityOpt = entityList.stream().findAny();
        System.out.println(entityOpt.isPresent());//true--表示有元素
        if(entityOpt.isPresent()) {
            Entity entity = entityOpt.get();
            System.out.println(entity);
        }

        //findFirst---获取第一个元素
        entityOpt=Collections.EMPTY_LIST.stream().findFirst();

        //求和
        List<Integer> numList =Arrays.asList(1,2,3,4);
        //reduce第一个参数表示初始值，第二个参数为(a,b)->a+b 是lambda表达式
        Integer sum = numList.stream().reduce(0, (a,b)->a+b);
        System.out.println(sum);

        //将普通流转换成数值流
        IntStream intStream = entityList.stream().mapToInt(Entity::getId);

        //最大值,最小值
        OptionalInt maxIdOpt = entityList.stream().mapToInt(Entity::getId).max();
        if(maxIdOpt.isPresent()) {
            System.out.println(maxIdOpt.getAsInt());
        }

        //求和
        sum = entityList.stream().mapToInt(Entity::getId).sum();
        System.out.println(sum);
        List<Entity> emptyList =Collections.EMPTY_LIST;
        sum = emptyList.stream().mapToInt(Entity::getId).sum();
        System.out.println(sum);
    }

    public static  <T> void printList(List<T> list) {
        int size = list==null?0:list.size();
        System.out.println("-------list size = "+size+"------");
        if(size>0) {
            int i=0;
            for(T t:list) {
                System.out.println(" list("+ i+") = "+t.toString());
                i++;
            }
        }
    }
}
