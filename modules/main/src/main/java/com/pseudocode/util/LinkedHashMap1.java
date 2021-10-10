package com.pseudocode.util;

import java.util.*;

//LinkedHashMap是一个map与双向链表结合的数据结构。
//顺序性可以是插入顺序，也可以是访问顺序，由accessOrder参数控制。
//LinkedHashMap可用作LRU算法。
public class LinkedHashMap1<K,V>  extends HashMap1<K,V> implements Map<K,V>
{
    private static final long serialVersionUID = 3801124242820219131L;

    static class Entry<K,V> extends HashMap1.Node<K,V> {
        LinkedHashMap1.Entry<K,V> before, after;
        Entry(int hash, K key, V value, Node<K,V> next) {
            super(hash, key, value, next);
        }
    }

    transient LinkedHashMap1.Entry<K,V> head;

    transient LinkedHashMap1.Entry<K,V> tail;

    //默认false--按插入排序，true--按访问排序
    final boolean accessOrder;

    public LinkedHashMap1(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
        accessOrder = false;
    }

    public LinkedHashMap1(int initialCapacity) {
        super(initialCapacity);
        accessOrder = false;
    }

    public LinkedHashMap1() {
        super();
        accessOrder = false;
    }

    public LinkedHashMap1(Map<? extends K, ? extends V> m) {
        super();
        accessOrder = false;
        putMapEntries(m, false);
    }

    public LinkedHashMap1(int initialCapacity,
                         float loadFactor,
                         boolean accessOrder) {
        super(initialCapacity, loadFactor);
        this.accessOrder = accessOrder;
    }

    public boolean containsValue(Object value) {
        for (LinkedHashMap1.Entry<K,V> e = head; e != null; e = e.after) {
            V v = e.value;
            if (v == value || (value != null && value.equals(v)))
                return true;
        }
        return false;
    }

    public V get(Object key) {
        Node<K,V> e;
        if ((e = getNode(hash(key), key)) == null)
            return null;
        if (accessOrder)
            afterNodeAccess(e);
        return e.value;
    }

    public V getOrDefault(Object key, V defaultValue) {
        Node<K,V> e;
        if ((e = getNode(hash(key), key)) == null)
            return defaultValue;
        if (accessOrder)
            afterNodeAccess(e);
        return e.value;
    }

    public void clear() {
        super.clear();
        head = tail = null;
    }

    //判断是否移除map中最老的Entry
    protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
        return false;
    }

}
