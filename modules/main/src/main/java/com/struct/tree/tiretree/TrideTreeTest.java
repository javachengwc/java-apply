package com.struct.tree.tiretree;

import java.util.Collections;
import java.util.Map;

public class TrideTreeTest {

    public static void main(String [] args ) {
        TrieTree tree = new TrieTree();
        tree.insert("i");
        tree.insert("on");
        tree.insert("cd");
        tree.insert("cd");
        tree.insert("cd");
        tree.insert("cdbj");

        Map<String,Integer> all = tree.getAll();
        for(Map.Entry<String,Integer> en:all.entrySet()) {
            System.out.println(en.getKey()+" "+en.getValue());
        }
        System.out.println("-------------------");

        Map<String,Integer> map = tree.listByPre("cd");
        for(Map.Entry<String,Integer> en:map.entrySet()) {
            System.out.println(en.getKey()+" "+en.getValue());
        }
        System.out.println("-------------------");
    }
}
