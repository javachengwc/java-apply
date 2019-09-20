package com.struct.set;

import java.io.Serializable;
import java.util.*;

//HashSet 是一个由 HashMap 实现的集合。元素无序且不重复
//HashSet 也实现了 Cloneable 接口和 Serializable 接口，分别用来支持克隆以及支持序列化
public class HashSet1<E> extends AbstractSet<E> implements Set<E>, Cloneable, Serializable {

    static final long serialVersionUID = -5024744406713321676L;

    //HashSet集合中的内容是通过 HashMap 数据结构来存储的
    private transient HashMap<E, Object> map;

    //存储在 HashSet 中的数据 作为 Map 的 key，而 Map 的value 统一为 PRESENT
    private static final Object PRESENT = new Object();

    //无参的 HashMap 构造函数，具有默认初始容量（16）和加载因子（0.75）
    public HashSet1() {
        map = new HashMap<>();
    }

    public HashSet1(int initialCapacity) {
        map = new HashMap<>(initialCapacity);
    }

    public HashSet1(int initialCapacity, float loadFactor) {
        map = new HashMap<>(initialCapacity, loadFactor);
    }

    public Iterator<E> iterator() {
        return map.keySet().iterator();
    }

    public int size() {
        return map.size();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    public boolean add(E e) {
        return map.put(e, PRESENT)==null;
    }

    public boolean remove(Object o) {
        return map.remove(o)==PRESENT;
    }

    public void clear() {
        map.clear();
    }

    public Object clone() {
        try {
            HashSet1<E> newSet = (HashSet1<E>) super.clone();
            newSet.map = (HashMap<E, Object>) map.clone();
            return newSet;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }

}