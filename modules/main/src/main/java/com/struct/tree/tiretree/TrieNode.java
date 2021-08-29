package com.struct.tree.tiretree;

public class TrieNode {

    //字串重复数目
    public  int dumpliNum;

    //以该字串为前置的字串数(包含自身)
    public  int prefixNum;

    public TrieNode [] childs;

    //是否为单词节点
    public boolean isLeaf;

    public TrieNode() {
        this.dumpliNum=0;
        this.prefixNum=0;
        this.isLeaf=false;
        this.childs= new TrieNode[26];
    }
}