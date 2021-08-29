package com.struct.tree.tiretree.v2;

import java.util.HashMap;
import java.util.Map;

public class TrieTree2 {

    private TrieNode2 root = new TrieNode2(' ');

    public void insert(String word){
        char[] arr = word.toCharArray();
        TrieNode2 cur = root;
        for (char c : arr) {
            TrieNode2 node = cur.childNode(c);
            if(node == null) {
                node = new TrieNode2(c);
                cur.childList.add(node);
            }
            cur = node;
        }
        //在词的最后一个字节点标记为true
        cur.isEnd = true;
    }

    //判断TrieTree中是否包含该词
    public boolean search(String word){
        char[] arr = word.toCharArray();
        TrieNode2 cur = root;
        for (int i=0; i<arr.length; i++) {
            TrieNode2 n = cur.childNode(arr[i]);
            if(n != null){
                cur = n;
                if(n.isEnd){
                    if(n.c == arr[arr.length-1]){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Map<String, Integer> analysis(String words){
        char[] arr = words.toCharArray();
        TrieNode2 currentNode = root;
        Map<String, Integer> map = new HashMap<String, Integer>();
        StringBuilder sb = new StringBuilder();
        //匹配到的词
        String word=null;
        //匹配坐标
        int idx = 0;
        for (int i=0; i<arr.length; i++) {
            TrieNode2 node = currentNode.childNode(arr[i]);
            if(node != null){
                System.out.println(node);
                sb.append(node.c);
                currentNode = node;
                if(node.isEnd){
                    word = sb.toString();
                    idx = i;
                }
            }else{
                //判断word是否有值
                if(word!=null && word.length()>0){
                    Integer num = map.get(word);
                    if(num==null){
                        map.put(word, 1);
                    }else{
                        map.put(word, num+1);
                    }
                    //i回退到最后匹配的坐标
                    i=idx;
                    //从root的开始匹配
                    currentNode = root;
                    //清空匹配到的词
                    word = null;
                    //清空当前路径匹配到的所有字
                    sb = new StringBuilder();
                }
            }
            if(i==arr.length-1){
                if(word!=null && word.length()>0){
                    Integer num = map.get(word);
                    if(num==null){
                        map.put(word, 1);
                    }else{
                        map.put(word, num+1);
                    }
                }
            }
        }

        return map;
    }
}
