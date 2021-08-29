package com.struct.tree.tiretree.v2;

import java.util.LinkedList;
import java.util.List;

public class TrieNode2 {

   public char c;

    //判断该字是否词语的末尾
    public boolean isEnd;

    //子节点
    public List<TrieNode2> childList;

    public TrieNode2(char c) {
        this.c = c;
        this.isEnd = false;
        this.childList = new LinkedList<TrieNode2>();
    }

    public TrieNode2 childNode(char c){
        for(TrieNode2 node : childList){
            if(node.c == c){
                return node;
            }
        }
        return null;
    }

    public String toString() {
        return c+" "+isEnd;
    }
}
