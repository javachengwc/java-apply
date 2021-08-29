package com.struct.tree.tiretree.v2;

import java.util.Map;

public class TrieTreeTest2 {

    public static void main(String[] args) {
        TrieTree2 tree = new TrieTree2();
        tree.insert("四川");
        tree.insert("成都");
        tree.insert("中国");
        tree.insert("中国人民");

        String word = "中国";
        boolean flag = tree.search(word);
        System.out.println("---------" + flag + "----------------");

        //分析
        Map<String, Integer> map = tree.analysis("中国首都是北京,中国北京有天安门，中国人民起来了");
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
    }
}
