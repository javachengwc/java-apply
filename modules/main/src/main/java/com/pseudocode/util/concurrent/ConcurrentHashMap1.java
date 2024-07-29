package com.pseudocode.util.concurrent;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.LockSupport;

//ConcurrentHashMap1.8
public class ConcurrentHashMap1<K,V> {
    // extends AbstractMap<K,V> implements ConcurrentMap<K,V>, Serializable
    private static final int MAXIMUM_CAPACITY = 1 << 30;

    //默认容量
    private static final int DEFAULT_CAPACITY = 16;

    //最大数组长度
    static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    //默认并发度
    private static final int DEFAULT_CONCURRENCY_LEVEL = 16;

    private static final float LOAD_FACTOR = 0.75f;

    //转红黑树的默认链表长度
    static final int TREEIFY_THRESHOLD = 8;

    //由红黑数转回成链表的节点数
    static final int UNTREEIFY_THRESHOLD = 6;

    //转红黑树的最小容量
    static final int MIN_TREEIFY_CAPACITY = 64;

    //扩容线程每次最少要迁移16个hash桶
    private static final int MIN_TRANSFER_STRIDE = 16;

    //扩容位数标识，主要用于扩容时生成一个巨大负数使用(第32位的标志位与 16位为1左移16位)
    private static int RESIZE_STAMP_BITS = 16;

    //最大并发线程数 65535
    private static final int MAX_RESIZERS = (1 << (32 - RESIZE_STAMP_BITS)) - 1;

    //扩容位数标识剩下的位数
    private static final int RESIZE_STAMP_SHIFT = 32 - RESIZE_STAMP_BITS;

    //节点标识
    static final int MOVED     = -1; // hash for forwarding nodes 节点正在扩容
    static final int TREEBIN   = -2; // hash for roots of trees   树节点
    static final int RESERVED  = -3; // hash for transient reservations  正在设置桶位首节点，与只有一个节点区分
    static final int HASH_BITS = 0x7fffffff; // 最大的整型数INT_MAX

    //cpu数
    static final int NCPU = Runtime.getRuntime().availableProcessors();

    static class Node<K,V> implements Map.Entry<K,V> {
        final int hash;
        final K key;
        //val是volatile修饰的，读可见
        volatile V val;
        volatile ConcurrentHashMap1.Node<K,V> next;

        Node(int hash, K key, V val, ConcurrentHashMap1.Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.val = val;
            this.next = next;
        }

        public final K getKey()       { return key; }
        public final V getValue()     { return val; }
        public final int hashCode()   { return key.hashCode() ^ val.hashCode(); }
        public final String toString(){ return key + "=" + val; }
        public final V setValue(V value) {
            throw new UnsupportedOperationException();
        }

        ConcurrentHashMap1.Node<K,V> find(int h, Object k) {
            ConcurrentHashMap1.Node<K,V> e = this;
            if (k != null) {
                do {
                    K ek;
                    if (e.hash == h && ((ek = e.key) == k || (ek != null && k.equals(ek))))
                        return e;
                } while ((e = e.next) != null);
            }
            return null;
        }
    }

    //hash算法，把位数控制在int最大整数之内
    static final int spread(int h) {
        return (h ^ (h >>> 16)) & HASH_BITS;
    }

    private static final int tableSizeFor(int c) {
        int n = c - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

    static Class<?> comparableClassFor(Object x) {
        if (x instanceof Comparable) {
            Class<?> c; Type[] ts, as; Type t; ParameterizedType p;
            if ((c = x.getClass()) == String.class) // bypass checks
                return c;
            if ((ts = c.getGenericInterfaces()) != null) {
                for (int i = 0; i < ts.length; ++i) {
                    if (((t = ts[i]) instanceof ParameterizedType) &&
                            ((p = (ParameterizedType)t).getRawType() == Comparable.class) &&
                            (as = p.getActualTypeArguments()) != null &&
                            as.length == 1 && as[0] == c) // type arg is c
                        return c;
                }
            }
        }
        return null;
    }

    static int compareComparables(Class<?> kc, Object k, Object x) {
        return (x == null || x.getClass() != kc ? 0 : ((Comparable)k).compareTo(x));
    }

    @SuppressWarnings("unchecked")
    static final <K,V> ConcurrentHashMap1.Node<K,V> tabAt(ConcurrentHashMap1.Node<K,V>[] tab, int i) {
        //U.getObjectVolatile本质是寻址公式，也就是 tab[i]，效果都是查看数组某下标处的元素，U.getObjectVolatile更多是从并发角度来考虑的。
        return (ConcurrentHashMap1.Node<K,V>)U.getObjectVolatile(tab, ((long)i << ASHIFT) + ABASE);
    }

    static final <K,V> boolean casTabAt(ConcurrentHashMap1.Node<K,V>[] tab, int i,
                                        ConcurrentHashMap1.Node<K,V> c, ConcurrentHashMap1.Node<K,V> v) {
        return U.compareAndSwapObject(tab, ((long)i << ASHIFT) + ABASE, c, v);
    }

    static final <K,V> void setTabAt(ConcurrentHashMap1.Node<K,V>[] tab, int i, ConcurrentHashMap1.Node<K,V> v) {
        U.putObjectVolatile(tab, ((long)i << ASHIFT) + ABASE, v);
    }

    //table存储数据的底层数组，volatile修饰，读可见
    transient volatile ConcurrentHashMap1.Node<K,V>[] table;

    //仅当扩容时不为空，其他时候都为空。扩容完成自动为null
    private transient volatile ConcurrentHashMap1.Node<K,V>[] nextTable;

    //元素个数的基础计数，加上counterCells数组所有元素的value值总和 即为所有元素个数
    private transient volatile long baseCount;

    //jdk8采用多线程扩容，扩容过程中，通过CAS设置sizeCtl，transferIndex等变量协调多个线程进行并发扩容
    //sizeCtl=0：表示没有指定初始容量
    //sizeCtl>0：表示初始容量
    //sizeCtl=-1,标记作用，告知其他线程，正在初始化
    //sizeCtl=0.75*n ,扩容阈值，正常状态
    //sizeCtl < 0 : 表示有其他线程正在执行扩容
    //sizeCtl = (resizeStamp(n) << RESIZE_STAMP_SHIFT) + 2 :表示此时只有一个线程在执行扩容
    private transient volatile int sizeCtl;

    //扩容索引，表示已经分配给扩容线程的table数组索引位置。
    //主要用来协调多个线程，并发安全地获取迁移任务（hash桶）。
    //扩容开始时transferindex=table.length
    //扩容线程A以cas的方式修改transferindex=transferindex-stride,比如32-16=16 ,
    //然后按照降序迁移table[31]--table[16]这个区间的hash桶。
    //stride为扩容线程每次迁移的桶个数，最小值是MIN_TRANSFER_STRIDE = 16;
    //如果线程2访问到了ForwardingNode节点，线程2执行的put或remove等写操作，那么就会先帮其扩容。
    //如果线程2执行的是get等读方法，则会调用ForwardingNode的find方法，去nextTable里面查找相关元素。
    //如果准备加入扩容的线程，发现以下情况，将放弃扩容。
    //1，发现transferIndex=0,即所有桶扩容均已分配
    //2，发现扩容线程已经达到最大扩容线程数
    //transferIndex=0表示扩容分配完成
    private transient volatile int transferIndex;

    //用于判断counterCells是否正在被使用，0为可使用，1为当前有线程正在使用
    private transient volatile int cellsBusy;

    //并发情况下，使用该数组进行count联合计数，减少baseCount的计数压力，提高效率
    private transient volatile ConcurrentHashMap1.CounterCell[] counterCells;

    public ConcurrentHashMap1() {
    }

    public ConcurrentHashMap1(int initialCapacity) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException();
        int cap = ((initialCapacity >= (MAXIMUM_CAPACITY >>> 1)) ?
                MAXIMUM_CAPACITY : tableSizeFor(initialCapacity + (initialCapacity >>> 1) + 1));
        this.sizeCtl = cap;
    }

    public ConcurrentHashMap1(Map<? extends K, ? extends V> m) {
        this.sizeCtl = DEFAULT_CAPACITY;
        putAll(m);
    }

    public ConcurrentHashMap1(int initialCapacity, float loadFactor) {
        this(initialCapacity, loadFactor, 1);
    }

    public ConcurrentHashMap1(int initialCapacity, float loadFactor, int concurrencyLevel) {
        if (!(loadFactor > 0.0f) || initialCapacity < 0 || concurrencyLevel <= 0)
            throw new IllegalArgumentException();
        if (initialCapacity < concurrencyLevel)
            initialCapacity = concurrencyLevel;
        long size = (long)(1.0 + (long)initialCapacity / loadFactor);
        int cap = (size >= (long)MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : tableSizeFor((int)size);
        this.sizeCtl = cap;
    }

    public int size() {
        long n = sumCount();
        return ((n < 0L) ? 0 : (n > (long)Integer.MAX_VALUE) ? Integer.MAX_VALUE : (int)n);
    }

    public boolean isEmpty() {
        return sumCount() <= 0L;
    }

    //获取数据
    public V get(Object key) {
        ConcurrentHashMap1.Node<K,V>[] tab; ConcurrentHashMap1.Node<K,V> e, p; int n, eh; K ek;
        int h = spread(key.hashCode());
        if ((tab = table) != null && (n = tab.length) > 0 && (e = tabAt(tab, (n - 1) & h)) != null) {
            if ((eh = e.hash) == h) {
                if ((ek = e.key) == key || (ek != null && key.equals(ek)))
                    return e.val;
            }
            else if (eh < 0)
                return (p = e.find(h, key)) != null ? p.val : null;
            while ((e = e.next) != null) {
                if (e.hash == h && ((ek = e.key) == key || (ek != null && key.equals(ek))))
                    return e.val;
            }
        }
        return null;
    }

    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    public boolean containsValue(Object value) {
        if (value == null)
            throw new NullPointerException();
        ConcurrentHashMap1.Node<K,V>[] t;
        if ((t = table) != null) {
            ConcurrentHashMap1.Traverser<K,V> it = new ConcurrentHashMap1.Traverser<K,V>(t, t.length, 0, t.length);
            for (ConcurrentHashMap1.Node<K,V> p; (p = it.advance()) != null; ) {
                V v;
                if ((v = p.val) == value || (v != null && value.equals(v)))
                    return true;
            }
        }
        return false;
    }

    //加入数据
    public V put(K key, V value) {
        return putVal(key, value, false);
    }

    final V putVal(K key, V value, boolean onlyIfAbsent) {
        //校验参数,key value 不能为空
        if (key == null || value == null) throw new NullPointerException();
        int hash = spread(key.hashCode());
        int binCount = 0;
        //遍历Node
        for (ConcurrentHashMap1.Node<K,V>[] tab = table;;) {
            ConcurrentHashMap1.Node<K,V> f; int n, i, fh;
            if (tab == null || (n = tab.length) == 0)
                //初始table
                tab = initTable();
            //如果下标节点为空
            else if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {
                //CAS对指定位置的节点进行原子操作
                if (casTabAt(tab, i, null, new ConcurrentHashMap1.Node<K,V>(hash, key, value, null)))
                    break;                   // no lock when adding to empty bin
            }
            //当 hash 值是 -1时，说明正在扩容
            else if ((fh = f.hash) == MOVED)
                //调用helpTransfer(tab, f); 一起扩容
                tab = helpTransfer(tab, f);
            else {
                V oldVal = null;
                //锁节点头
                synchronized (f) {
                    if (tabAt(tab, i) == f) {
                        if (fh >= 0) {
                            binCount = 1;
                            for (ConcurrentHashMap1.Node<K,V> e = f;; ++binCount) {
                                K ek;
                                if (e.hash == hash && ((ek = e.key) == key || (ek != null && key.equals(ek)))) {
                                    oldVal = e.val;
                                    if (!onlyIfAbsent)
                                        e.val = value;
                                    break;
                                }
                                ConcurrentHashMap1.Node<K,V> pred = e;
                                if ((e = e.next) == null) {
                                    //加入到链尾
                                    pred.next = new ConcurrentHashMap1.Node<K,V>(hash, key, value, null);
                                    break;
                                }
                            }
                        }
                        else if (f instanceof ConcurrentHashMap1.TreeBin) {
                            ConcurrentHashMap1.Node<K,V> p;
                            binCount = 2;
                            if ((p = ((ConcurrentHashMap1.TreeBin<K,V>)f).putTreeVal(hash, key, value)) != null) {
                                oldVal = p.val;
                                if (!onlyIfAbsent)
                                    p.val = value;
                            }
                        }
                    }
                }
                if (binCount != 0) {
                    if (binCount >= TREEIFY_THRESHOLD)
                        //链表转红黑树
                        treeifyBin(tab, i);
                    if (oldVal != null)
                        return oldVal;
                    break;
                }
            }
        }
        addCount(1L, binCount);
        return null;
    }

    public void putAll(Map<? extends K, ? extends V> m) {
        tryPresize(m.size());
        for (Map.Entry<? extends K, ? extends V> e : m.entrySet())
            putVal(e.getKey(), e.getValue(), false);
    }

    public V remove(Object key) {
        return replaceNode(key, null, null);
    }

    final V replaceNode(Object key, V value, Object cv) {
        int hash = spread(key.hashCode());
        for (ConcurrentHashMap1.Node<K,V>[] tab = table;;) {
            ConcurrentHashMap1.Node<K,V> f; int n, i, fh;
            if (tab == null || (n = tab.length) == 0 || (f = tabAt(tab, i = (n - 1) & hash)) == null)
                break;
            else if ((fh = f.hash) == MOVED)
                tab = helpTransfer(tab, f);
            else {
                V oldVal = null;
                boolean validated = false;
                synchronized (f) {
                    if (tabAt(tab, i) == f) {
                        if (fh >= 0) {
                            validated = true;
                            for (ConcurrentHashMap1.Node<K,V> e = f, pred = null;;) {
                                K ek;
                                if (e.hash == hash && ((ek = e.key) == key || (ek != null && key.equals(ek)))) {
                                    V ev = e.val;
                                    if (cv == null || cv == ev || (ev != null && cv.equals(ev))) {
                                        oldVal = ev;
                                        if (value != null)
                                            e.val = value;
                                        else if (pred != null)
                                            pred.next = e.next;
                                        else
                                            setTabAt(tab, i, e.next);
                                    }
                                    break;
                                }
                                pred = e;
                                if ((e = e.next) == null)
                                    break;
                            }
                        }
                        else if (f instanceof ConcurrentHashMap1.TreeBin) {
                            validated = true;
                            ConcurrentHashMap1.TreeBin<K,V> t = (ConcurrentHashMap1.TreeBin<K,V>)f;
                            ConcurrentHashMap1.TreeNode<K,V> r, p;
                            if ((r = t.root) != null && (p = r.findTreeNode(hash, key, null)) != null) {
                                V pv = p.val;
                                if (cv == null || cv == pv || (pv != null && cv.equals(pv))) {
                                    oldVal = pv;
                                    if (value != null)
                                        p.val = value;
                                    else if (t.removeTreeNode(p))
                                        setTabAt(tab, i, untreeify(t.first));
                                }
                            }
                        }
                    }
                }
                if (validated) {
                    if (oldVal != null) {
                        if (value == null)
                            addCount(-1L, -1);
                        return oldVal;
                    }
                    break;
                }
            }
        }
        return null;
    }

    public void clear() {
        long delta = 0L; // negative number of deletions
        int i = 0;
        ConcurrentHashMap1.Node<K,V>[] tab = table;
        while (tab != null && i < tab.length) {
            int fh;
            ConcurrentHashMap1.Node<K,V> f = tabAt(tab, i);
            if (f == null)
                ++i;
            else if ((fh = f.hash) == MOVED) {
                tab = helpTransfer(tab, f);
                i = 0; // restart
            }
            else {
                synchronized (f) {
                    if (tabAt(tab, i) == f) {
                        ConcurrentHashMap1.Node<K,V> p = (fh >= 0 ? f :
                                (f instanceof ConcurrentHashMap1.TreeBin) ? ((ConcurrentHashMap1.TreeBin<K,V>)f).first : null);
                        while (p != null) {
                            --delta;
                            p = p.next;
                        }
                        setTabAt(tab, i++, null);
                    }
                }
            }
        }
        if (delta != 0L)
            addCount(delta, -1);
    }

    public V putIfAbsent(K key, V value) {
        return putVal(key, value, true);
    }

    public boolean remove(Object key, Object value) {
        if (key == null)
            throw new NullPointerException();
        return value != null && replaceNode(key, null, value) != null;
    }

    public boolean replace(K key, V oldValue, V newValue) {
        if (key == null || oldValue == null || newValue == null)
            throw new NullPointerException();
        return replaceNode(key, newValue, oldValue) != null;
    }

    public V replace(K key, V value) {
        if (key == null || value == null)
            throw new NullPointerException();
        return replaceNode(key, value, null);
    }

    public V getOrDefault(Object key, V defaultValue) {
        V v;
        return (v = get(key)) == null ? defaultValue : v;
    }

    public boolean contains(Object value) {
        return containsValue(value);
    }

    public long mappingCount() {
        long n = sumCount();
        return (n < 0L) ? 0L : n; // ignore transient negative values
    }

    //ForwardingNode 继承 Node，扩容时使用，表示其他线程正在扩容，并且此节点已经扩容完毕
    //ForwardingNode关联了nextTable,扩容期间可以通过find方法，访问已经迁移到了nextTable中的数据
    static final class ForwardingNode<K,V> extends ConcurrentHashMap1.Node<K,V> {
        final ConcurrentHashMap1.Node<K,V>[] nextTable;
        ForwardingNode(ConcurrentHashMap1.Node<K,V>[] tab) {
            //指定 MOVED 为当前节点的 Hash，标志正在移位
            super(MOVED, null, null, null);
            this.nextTable = tab;
        }

        ConcurrentHashMap1.Node<K,V> find(int h, Object k) {
            outer: for (ConcurrentHashMap1.Node<K,V>[] tab = nextTable;;) {
                ConcurrentHashMap1.Node<K,V> e; int n;
                if (k == null || tab == null || (n = tab.length) == 0 || (e = tabAt(tab, (n - 1) & h)) == null)
                    return null;
                for (;;) {
                    int eh; K ek;
                    if ((eh = e.hash) == h && ((ek = e.key) == k || (ek != null && k.equals(ek))))
                        return e;
                    if (eh < 0) {
                        if (e instanceof ConcurrentHashMap1.ForwardingNode) {
                            tab = ((ConcurrentHashMap1.ForwardingNode<K,V>)e).nextTable;
                            continue outer;
                        }
                        else
                            return e.find(h, k);
                    }
                    if ((e = e.next) == null)
                        return null;
                }
            }
        }
    }

    //返回一个扩容标识,返回的数的第16位肯定为1，这为sc设置为巨大的负数提供了条件
    static final int resizeStamp(int n) {
        return Integer.numberOfLeadingZeros(n) | (1 << (RESIZE_STAMP_BITS - 1));
    }

    //初始table
    private final ConcurrentHashMap1.Node<K,V>[] initTable() {
        ConcurrentHashMap1.Node<K,V>[] tab; int sc;
        while ((tab = table) == null || tab.length == 0) {
            if ((sc = sizeCtl) < 0)
                Thread.yield(); // lost initialization race; just spin
            else if (U.compareAndSwapInt(this, SIZECTL, sc, -1)) {
                try {
                    if ((tab = table) == null || tab.length == 0) {
                        int n = (sc > 0) ? sc : DEFAULT_CAPACITY;
                        @SuppressWarnings("unchecked")
                        ConcurrentHashMap1.Node<K,V>[] nt = (ConcurrentHashMap1.Node<K,V>[])new ConcurrentHashMap1.Node<?,?>[n];
                        table = tab = nt;
                        sc = n - (n >>> 2);
                    }
                } finally {
                    sizeCtl = sc;
                }
                break;
            }
        }
        return tab;
    }

    //
    private final void addCount(long x, int check) {
        ConcurrentHashMap1.CounterCell[] as; long b, s;
        if ((as = counterCells) != null || !U.compareAndSwapLong(this, BASECOUNT, b = baseCount, s = b + x)) {
            ConcurrentHashMap1.CounterCell a=null; long v; int m;
            boolean uncontended = true;
            if (as == null || (m = as.length - 1) < 0 ||
                   // (a = as[ThreadLocalRandom.getProbe() & m]) == null ||
                    !(uncontended =U.compareAndSwapLong(a, CELLVALUE, v = a.value, v + x))) {
                fullAddCount(x, uncontended);
                return;
            }
            if (check <= 1)
                return;
            s = sumCount();
        }
        if (check >= 0) {
            ConcurrentHashMap1.Node<K,V>[] tab, nt; int n, sc;
            while (s >= (long)(sc = sizeCtl) && (tab = table) != null && (n = tab.length) < MAXIMUM_CAPACITY) {
                int rs = resizeStamp(n);
                if (sc < 0) {
                    if ((sc >>> RESIZE_STAMP_SHIFT) != rs || sc == rs + 1 || sc == rs + MAX_RESIZERS
                            || (nt = nextTable) == null || transferIndex <= 0)
                        break;
                    if (U.compareAndSwapInt(this, SIZECTL, sc, sc + 1))
                        //调用transfer()扩容
                        transfer(tab, nt);
                }
                else if (U.compareAndSwapInt(this, SIZECTL, sc,
                        (rs << RESIZE_STAMP_SHIFT) + 2))
                    //调用transfer()扩容
                    transfer(tab, null);
                s = sumCount();
            }
        }
    }

    //帮助扩容
    final ConcurrentHashMap1.Node<K,V>[] helpTransfer(ConcurrentHashMap1.Node<K,V>[] tab, ConcurrentHashMap1.Node<K,V> f) {
        ConcurrentHashMap1.Node<K,V>[] nextTab; int sc;
        if (tab != null && (f instanceof ConcurrentHashMap1.ForwardingNode) &&
                (nextTab = ((ConcurrentHashMap1.ForwardingNode<K,V>)f).nextTable) != null) {
            int rs = resizeStamp(tab.length);
            while (nextTab == nextTable && table == tab && (sc = sizeCtl) < 0) {
                if ((sc >>> RESIZE_STAMP_SHIFT) != rs || sc == rs + 1 || sc == rs + MAX_RESIZERS || transferIndex <= 0)
                    break;
                if (U.compareAndSwapInt(this, SIZECTL, sc, sc + 1)) {
                    transfer(tab, nextTab);
                    break;
                }
            }
            return nextTab;
        }
        return table;
    }

    //调整数组的的大小,协调多个线程如何调用transfer方法进行hash桶的迁移
    private final void tryPresize(int size) {
        int c = (size >= (MAXIMUM_CAPACITY >>> 1)) ? MAXIMUM_CAPACITY : tableSizeFor(size + (size >>> 1) + 1);
        int sc;
        while ((sc = sizeCtl) >= 0) {
            ConcurrentHashMap1.Node<K,V>[] tab = table; int n;
            //tab还未被初始化
            if (tab == null || (n = tab.length) == 0) {
                n = (sc > c) ? sc : c;
                //CAS 抢夺 SIZECTL 设置权限
                if (U.compareAndSwapInt(this, SIZECTL, sc, -1)) {
                    try {
                        if (table == tab) {
                            @SuppressWarnings("unchecked")
                            ConcurrentHashMap1.Node<K,V>[] nt = (ConcurrentHashMap1.Node<K,V>[])new ConcurrentHashMap1.Node<?,?>[n];
                            table = nt;
                            sc = n - (n >>> 2);
                        }
                    } finally {
                        sizeCtl = sc;
                    }
                }
            }
            else if (c <= sc || n >= MAXIMUM_CAPACITY)
                break;
            else if (tab == table) {
                int rs = resizeStamp(n);
                if (sc < 0) {
                    ConcurrentHashMap1.Node<K,V>[] nt;
                    if ((sc >>> RESIZE_STAMP_SHIFT) != rs || sc == rs + 1 ||
                            sc == rs + MAX_RESIZERS || (nt = nextTable) == null || transferIndex <= 0)
                        break;
                    if (U.compareAndSwapInt(this, SIZECTL, sc, sc + 1))
                        transfer(tab, nt);
                }
                else if (U.compareAndSwapInt(this, SIZECTL, sc, (rs << RESIZE_STAMP_SHIFT) + 2))
                    transfer(tab, null);
            }
        }
    }

    //扩容,参数tab表示旧的哈希表，参数nextTab表示构造的新哈希表，要么传入ConcurrentHashMap的nextTable属性，要么传入null。
    private final void transfer(ConcurrentHashMap1.Node<K,V>[] tab, ConcurrentHashMap1.Node<K,V>[] nextTab) {
        int n = tab.length, stride;
        //stride决定一个线程处理的哈希桶数量
        //n >>> 3 表示表长度除以8
        if ((stride = (NCPU > 1) ? (n >>> 3) / NCPU : n) < MIN_TRANSFER_STRIDE)
            //确保每次迁移的node个数不少于16个
            stride = MIN_TRANSFER_STRIDE; // subdivide range
        if (nextTab == null) {            // initiating
            try {
                //构造一个新的数组容量为旧数据的两倍
                ConcurrentHashMap1.Node<K,V>[] nt = (ConcurrentHashMap1.Node<K,V>[])new ConcurrentHashMap1.Node<?,?>[n << 1];
                nextTab = nt;
            } catch (Throwable ex) {      // try to cope with OOME
                sizeCtl = Integer.MAX_VALUE;
                return;
            }
            //赋值给成员属性，防止在多线程下多次初始化
            nextTable = nextTab;
            transferIndex = n;
        }
        //新哈希表长度
        int nextn = nextTab.length;
        //占位节点。并且哈希值为-1，对应为成员字段MOVED，用于标识正在扩容
        ConcurrentHashMap1.ForwardingNode<K,V> fwd = new ConcurrentHashMap1.ForwardingNode<K,V>(nextTab);
        //advance用于提示代码是否进行推进处理，也就是当前桶处理完，处理下一个桶的标识
        boolean advance = true;
        //finishing变量用于提示扩容是否结束用的
        boolean finishing = false; // to ensure sweep before committing nextTab
        //for循环就是用来做旧表迁移到新表的逻辑,一直循环,直到迁移完成。
        //i表示当前线程处理哈希桶的初始下标，bound表示当前线程处理哈希桶的结束下标
        for (int i = 0, bound = 0;;) {
            ConcurrentHashMap1.Node<K,V> f; int fh;
            while (advance) {
                int nextIndex, nextBound;
                //--i 是表示处理区间的下一个哈希桶元素列表，因为上次已经处理过i位置。
                if (--i >= bound || finishing)
                    advance = false;
                else if ((nextIndex = transferIndex) <= 0) {
                    //如果下标不合法就结束
                    i = -1;
                    advance = false;
                }
                //cas方式设置ConcurrentHashMap的transferIndex属性,transferIndex在transfer初始化阶段赋值为旧哈希表长度,nextIndex此时等于transferIndex。
                //nextIndex > stride 如果当前分割点位置(transferIndex)大于最大处理阈值stride，就返回nextIndex - stride 作为处理哈希桶区间的起始下标，否则是0
                else if (U.compareAndSwapInt
                        (this, TRANSFERINDEX, nextIndex, nextBound = (nextIndex > stride ? nextIndex - stride : 0))) {
                    bound = nextBound;
                    i = nextIndex - 1;
                    advance = false;
                }
            }
            if (i < 0 || i >= n || i + n >= nextn) {
                int sc;
                if (finishing) {
                    nextTable = null;
                    table = nextTab;
                    sizeCtl = (n << 1) - (n >>> 1);
                    return;
                }
                if (U.compareAndSwapInt(this, SIZECTL, sc = sizeCtl, sc - 1)) {
                    if ((sc - 2) != resizeStamp(n) << RESIZE_STAMP_SHIFT)
                        return;
                    finishing = advance = true;
                    i = n; // recheck before commit
                }
            }
            else if ((f = tabAt(tab, i)) == null)
                advance = casTabAt(tab, i, null, fwd);
            else if ((fh = f.hash) == MOVED)
                advance = true; // already processed
            else {
                synchronized (f) {
                    if (tabAt(tab, i) == f) {
                        ConcurrentHashMap1.Node<K,V> ln, hn;
                        if (fh >= 0) {
                            //链表哈希桶 迁移到新的扩容数组。
                            int runBit = fh & n;
                            ConcurrentHashMap1.Node<K,V> lastRun = f;
                            for (ConcurrentHashMap1.Node<K,V> p = f.next; p != null; p = p.next) {
                                int b = p.hash & n;
                                if (b != runBit) {
                                    runBit = b;
                                    lastRun = p;
                                }
                            }
                            if (runBit == 0) {
                                ln = lastRun;
                                hn = null;
                            }
                            else {
                                hn = lastRun;
                                ln = null;
                            }
                            for (ConcurrentHashMap1.Node<K,V> p = f; p != lastRun; p = p.next) {
                                int ph = p.hash; K pk = p.key; V pv = p.val;
                                if ((ph & n) == 0)
                                    ln = new ConcurrentHashMap1.Node<K,V>(ph, pk, pv, ln);
                                else
                                    hn = new ConcurrentHashMap1.Node<K,V>(ph, pk, pv, hn);
                            }
                            setTabAt(nextTab, i, ln);
                            setTabAt(nextTab, i + n, hn);
                            setTabAt(tab, i, fwd);
                            advance = true;
                        }
                        else if (f instanceof ConcurrentHashMap1.TreeBin) {
                            //红黑树哈希桶,迁移到新的扩容数组
                            ConcurrentHashMap1.TreeBin<K,V> t = (ConcurrentHashMap1.TreeBin<K,V>)f;
                            ConcurrentHashMap1.TreeNode<K,V> lo = null, loTail = null;
                            ConcurrentHashMap1.TreeNode<K,V> hi = null, hiTail = null;
                            int lc = 0, hc = 0;
                            for (ConcurrentHashMap1.Node<K,V> e = t.first; e != null; e = e.next) {
                                int h = e.hash;
                                ConcurrentHashMap1.TreeNode<K,V> p = new ConcurrentHashMap1.TreeNode<K,V>
                                        (h, e.key, e.val, null, null);
                                if ((h & n) == 0) {
                                    if ((p.prev = loTail) == null)
                                        lo = p;
                                    else
                                        loTail.next = p;
                                    loTail = p;
                                    ++lc;
                                }
                                else {
                                    if ((p.prev = hiTail) == null)
                                        hi = p;
                                    else
                                        hiTail.next = p;
                                    hiTail = p;
                                    ++hc;
                                }
                            }
                            ln = (lc <= UNTREEIFY_THRESHOLD) ? untreeify(lo) :
                                    (hc != 0) ? new ConcurrentHashMap1.TreeBin<K,V>(lo) : t;
                            hn = (hc <= UNTREEIFY_THRESHOLD) ? untreeify(hi) :
                                    (lc != 0) ? new ConcurrentHashMap1.TreeBin<K,V>(hi) : t;
                            setTabAt(nextTab, i, ln);
                            setTabAt(nextTab, i + n, hn);
                            setTabAt(tab, i, fwd);
                            advance = true;
                        }
                    }
                }
            }
        }
    }

    //用于对baseCount的计数
    @sun.misc.Contended static final class CounterCell {
        volatile long value;
        CounterCell(long x) { value = x; }
    }

    final long sumCount() {
        ConcurrentHashMap1.CounterCell[] as = counterCells; ConcurrentHashMap1.CounterCell a;
        long sum = baseCount;
        if (as != null) {
            for (int i = 0; i < as.length; ++i) {
                if ((a = as[i]) != null)
                    sum += a.value;
            }
        }
        return sum;
    }

    private final void fullAddCount(long x, boolean wasUncontended) {
        int h=0;
//        int h;
//        if ((h = ThreadLocalRandom.getProbe()) == 0) {
//            ThreadLocalRandom.localInit();      // force initialization
//            h = ThreadLocalRandom.getProbe();
//            wasUncontended = true;
//        }
        boolean collide = false;                // True if last slot nonempty
        for (;;) {
            ConcurrentHashMap1.CounterCell[] as; ConcurrentHashMap1.CounterCell a; int n; long v;
            if ((as = counterCells) != null && (n = as.length) > 0) {
                if ((a = as[(n - 1) & h]) == null) {
                    if (cellsBusy == 0) {            // Try to attach new Cell
                        ConcurrentHashMap1.CounterCell r = new ConcurrentHashMap1.CounterCell(x); // Optimistic create
                        if (cellsBusy == 0 && U.compareAndSwapInt(this, CELLSBUSY, 0, 1)) {
                            boolean created = false;
                            try {               // Recheck under lock
                                ConcurrentHashMap1.CounterCell[] rs; int m, j;
                                if ((rs = counterCells) != null && (m = rs.length) > 0 && rs[j = (m - 1) & h] == null) {
                                    rs[j] = r;
                                    created = true;
                                }
                            } finally {
                                cellsBusy = 0;
                            }
                            if (created)
                                break;
                            continue;           // Slot is now non-empty
                        }
                    }
                    collide = false;
                }
                else if (!wasUncontended)       // CAS already known to fail
                    wasUncontended = true;      // Continue after rehash
                else if (U.compareAndSwapLong(a, CELLVALUE, v = a.value, v + x))
                    break;
                else if (counterCells != as || n >= NCPU)
                    collide = false;            // At max size or stale
                else if (!collide)
                    collide = true;
                else if (cellsBusy == 0 &&
                        U.compareAndSwapInt(this, CELLSBUSY, 0, 1)) {
                    try {
                        if (counterCells == as) {// Expand table unless stale
                            ConcurrentHashMap1.CounterCell[] rs = new ConcurrentHashMap1.CounterCell[n << 1];
                            for (int i = 0; i < n; ++i)
                                rs[i] = as[i];
                            counterCells = rs;
                        }
                    } finally {
                        cellsBusy = 0;
                    }
                    collide = false;
                    continue;                   // Retry with expanded table
                }
                //h = ThreadLocalRandom.advanceProbe(h);
            }
            else if (cellsBusy == 0 && counterCells == as && U.compareAndSwapInt(this, CELLSBUSY, 0, 1)) {
                boolean init = false;
                try {                           // Initialize table
                    if (counterCells == as) {
                        ConcurrentHashMap1.CounterCell[] rs = new ConcurrentHashMap1.CounterCell[2];
                        rs[h & 1] = new ConcurrentHashMap1.CounterCell(x);
                        counterCells = rs;
                        init = true;
                    }
                } finally {
                    cellsBusy = 0;
                }
                if (init)
                    break;
            }
            else if (U.compareAndSwapLong(this, BASECOUNT, v = baseCount, v + x))
                break;                          // Fall back on using base
        }
    }

    //链表转换成红黑树
    private final void treeifyBin(ConcurrentHashMap1.Node<K,V>[] tab, int index) {
        ConcurrentHashMap1.Node<K,V> b; int n, sc;
        if (tab != null) {
            if ((n = tab.length) < MIN_TREEIFY_CAPACITY)
                //table进行扩容
                tryPresize(n << 1);
            else if ((b = tabAt(tab, index)) != null && b.hash >= 0) {
                //头节点b加锁
                synchronized (b) {
                    if (tabAt(tab, index) == b) {
                        ConcurrentHashMap1.TreeNode<K,V> hd = null, tl = null;
                        //for循环,把桶位中的单链表转换成双向链表，便于树化,hd指向双向列表的头部，tl指向双向链表的尾部
                        for (ConcurrentHashMap1.Node<K,V> e = b; e != null; e = e.next) {
                            ConcurrentHashMap1.TreeNode<K,V> p = new ConcurrentHashMap1.TreeNode<K,V>(e.hash,e.key,e.val,null,null);
                            if ((p.prev = tl) == null)
                                hd = p;
                            else
                                tl.next = p;
                            tl = p;
                        }
                        //把node单链表转换的双向链表转换成TreeBin对象
                        setTabAt(tab, index, new ConcurrentHashMap1.TreeBin<K,V>(hd));
                    }
                }
            }
        }
    }

    static <K,V> ConcurrentHashMap1.Node<K,V> untreeify(ConcurrentHashMap1.Node<K,V> b) {
        ConcurrentHashMap1.Node<K,V> hd = null, tl = null;
        for (ConcurrentHashMap1.Node<K,V> q = b; q != null; q = q.next) {
            ConcurrentHashMap1.Node<K,V> p = new ConcurrentHashMap1.Node<K,V>(q.hash, q.key, q.val, null);
            if (tl == null)
                hd = p;
            else
                tl.next = p;
            tl = p;
        }
        return hd;
    }

    //树节点
    static final class TreeNode<K,V> extends ConcurrentHashMap1.Node<K,V> {
        ConcurrentHashMap1.TreeNode<K,V> parent;  // red-black tree links
        ConcurrentHashMap1.TreeNode<K,V> left;
        ConcurrentHashMap1.TreeNode<K,V> right;
        ConcurrentHashMap1.TreeNode<K,V> prev;    // needed to unlink next upon deletion
        boolean red;

        TreeNode(int hash, K key, V val, ConcurrentHashMap1.Node<K,V> next,
                 ConcurrentHashMap1.TreeNode<K,V> parent) {
            super(hash, key, val, next);
            this.parent = parent;
        }

        ConcurrentHashMap1.Node<K,V> find(int h, Object k) {
            return findTreeNode(h, k, null);
        }

        final ConcurrentHashMap1.TreeNode<K,V> findTreeNode(int h, Object k, Class<?> kc) {
            if (k != null) {
                ConcurrentHashMap1.TreeNode<K,V> p = this;
                do  {
                    int ph, dir; K pk; ConcurrentHashMap1.TreeNode<K,V> q;
                    ConcurrentHashMap1.TreeNode<K,V> pl = p.left, pr = p.right;
                    if ((ph = p.hash) > h)
                        p = pl;
                    else if (ph < h)
                        p = pr;
                    else if ((pk = p.key) == k || (pk != null && k.equals(pk)))
                        return p;
                    else if (pl == null)
                        p = pr;
                    else if (pr == null)
                        p = pl;
                    else if ((kc != null ||
                            (kc = comparableClassFor(k)) != null) &&
                            (dir = compareComparables(kc, k, pk)) != 0)
                        p = (dir < 0) ? pl : pr;
                    else if ((q = pr.findTreeNode(h, k, kc)) != null)
                        return q;
                    else
                        p = pl;
                } while (p != null);
            }
            return null;
        }
    }

    //封装TreeNode节点
    static final class TreeBin<K,V> extends ConcurrentHashMap1.Node<K,V> {

        ConcurrentHashMap1.TreeNode<K,V> root;
        volatile ConcurrentHashMap1.TreeNode<K,V> first;
        //等待者线程
        volatile Thread waiter;
        //锁的状态：
        //1.写锁状态 写是独占状态，以散列表来看，真正进入到TreeBin中的写线程 同一时刻只能有一个线程。
        //2.读锁状态 读锁是共享，同一时刻可以有多个线程 同时进入到 TreeBin对象中获取数据。每一个线程都会给lockState=4
        //3.等待者状态（写线程在等待），当TreeBin中有读线程目前正在读取数据时，写线程无法修改数据，
        //那么就将lockState的最低2位设置为 0b 10 ：即，换算成十进制就是WAITER = 2;
        volatile int lockState;
        // values for lockState
        static final int WRITER = 1; // set while holding write lock
        static final int WAITER = 2; // set when waiting for write lock
        static final int READER = 4; // increment value for setting read lock

        static int tieBreakOrder(Object a, Object b) {
            int d;
            if (a == null || b == null || (d = a.getClass().getName().compareTo(b.getClass().getName())) == 0)
                d = (System.identityHashCode(a) <= System.identityHashCode(b) ? -1 : 1);
            return d;
        }

        TreeBin(ConcurrentHashMap1.TreeNode<K,V> b) {
            //设置当前节点hash为-2 表示此节点是TreeBin节点
            super(TREEBIN, null, null, null);
            //使用first引用treeNode链表
            this.first = b;
            ConcurrentHashMap1.TreeNode<K,V> r = null;
            for (ConcurrentHashMap1.TreeNode<K,V> x = b, next; x != null; x = next) {
                next = (ConcurrentHashMap1.TreeNode<K,V>)x.next;
                x.left = x.right = null;
                //如果当前红黑树是一个空树，那么设置插入元素为根节点
                //第一次循环，r一定是null
                if (r == null) {
                    x.parent = null;
                    //颜色改为黑色
                    x.red = false;
                    r = x;
                }
                else {
                    //红黑树根节点已经有数据了,k 表示插入节点的key
                    K k = x.key;
                    int h = x.hash;
                    Class<?> kc = null;

                    //这里的for循环，是一个查找并插入的过程
                    for (ConcurrentHashMap1.TreeNode<K,V> p = r;;) {
                        int dir, ph;
                        K pk = p.key;
                        if ((ph = p.hash) > h)
                            dir = -1;
                        else if (ph < h)
                            dir = 1;
                        else if ((kc == null &&
                                (kc = comparableClassFor(k)) == null) ||
                                (dir = compareComparables(kc, k, pk)) == 0)
                            dir = tieBreakOrder(k, pk);
                        ConcurrentHashMap1.TreeNode<K,V> xp = p;
                        if ((p = (dir <= 0) ? p.left : p.right) == null) {
                            x.parent = xp;
                            if (dir <= 0)
                                xp.left = x;
                            else
                                xp.right = x;
                            r = balanceInsertion(r, x);
                            break;
                        }
                    }
                }
            }
            this.root = r;
            assert checkInvariants(root);
        }

        //加锁：基于CAS的方式更新LOCKSTATE的值，期望值是0，更新值是WRITER（1，写锁）
        private final void lockRoot() {
            if (!U.compareAndSwapInt(this, LOCKSTATE, 0, WRITER))
                //竞争锁
                contendedLock(); // offload to separate method
        }

        //释放锁
        private final void unlockRoot() {
            lockState = 0;
        }

        //阻塞等待root锁
        private final void contendedLock() {
            boolean waiting = false;
            for (int s;;) {
                //没有线程持有写锁时，尝试获取写锁
                if (((s = lockState) & ~WAITER) == 0) {
                    if (U.compareAndSwapInt(this, LOCKSTATE, s, WRITER)) {
                        //拿到锁后将等待线程清空（等待线程是它自己）
                        if (waiting)
                            waiter = null;
                        return;
                    }
                }
                //有线程持有写锁且本线程状态不为WAITER时
                else if ((s & WAITER) == 0) {
                    //尝试占有waiting线程
                    if (U.compareAndSwapInt(this, LOCKSTATE, s, s | WAITER)) {
                        waiting = true;
                        waiter = Thread.currentThread();
                    }
                }
                //有线程持有写锁且本线程状态为WAITER时，堵塞自己
                else if (waiting)
                    LockSupport.park(this);
            }
        }

        final ConcurrentHashMap1.Node<K,V> find(int h, Object k) {
            if (k != null) {
                for (ConcurrentHashMap1.Node<K,V> e = first; e != null; ) {
                    int s; K ek;
                    //lockState & 0011 != 0 条件成立，说明当前TreeBin有等待者线程或者写操作线程正在加锁
                    if (((s = lockState) & (WAITER|WRITER)) != 0) {
                        if (e.hash == h && ((ek = e.key) == k || (ek != null && k.equals(ek))))
                            return e;
                        e = e.next;
                    }
                    //添加读锁成功
                    else if (U.compareAndSwapInt(this, LOCKSTATE, s, s + READER)) {
                        ConcurrentHashMap1.TreeNode<K,V> r, p;
                        try {
                            p = ((r = root) == null ? null : r.findTreeNode(h, k, null));
                        } finally {
                            Thread w;
                            //(w = waiter) != null 说明有一个写线程在等待读操作全部结束
                            if (U.getAndAddInt(this, LOCKSTATE, -READER) == (READER|WAITER) && (w = waiter) != null)
                                //使用unpark让写线程恢复运行状态
                                LockSupport.unpark(w);
                        }
                        return p;
                    }
                }
            }
            return null;
        }

        final ConcurrentHashMap1.TreeNode<K,V> putTreeVal(int h, K k, V v) {
            Class<?> kc = null;
            boolean searched = false;
            for (ConcurrentHashMap1.TreeNode<K,V> p = root;;) {
                int dir, ph; K pk;
                if (p == null) {
                    first = root = new ConcurrentHashMap1.TreeNode<K,V>(h, k, v, null, null);
                    break;
                }
                else if ((ph = p.hash) > h)
                    dir = -1;
                else if (ph < h)
                    dir = 1;
                else if ((pk = p.key) == k || (pk != null && k.equals(pk)))
                    return p;
                else if ((kc == null && (kc = comparableClassFor(k)) == null) || (dir = compareComparables(kc, k, pk)) == 0) {
                    if (!searched) {
                        ConcurrentHashMap1.TreeNode<K,V> q, ch;
                        searched = true;
                        if (((ch = p.left) != null && (q = ch.findTreeNode(h, k, kc)) != null) ||
                                ((ch = p.right) != null && (q = ch.findTreeNode(h, k, kc)) != null))
                            return q;
                    }
                    dir = tieBreakOrder(k, pk);
                }

                ConcurrentHashMap1.TreeNode<K,V> xp = p;
                if ((p = (dir <= 0) ? p.left : p.right) == null) {
                    ConcurrentHashMap1.TreeNode<K,V> x, f = first;
                    first = x = new ConcurrentHashMap1.TreeNode<K,V>(h, k, v, f, xp);
                    if (f != null)
                        f.prev = x;
                    if (dir <= 0)
                        xp.left = x;
                    else
                        xp.right = x;
                    if (!xp.red)
                        x.red = true;
                    else {
                        lockRoot();
                        try {
                            //平衡红黑树，使其再次符合规范
                            root = balanceInsertion(root, x);
                        } finally {
                            unlockRoot();
                        }
                    }
                    break;
                }
            }
            assert checkInvariants(root);
            return null;
        }

        final boolean removeTreeNode(ConcurrentHashMap1.TreeNode<K,V> p) {
            ConcurrentHashMap1.TreeNode<K,V> next = (ConcurrentHashMap1.TreeNode<K,V>)p.next;
            ConcurrentHashMap1.TreeNode<K,V> pred = p.prev;  // unlink traversal pointers
            ConcurrentHashMap1.TreeNode<K,V> r, rl;
            if (pred == null)
                first = next;
            else
                pred.next = next;
            if (next != null)
                next.prev = pred;
            if (first == null) {
                root = null;
                return true;
            }
            if ((r = root) == null || r.right == null || // too small
                    (rl = r.left) == null || rl.left == null)
                return true;
            lockRoot();
            try {
                ConcurrentHashMap1.TreeNode<K,V> replacement;
                ConcurrentHashMap1.TreeNode<K,V> pl = p.left;
                ConcurrentHashMap1.TreeNode<K,V> pr = p.right;
                if (pl != null && pr != null) {
                    ConcurrentHashMap1.TreeNode<K,V> s = pr, sl;
                    while ((sl = s.left) != null) // find successor
                        s = sl;
                    boolean c = s.red; s.red = p.red; p.red = c; // swap colors
                    ConcurrentHashMap1.TreeNode<K,V> sr = s.right;
                    ConcurrentHashMap1.TreeNode<K,V> pp = p.parent;
                    if (s == pr) { // p was s's direct parent
                        p.parent = s;
                        s.right = p;
                    }
                    else {
                        ConcurrentHashMap1.TreeNode<K,V> sp = s.parent;
                        if ((p.parent = sp) != null) {
                            if (s == sp.left)
                                sp.left = p;
                            else
                                sp.right = p;
                        }
                        if ((s.right = pr) != null)
                            pr.parent = s;
                    }
                    p.left = null;
                    if ((p.right = sr) != null)
                        sr.parent = p;
                    if ((s.left = pl) != null)
                        pl.parent = s;
                    if ((s.parent = pp) == null)
                        r = s;
                    else if (p == pp.left)
                        pp.left = s;
                    else
                        pp.right = s;
                    if (sr != null)
                        replacement = sr;
                    else
                        replacement = p;
                }
                else if (pl != null)
                    replacement = pl;
                else if (pr != null)
                    replacement = pr;
                else
                    replacement = p;
                if (replacement != p) {
                    ConcurrentHashMap1.TreeNode<K,V> pp = replacement.parent = p.parent;
                    if (pp == null)
                        r = replacement;
                    else if (p == pp.left)
                        pp.left = replacement;
                    else
                        pp.right = replacement;
                    p.left = p.right = p.parent = null;
                }

                root = (p.red) ? r : balanceDeletion(r, replacement);

                if (p == replacement) {  // detach pointers
                    ConcurrentHashMap1.TreeNode<K,V> pp;
                    if ((pp = p.parent) != null) {
                        if (p == pp.left)
                            pp.left = null;
                        else if (p == pp.right)
                            pp.right = null;
                        p.parent = null;
                    }
                }
            } finally {
                unlockRoot();
            }
            assert checkInvariants(root);
            return false;
        }

        static <K,V> ConcurrentHashMap1.TreeNode<K,V> rotateLeft(ConcurrentHashMap1.TreeNode<K,V> root,
                                                                ConcurrentHashMap1.TreeNode<K,V> p) {
            ConcurrentHashMap1.TreeNode<K,V> r, pp, rl;
            if (p != null && (r = p.right) != null) {
                if ((rl = p.right = r.left) != null)
                    rl.parent = p;
                if ((pp = r.parent = p.parent) == null)
                    (root = r).red = false;
                else if (pp.left == p)
                    pp.left = r;
                else
                    pp.right = r;
                r.left = p;
                p.parent = r;
            }
            return root;
        }

        static <K,V> ConcurrentHashMap1.TreeNode<K,V> rotateRight(ConcurrentHashMap1.TreeNode<K,V> root,
                                                                 ConcurrentHashMap1.TreeNode<K,V> p) {
            ConcurrentHashMap1.TreeNode<K,V> l, pp, lr;
            if (p != null && (l = p.left) != null) {
                if ((lr = p.left = l.right) != null)
                    lr.parent = p;
                if ((pp = l.parent = p.parent) == null)
                    (root = l).red = false;
                else if (pp.right == p)
                    pp.right = l;
                else
                    pp.left = l;
                l.right = p;
                p.parent = l;
            }
            return root;
        }

        static <K,V> ConcurrentHashMap1.TreeNode<K,V> balanceInsertion(ConcurrentHashMap1.TreeNode<K,V> root,
                                                                      ConcurrentHashMap1.TreeNode<K,V> x) {
            x.red = true;
            for (ConcurrentHashMap1.TreeNode<K,V> xp, xpp, xppl, xppr;;) {
                if ((xp = x.parent) == null) {
                    x.red = false;
                    return x;
                }
                else if (!xp.red || (xpp = xp.parent) == null)
                    return root;
                if (xp == (xppl = xpp.left)) {
                    if ((xppr = xpp.right) != null && xppr.red) {
                        xppr.red = false;
                        xp.red = false;
                        xpp.red = true;
                        x = xpp;
                    }
                    else {
                        if (x == xp.right) {
                            root = rotateLeft(root, x = xp);
                            xpp = (xp = x.parent) == null ? null : xp.parent;
                        }
                        if (xp != null) {
                            xp.red = false;
                            if (xpp != null) {
                                xpp.red = true;
                                root = rotateRight(root, xpp);
                            }
                        }
                    }
                }
                else {
                    if (xppl != null && xppl.red) {
                        xppl.red = false;
                        xp.red = false;
                        xpp.red = true;
                        x = xpp;
                    }
                    else {
                        if (x == xp.left) {
                            root = rotateRight(root, x = xp);
                            xpp = (xp = x.parent) == null ? null : xp.parent;
                        }
                        if (xp != null) {
                            xp.red = false;
                            if (xpp != null) {
                                xpp.red = true;
                                root = rotateLeft(root, xpp);
                            }
                        }
                    }
                }
            }
        }

        static <K,V> ConcurrentHashMap1.TreeNode<K,V> balanceDeletion(ConcurrentHashMap1.TreeNode<K,V> root,
                                                                     ConcurrentHashMap1.TreeNode<K,V> x) {
            for (ConcurrentHashMap1.TreeNode<K,V> xp, xpl, xpr;;)  {
                if (x == null || x == root)
                    return root;
                else if ((xp = x.parent) == null) {
                    x.red = false;
                    return x;
                }
                else if (x.red) {
                    x.red = false;
                    return root;
                }
                else if ((xpl = xp.left) == x) {
                    if ((xpr = xp.right) != null && xpr.red) {
                        xpr.red = false;
                        xp.red = true;
                        root = rotateLeft(root, xp);
                        xpr = (xp = x.parent) == null ? null : xp.right;
                    }
                    if (xpr == null)
                        x = xp;
                    else {
                        ConcurrentHashMap1.TreeNode<K,V> sl = xpr.left, sr = xpr.right;
                        if ((sr == null || !sr.red) &&
                                (sl == null || !sl.red)) {
                            xpr.red = true;
                            x = xp;
                        }
                        else {
                            if (sr == null || !sr.red) {
                                if (sl != null)
                                    sl.red = false;
                                xpr.red = true;
                                root = rotateRight(root, xpr);
                                xpr = (xp = x.parent) == null ?
                                        null : xp.right;
                            }
                            if (xpr != null) {
                                xpr.red = (xp == null) ? false : xp.red;
                                if ((sr = xpr.right) != null)
                                    sr.red = false;
                            }
                            if (xp != null) {
                                xp.red = false;
                                root = rotateLeft(root, xp);
                            }
                            x = root;
                        }
                    }
                }
                else { // symmetric
                    if (xpl != null && xpl.red) {
                        xpl.red = false;
                        xp.red = true;
                        root = rotateRight(root, xp);
                        xpl = (xp = x.parent) == null ? null : xp.left;
                    }
                    if (xpl == null)
                        x = xp;
                    else {
                        ConcurrentHashMap1.TreeNode<K,V> sl = xpl.left, sr = xpl.right;
                        if ((sl == null || !sl.red) &&
                                (sr == null || !sr.red)) {
                            xpl.red = true;
                            x = xp;
                        }
                        else {
                            if (sl == null || !sl.red) {
                                if (sr != null)
                                    sr.red = false;
                                xpl.red = true;
                                root = rotateLeft(root, xpl);
                                xpl = (xp = x.parent) == null ?
                                        null : xp.left;
                            }
                            if (xpl != null) {
                                xpl.red = (xp == null) ? false : xp.red;
                                if ((sl = xpl.left) != null)
                                    sl.red = false;
                            }
                            if (xp != null) {
                                xp.red = false;
                                root = rotateRight(root, xp);
                            }
                            x = root;
                        }
                    }
                }
            }
        }

        static <K,V> boolean checkInvariants(ConcurrentHashMap1.TreeNode<K,V> t) {
            ConcurrentHashMap1.TreeNode<K,V> tp = t.parent, tl = t.left, tr = t.right,
                    tb = t.prev, tn = (ConcurrentHashMap1.TreeNode<K,V>)t.next;
            if (tb != null && tb.next != t)
                return false;
            if (tn != null && tn.prev != t)
                return false;
            if (tp != null && t != tp.left && t != tp.right)
                return false;
            if (tl != null && (tl.parent != t || tl.hash > t.hash))
                return false;
            if (tr != null && (tr.parent != t || tr.hash < t.hash))
                return false;
            if (t.red && tl != null && tl.red && tr != null && tr.red)
                return false;
            if (tl != null && !checkInvariants(tl))
                return false;
            if (tr != null && !checkInvariants(tr))
                return false;
            return true;
        }

        private static final sun.misc.Unsafe U;

        private static final long LOCKSTATE;

        static {
            try {
                U = sun.misc.Unsafe.getUnsafe();
                Class<?> k = ConcurrentHashMap1.TreeBin.class;
                LOCKSTATE = U.objectFieldOffset(k.getDeclaredField("lockState"));
            } catch (Exception e) {
                throw new Error(e);
            }
        }
    }

    //正在遍历的数组以及索引信息
    static final class TableStack<K,V> {
        //数组的长度
        int length;
        //遍历时的索引
        int index;
        //数组
        ConcurrentHashMap1.Node<K,V>[] tab;
        //next 指针
        ConcurrentHashMap1.TableStack<K,V> next;
    }

    //主要用于遍历操作
    static class Traverser<K,V> {
        //数组
        ConcurrentHashMap1.Node<K,V>[] tab;        // current table; updated if resized
        //下一元素的指针
        ConcurrentHashMap1.Node<K,V> next;         // the next entry to use
        //stack 栈顶结点， spare 备用节点，可以理解为中继节点
        ConcurrentHashMap1.TableStack<K,V> stack, spare; // to save/restore on ForwardingNodes
        int index;              // index of bin to use next
        int baseIndex;          // current index of initial table
        int baseLimit;          // index bound for initial table
        final int baseSize;     // initial table size

        Traverser(ConcurrentHashMap1.Node<K,V>[] tab, int size, int index, int limit) {
            this.tab = tab;
            this.baseSize = size;
            this.baseIndex = this.index = index;
            this.baseLimit = limit;
            this.next = null;
        }

        //获取下一元素，不存在则返回null
        final ConcurrentHashMap1.Node<K,V> advance() {
            ConcurrentHashMap1.Node<K,V> e;
            if ((e = next) != null)
                e = e.next;
            for (;;) {
                ConcurrentHashMap1.Node<K,V>[] t; int i, n;  // must use locals in checks
                if (e != null)
                    return next = e;
                if (baseIndex >= baseLimit || (t = tab) == null || (n = t.length) <= (i = index) || i < 0)
                    return next = null;
                if ((e = tabAt(t, i)) != null && e.hash < 0) {
                    ///是否正在扩容
                    if (e instanceof ConcurrentHashMap1.ForwardingNode) {
                        tab = ((ConcurrentHashMap1.ForwardingNode<K,V>)e).nextTable;
                        e = null;
                        //保存当前遍历状态
                        pushState(t, i, n);
                        continue;
                    }
                    else if (e instanceof ConcurrentHashMap1.TreeBin)
                        e = ((ConcurrentHashMap1.TreeBin<K,V>)e).first;
                    else
                        e = null;
                }
                if (stack != null)
                    recoverState(n);
                else if ((index = i + baseSize) >= n)
                    index = ++baseIndex; // visit upper slots if present
            }
        }

        //保存当前遍历状态
        private void pushState(ConcurrentHashMap1.Node<K,V>[] t, int i, int n) {
            ConcurrentHashMap1.TableStack<K,V> s = spare;  // reuse if possible
            if (s != null)
                spare = s.next;
            else
                s = new ConcurrentHashMap1.TableStack<K,V>();
            s.tab = t;
            s.length = n;
            s.index = i;
            s.next = stack;
            stack = s;
        }

        //恢复存储时的状态
        private void recoverState(int n) {
            ConcurrentHashMap1.TableStack<K,V> s; int len;
            while ((s = stack) != null && (index += (len = s.length)) >= n) {
                n = len;
                index = s.index;
                tab = s.tab;
                s.tab = null;
                ConcurrentHashMap1.TableStack<K,V> next = s.next;
                s.next = spare; // save for reuse
                stack = next;
                spare = s;
            }
            if (s == null && (index += baseSize) >= n)
                index = ++baseIndex;
        }
    }

    // Unsafe mechanics
    private static final sun.misc.Unsafe U;
    private static final long SIZECTL;
    private static final long TRANSFERINDEX;
    private static final long BASECOUNT;
    private static final long CELLSBUSY;
    private static final long CELLVALUE;
    private static final long ABASE;
    private static final int ASHIFT;

    static {
        try {
            U = sun.misc.Unsafe.getUnsafe();
            Class<?> k = ConcurrentHashMap.class;
            SIZECTL = U.objectFieldOffset
                    (k.getDeclaredField("sizeCtl"));
            TRANSFERINDEX = U.objectFieldOffset
                    (k.getDeclaredField("transferIndex"));
            BASECOUNT = U.objectFieldOffset
                    (k.getDeclaredField("baseCount"));
            CELLSBUSY = U.objectFieldOffset
                    (k.getDeclaredField("cellsBusy"));
            Class<?> ck = ConcurrentHashMap1.CounterCell.class;
            CELLVALUE = U.objectFieldOffset
                    (ck.getDeclaredField("value"));
            Class<?> ak = ConcurrentHashMap1.Node[].class;
            ABASE = U.arrayBaseOffset(ak);
            int scale = U.arrayIndexScale(ak);
            if ((scale & (scale - 1)) != 0)
                throw new Error("data type scale not a power of two");
            ASHIFT = 31 - Integer.numberOfLeadingZeros(scale);
        } catch (Exception e) {
            throw new Error(e);
        }
    }
}