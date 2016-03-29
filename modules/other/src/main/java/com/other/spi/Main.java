package com.other.spi;

import com.other.spi.search.Search;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * 在编译打包后的jar里添加META-INF/searvices/com.other.spi.search.Search文件
 *
 * 当此文件内容是:com.other.spi.search.FileSearch时，程序输出是:file search. keyword:test
 * 当此文件内容是:com.other.spi.search.DbSearch时，程序输出是:db search. keyword:
 * 
 * main方法中没有任何和具体实现有关的代码，它基于spi的机制去查找服务的实现
 */
public class Main {

    public static void main(String[] args) {

        ServiceLoader<Search> s = ServiceLoader.load(Search.class);
        Iterator<Search> searchs = s.iterator();
        if (searchs.hasNext()) {
            Search curSearch = searchs.next();
            curSearch.search("test");
        }else
        {
            System.out.println("no find service");
        }
    }
}
