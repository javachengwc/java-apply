package com.struct.tree.tiretree;

import java.util.HashMap;
import java.util.Map;

//前缀树,也叫字典树
//搜索引擎lucene，term index 涉及的数据结构
//TrieTree的特点:
//根节点不包含字符，除根节点外每一个节点都只包含一个字符。
//从根节点到某一节点，路径上经过的字符连接起来，为该节点对应的字符串。
//每个节点的所有子节点包含的字符都不相同。
public class TrieTree {

    private TrieNode root;

    public TrieTree() {
        root = new TrieNode();
    }

    //插入
    public void insert(String word) {
        insert(root,word);
    }

    private void insert(TrieNode node,String word) {
        word = word.toLowerCase();
        char [] chrs = word.toCharArray();
        int length = chrs.length;
        for(int i=0;i<length;i++) {
            int index = chrs[i]-'a';
            if(node.childs[index]==null) {
                node.childs[index]= new TrieNode();
            }
            node.childs[index].prefixNum++;
            if(i==length-1) {
                node.childs[index].isLeaf=true;
                node.childs[index].dumpliNum++;
            }
            //node指向子节点，继续处理
            node=node.childs[index];
        }
    }

    //遍历
    public Map<String,Integer> getAll() {
        return preTraversal(root,"");
    }

    private Map<String,Integer> preTraversal(TrieNode node,String pre) {
        Map<String,Integer> map = new HashMap<String,Integer>();
        if(node!=null) {
            if(node.isLeaf) {
                map.put(pre,node.dumpliNum);
            }
            for(int i=0;i<node.childs.length;i++) {
                if(node.childs[i]!=null) {
                    char cur = (char)(i+'a');
                    String curPre = pre+cur;
                    map.putAll(preTraversal(node.childs[i],curPre));
                }
            }
        }
        return map;
    }

    public boolean isExist(String word) {
        return search(root,word);
    }

    public boolean search(TrieNode node,String word) {
        char [] chs = word.toLowerCase().toCharArray();
        for(int i=0;i<chs.length;i++) {
            int index= chs[i]-'a';
            if(node.childs[index]==null) {
                return false;
            }
            node = node.childs[index];
        }
        return true;
    }

    //获取以某个字串为前缀的字串集
    public Map<String,Integer> listByPre(String pre) {

        return listByPre(root,pre);
    }

    private Map<String,Integer> listByPre(TrieNode node,String pre) {
        char [] chs = pre.toLowerCase().toCharArray();
        for(int i=0;i<chs.length;i++) {
            int index= chs[i]-'a';
            if(node.childs[index]==null) {
                return null;
            }
            node= node.childs[index];
        }
        return  preTraversal(node,pre);
    }

}
