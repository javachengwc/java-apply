package com.pseudocode.util;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

//jdk8实现
public class HashMap1<K,V>  {
    // extends AbstractMap<K,V> implements Map<K,V>, Cloneable, Serializable

    //默认容量
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;

    //最大容量
    static final int MAXIMUM_CAPACITY = 1 << 30;

    //默认加载因子
    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    //转红黑树的默认链表长度
    static final int TREEIFY_THRESHOLD = 8;

    //由红黑数转回成链表的节点数
    //在resize()扩容中判断红黑树节点<=6,会转换成链表
    static final int UNTREEIFY_THRESHOLD = 6;

    //转红黑树的最小容量
    static final int MIN_TREEIFY_CAPACITY = 64;

    //简单封装Map.Entry<K,V>;
    static class Node<K,V> implements Map.Entry<K,V> {
        final int hash;
        final K key;
        V value;
        HashMap1.Node<K,V> next;

        Node(int hash, K key, V value, HashMap1.Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        public final K getKey()        { return key; }
        public final V getValue()      { return value; }
        public final String toString() { return key + "=" + value; }

        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        public final V setValue(V newValue) {
            V oldValue = value;
            value = newValue;
            return oldValue;
        }

        public final boolean equals(Object o) {
            if (o == this)
                return true;
            if (o instanceof Map.Entry) {
                Map.Entry<?,?> e = (Map.Entry<?,?>)o;
                if (Objects.equals(key, e.getKey()) &&
                        Objects.equals(value, e.getValue()))
                    return true;
            }
            return false;
        }
    }

    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    //返回cap最接近2的n次方的数
    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        //>>>无符号右移，无论是正数负数，高位补0
        //比如cap=5,n=4;
        n |= n >>> 1;//  00000100 | 00000010 = 00000110  6
        n |= n >>> 2; // 00000110 | 00000011 = 00000111  7
        n |= n >>> 4; // 00000111 | 00000000 = 00000111  7
        n |= n >>> 8; // 00000111 | 00000000 = 00000111  7
        n |= n >>> 16;// 00000111 | 00000000 = 00000111  7
        //返回n+1=8
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

    transient HashMap1.Node<K,V>[] table;

    transient Set<Map.Entry<K,V>> entrySet;

    //存放KV的数量
    transient int size;

    transient int modCount;

    //threshold表示当HashMap的size大于threshold时会执行resize操作，threshold=capacity*loadFactor
    int threshold;

    //装载因子，实时装载因子的计算方法为：size/capacity
    final float loadFactor;

    //AbstractMap中的属性
    transient Set<K>        keySet;

    //AbstractMap中的属性
    transient Collection<V> values;

    public HashMap1(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        if (initialCapacity > MAXIMUM_CAPACITY)
            initialCapacity = MAXIMUM_CAPACITY;
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
        this.loadFactor = loadFactor;
        this.threshold = tableSizeFor(initialCapacity);
    }

    public HashMap1(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public HashMap1() {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
    }

    public HashMap1(Map<? extends K, ? extends V> m) {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        putMapEntries(m, false);
    }

    final void putMapEntries(Map<? extends K, ? extends V> m, boolean evict) {
        int s = m.size();
        if (s > 0) {
            if (table == null) {
                float ft = ((float)s / loadFactor) + 1.0F;
                int t = ((ft < (float)MAXIMUM_CAPACITY) ?
                        (int)ft : MAXIMUM_CAPACITY);
                if (t > threshold)
                    threshold = tableSizeFor(t);
            }
            else if (s > threshold)
                resize();
            for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
                K key = e.getKey();
                V value = e.getValue();
                putVal(hash(key), key, value, false, evict);
            }
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    //查找数据
    public V get(Object key) {
        HashMap1.Node<K,V> e;
        return (e = getNode(hash(key), key)) == null ? null : e.value;
    }

    final HashMap1.Node<K,V> getNode(int hash, Object key) {
        HashMap1.Node<K,V>[] tab; HashMap1.Node<K,V> first, e; int n; K k;
        //定位下标用的&操作，(n - 1) & hash,只有长度为2的n次方才适合这样用，不然与操作后，结果会有空缺，
        //始终得不到一些下标值。
        if ((tab = table) != null && (n = tab.length) > 0 && (first = tab[(n - 1) & hash]) != null) {
            //比较key的hash值和equals是否相等
            if (first.hash == hash &&  ((k = first.key) == key || (key != null && key.equals(k))))
                return first;
            if ((e = first.next) != null) {
                if (first instanceof HashMap1.TreeNode) {
                    //return ((HashMap1.TreeNode<K, V>) first).getTreeNode(hash, key);
                }
                do {
                    if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k))))
                        return e;
                } while ((e = e.next) != null);
            }
        }
        return null;
    }

    public boolean containsKey(Object key) {
        return getNode(hash(key), key) != null;
    }

    public V put(K key, V value) {
        return putVal(hash(key), key, value, false, true);
    }

    final V putVal(int hash, K key, V value, boolean onlyIfAbsent, boolean evict) {
        HashMap1.Node<K,V>[] tab; HashMap1.Node<K,V> p; int n, i;
        if ((tab = table) == null || (n = tab.length) == 0)
            //map创建时没初始化链表数组，第一次放入数据时,创建链表数组
            n = (tab = resize()).length;
        if ((p = tab[i = (n - 1) & hash]) == null)
            tab[i] = newNode(hash, key, value, null);
        else {
            HashMap1.Node<K,V> e=null; K k;
            if (p.hash == hash && ((k = p.key) == key || (key != null && key.equals(k))))
                e = p;
            else if (p instanceof HashMap1.TreeNode) {
               // e = ((HashMap1.TreeNode<K, V>) p).putTreeVal(this, tab, hash, key, value);
            }
            else {
                for (int binCount = 0; ; ++binCount) {
                    if ((e = p.next) == null) {
                        //放到链尾
                        p.next = newNode(hash, key, value, null);
                        if (binCount >= TREEIFY_THRESHOLD - 1) { // -1 for 1st
                            //链长等于8个，转换成红黑树
                            treeifyBin(tab, hash);
                        }
                        break;
                    }
                    //找到对应已存在的key
                    if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k))))
                        break;
                    p = e;
                }
            }
            if (e != null) { // existing mapping for key
                V oldValue = e.value;
                if (!onlyIfAbsent || oldValue == null)
                    e.value = value;
                afterNodeAccess(e);
                return oldValue;
            }
        }
        ++modCount;
        if (++size > threshold) {
            //添加了kv以后，需要扩容才扩容
            resize();
        }
        afterNodeInsertion(evict);
        return null;
    }

    //链表转换成红黑树
    final void treeifyBin(HashMap1.Node<K,V>[] tab, int hash) {
        int n, index; HashMap1.Node<K,V> e;
        if (tab == null || (n = tab.length) < MIN_TREEIFY_CAPACITY) {
            //容量不到64，扩容
            resize();
        } else if ((e = tab[index = (n - 1) & hash]) != null) {
            HashMap1.TreeNode<K,V> hd = null, tl = null;
            do {
                HashMap1.TreeNode<K,V> p = replacementTreeNode(e, null);
                if (tl == null)
                    hd = p;
                else {
                    p.prev = tl;
                    tl.next = p;
                }
                tl = p;
            } while ((e = e.next) != null);
            if ((tab[index] = hd) != null) {
                //转成红黑树
                //hd.treeify(tab);
            }
        }
    }

    HashMap1.TreeNode<K,V> replacementTreeNode(HashMap1.Node<K,V> p, HashMap1.Node<K,V> next) {
        return new HashMap1.TreeNode<>(p.hash, p.key, p.value, next);
    }

    Node<K,V> newNode(int hash, K key, V value, Node<K,V> next) {
        return new Node<>(hash, key, value, next);
    }

    //扩容
    final HashMap1.Node<K,V>[] resize() {
        HashMap1.Node<K,V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold;
        int newCap, newThr = 0;
        if (oldCap > 0) {
            if (oldCap >= MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return oldTab;
            }
            else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                    oldCap >= DEFAULT_INITIAL_CAPACITY) {
                //cap,thr都扩容2倍
                newThr = oldThr << 1;
            }
        }
        else if (oldThr > 0)
            newCap = oldThr;
        else {
            //默认容量16
            //扩容砸值为 16*0.75=12
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }
        if (newThr == 0) {
            float ft = (float)newCap * loadFactor;
            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                    (int)ft : Integer.MAX_VALUE);
        }
        threshold = newThr;
        //新的数组
        HashMap1.Node<K,V>[] newTab = (HashMap1.Node<K,V>[])new HashMap1.Node[newCap];
        //初始化链表数组
        table = newTab;
        if (oldTab != null) {
            for (int j = 0; j < oldCap; ++j) {
                HashMap1.Node<K,V> e;
                if ((e = oldTab[j]) != null) {
                    oldTab[j] = null;
                    if (e.next == null) {
                        //放到新的数组中
                        newTab[e.hash & (newCap - 1)] = e;
                    }
                    else if (e instanceof HashMap1.TreeNode) {
                        //这里如果红黑树节点数<=6,会转换成链表
                        //((HashMap1.TreeNode<K, V>) e).split(this, newTab, j, oldCap);
                    }
                    else { // preserve order
                        HashMap1.Node<K,V> loHead = null, loTail = null;
                        HashMap1.Node<K,V> hiHead = null, hiTail = null;
                        HashMap1.Node<K,V> next;
                        do {
                            next = e.next;
                            if ((e.hash & oldCap) == 0) {
                                if (loTail == null)
                                    loHead = e;
                                else
                                    loTail.next = e;
                                loTail = e;
                            }
                            else {
                                if (hiTail == null)
                                    hiHead = e;
                                else
                                    hiTail.next = e;
                                hiTail = e;
                            }
                        } while ((e = next) != null);
                        if (loTail != null) {
                            loTail.next = null;
                            newTab[j] = loHead;
                        }
                        if (hiTail != null) {
                            hiTail.next = null;
                            newTab[j + oldCap] = hiHead;
                        }
                    }
                }
            }
        }
        return newTab;
    }

    public void putAll(Map<? extends K, ? extends V> m) {
        putMapEntries(m, true);
    }

    public V remove(Object key) {
        HashMap1.Node<K,V> e;
        return (e = removeNode(hash(key), key, null, false, true)) == null ?
                null : e.value;
    }

    final HashMap1.Node<K,V> removeNode(int hash, Object key, Object value, boolean matchValue, boolean movable) {
        HashMap1.Node<K,V>[] tab; HashMap1.Node<K,V> p; int n, index;
        if ((tab = table) != null && (n = tab.length) > 0 && (p = tab[index = (n - 1) & hash]) != null) {
            HashMap1.Node<K,V> node = null, e; K k; V v;
            if (p.hash == hash && ((k = p.key) == key || (key != null && key.equals(k))))
                node = p;
            else if ((e = p.next) != null) {
                if (p instanceof HashMap1.TreeNode) {
                    //node = ((HashMap1.TreeNode<K, V>) p).getTreeNode(hash, key);
                }
                else {
                    do {
                        if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k)))) {
                            node = e;
                            break;
                        }
                        p = e;
                    } while ((e = e.next) != null);
                }
            }
            //找到node节点
            if (node != null && (!matchValue || (v = node.value) == value ||
                    (value != null && value.equals(v)))) {
                if (node instanceof HashMap1.TreeNode) {
                    //删除节点
                    //((HashMap1.TreeNode<K, V>) node).removeTreeNode(this, tab, movable);
                }
                else if (node == p)
                    tab[index] = node.next;
                else
                    p.next = node.next;
                ++modCount;
                --size;
                afterNodeRemoval(node);
                return node;
            }
        }
        return null;
    }

    public void clear() {
        HashMap1.Node<K,V>[] tab;
        modCount++;
        if ((tab = table) != null && size > 0) {
            size = 0;
            for (int i = 0; i < tab.length; ++i)
                tab[i] = null;
        }
    }

    public boolean containsValue(Object value) {
        HashMap1.Node<K,V>[] tab; V v;
        if ((tab = table) != null && size > 0) {
            for (int i = 0; i < tab.length; ++i) {
                for (HashMap1.Node<K,V> e = tab[i]; e != null; e = e.next) {
                    if ((v = e.value) == value ||
                            (value != null && value.equals(v)))
                        return true;
                }
            }
        }
        return false;
    }

    public Set<K> keySet() {
        Set<K> ks = keySet;
//        if (ks == null) {
//            ks = new HashMap1.KeySet();
//            keySet = ks;
//        }
        return ks;
    }

    public Collection<V> values() {
        Collection<V> vs = values;
//        if (vs == null) {
//            vs = new HashMap1.Values();
//            values = vs;
//        }
        return vs;
    }

    public Set<Map.Entry<K,V>> entrySet() {
        Set<Map.Entry<K,V>> es=null;
        //return (es = entrySet) == null ? (entrySet = new HashMap1.EntrySet()) : es;
        return es;
    }

    public V getOrDefault(Object key, V defaultValue) {
        HashMap1.Node<K,V> e;
        return (e = getNode(hash(key), key)) == null ? defaultValue : e.value;
    }

    public V putIfAbsent(K key, V value) {
        return putVal(hash(key), key, value, true, true);
    }

    public boolean remove(Object key, Object value) {
        return removeNode(hash(key), key, value, true, true) != null;
    }

    public boolean replace(K key, V oldValue, V newValue) {
        HashMap1.Node<K,V> e; V v;
        if ((e = getNode(hash(key), key)) != null && ((v = e.value) == oldValue || (v != null && v.equals(oldValue)))) {
            e.value = newValue;
            afterNodeAccess(e);
            return true;
        }
        return false;
    }

    public V replace(K key, V value) {
        HashMap1.Node<K,V> e;
        if ((e = getNode(hash(key), key)) != null) {
            V oldValue = e.value;
            e.value = value;
            afterNodeAccess(e);
            return oldValue;
        }
        return null;
    }

    public void forEach(BiConsumer<? super K, ? super V> action) {
        HashMap1.Node<K,V>[] tab;
        if (action == null)
            throw new NullPointerException();
        if (size > 0 && (tab = table) != null) {
            int mc = modCount;
            for (int i = 0; i < tab.length; ++i) {
                for (HashMap1.Node<K,V> e = tab[i]; e != null; e = e.next)
                    action.accept(e.key, e.value);
            }
            if (modCount != mc)
                throw new ConcurrentModificationException();
        }
    }

    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        HashMap1.Node<K,V>[] tab;
        if (function == null)
            throw new NullPointerException();
        if (size > 0 && (tab = table) != null) {
            int mc = modCount;
            for (int i = 0; i < tab.length; ++i) {
                for (HashMap1.Node<K,V> e = tab[i]; e != null; e = e.next) {
                    e.value = function.apply(e.key, e.value);
                }
            }
            if (modCount != mc)
                throw new ConcurrentModificationException();
        }
    }

    final float loadFactor() { return loadFactor; }

    final int capacity() {
        return (table != null) ? table.length :
                (threshold > 0) ? threshold :
                        DEFAULT_INITIAL_CAPACITY;
    }

    void afterNodeAccess(HashMap1.Node<K,V> p) { }
    void afterNodeInsertion(boolean evict) { }
    void afterNodeRemoval(HashMap1.Node<K,V> p) { }

    static final class TreeNode<K,V> extends Node<K,V> {
        //extends LinkedHashMap.Entry<K,V>
        HashMap1.TreeNode<K,V> parent;  // red-black tree links
        HashMap1.TreeNode<K,V> left;
        HashMap1.TreeNode<K,V> right;
        HashMap1.TreeNode<K,V> prev;    // needed to unlink next upon deletion
        boolean red;
        TreeNode(int hash, K key, V val, HashMap1.Node<K,V> next) {
            super(hash, key, val, next);
        }

        final HashMap1.TreeNode<K,V> root() {
            for (HashMap1.TreeNode<K,V> r = this, p;;) {
                if ((p = r.parent) == null)
                    return r;
                r = p;
            }
        }
    }
}
