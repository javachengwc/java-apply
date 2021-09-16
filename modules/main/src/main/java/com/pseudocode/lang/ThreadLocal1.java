package com.pseudocode.lang;

import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class ThreadLocal1 <T> {

    private final int threadLocalHashCode = nextHashCode();

    private static AtomicInteger nextHashCode = new AtomicInteger();

    //魔数值，可以让生成出来的值较为均匀地分布在2的幂大小的数组中，
    //魔数值的选取与斐波那契散列有关，0x61c88647对应的十进制为1640531527
    private static final int HASH_INCREMENT = 0x61c88647;

    private static int nextHashCode() {
        return nextHashCode.getAndAdd(HASH_INCREMENT);
    }

    //初始值
    protected T initialValue() {
        return null;
    }

    public static <S> ThreadLocal1<S> withInitial(Supplier<? extends S> supplier) {
        return new ThreadLocal1.SuppliedThreadLocal<S>(supplier);
    }

    public ThreadLocal1() {
    }

    //获取本地线程值,key是 threadlocal对象，值是当前线程内部对象threadlocalmap中key为threadlocal对象的值。
    public T get() {
        Thread1 t = Thread1.currentThread();
        ThreadLocal1.ThreadLocalMap map = getMap(t);
        if (map != null) {
            ThreadLocal1.ThreadLocalMap.Entry e = map.getEntry(this);
            if (e != null) {
                @SuppressWarnings("unchecked")
                T result = (T)e.value;
                return result;
            }
        }
        return setInitialValue();
    }

    private T setInitialValue() {
        T value = initialValue();
        Thread1 t = Thread1.currentThread();
        ThreadLocal1.ThreadLocalMap map = getMap(t);
        if (map != null)
            map.set(this, value);
        else
            createMap(t, value);
        return value;
    }

    //设置值
    public void set(T value) {
        Thread1 t = Thread1.currentThread();
        ThreadLocal1.ThreadLocalMap map = getMap(t);
        if (map != null)
            map.set(this, value);
        else
            createMap(t, value);
    }

    //移除值
    public void remove() {
        ThreadLocal1.ThreadLocalMap m = getMap(Thread1.currentThread());
        if (m != null)
            m.remove(this);
    }

    ThreadLocal1.ThreadLocalMap getMap(Thread1 t) {
        return t.threadLocals;
    }

    void createMap(Thread1 t, T firstValue) {
        t.threadLocals = new ThreadLocal1.ThreadLocalMap(this, firstValue);
    }

    static ThreadLocal1.ThreadLocalMap createInheritedMap(ThreadLocal1.ThreadLocalMap parentMap) {
        return new ThreadLocal1.ThreadLocalMap(parentMap);
    }

    T childValue(T parentValue) {
        throw new UnsupportedOperationException();
    }

    static final class SuppliedThreadLocal<T> extends ThreadLocal1<T> {

        private final Supplier<? extends T> supplier;

        SuppliedThreadLocal(Supplier<? extends T> supplier) {
            this.supplier = Objects.requireNonNull(supplier);
        }

        @Override
        protected T initialValue() {
            return supplier.get();
        }
    }

    //内部map
    static class ThreadLocalMap {

        //key为threadlocal对像，弱引用，如果引用的ThreadLocal的对象被回收了，
        //这里key即使没有手动删除,由于ThreadLocalMap持有ThreadLocal的弱引用，ThreadLocal也会被回收
        static class Entry extends WeakReference<ThreadLocal1<?>> {

            //value值
            Object value;

            Entry(ThreadLocal1<?> k, Object v) {
                super(k);
                value = v;
            }
        }

        //初始容量(数组长度)
        private static final int INITIAL_CAPACITY = 16;

        //entry数组
        private ThreadLocal1.ThreadLocalMap.Entry[] table;

        private int size = 0;

        private int threshold; // Default to 0

        private void setThreshold(int len) {
            threshold = len * 2 / 3;
        }

        //下一个下标值
        private static int nextIndex(int i, int len) {
            return ((i + 1 < len) ? i + 1 : 0);
        }

        private static int prevIndex(int i, int len) {
            return ((i - 1 >= 0) ? i - 1 : len - 1);
        }

        //初始map
        ThreadLocalMap(ThreadLocal1<?> firstKey, Object firstValue) {
            table = new ThreadLocal1.ThreadLocalMap.Entry[INITIAL_CAPACITY];
            int i = firstKey.threadLocalHashCode & (INITIAL_CAPACITY - 1);
            table[i] = new ThreadLocal1.ThreadLocalMap.Entry(firstKey, firstValue);
            size = 1;
            setThreshold(INITIAL_CAPACITY);
        }

        private ThreadLocalMap(ThreadLocal1.ThreadLocalMap parentMap) {
            ThreadLocal1.ThreadLocalMap.Entry[] parentTable = parentMap.table;
            int len = parentTable.length;
            setThreshold(len);
            table = new ThreadLocal1.ThreadLocalMap.Entry[len];

            for (int j = 0; j < len; j++) {
                ThreadLocal1.ThreadLocalMap.Entry e = parentTable[j];
                if (e != null) {
                    @SuppressWarnings("unchecked")
                    ThreadLocal1<Object> key = (ThreadLocal1<Object>) e.get();
                    if (key != null) {
                        Object value = key.childValue(e.value);
                        ThreadLocal1.ThreadLocalMap.Entry c = new ThreadLocal1.ThreadLocalMap.Entry(key, value);
                        int h = key.threadLocalHashCode & (len - 1);
                        while (table[h] != null)
                            h = nextIndex(h, len);
                        table[h] = c;
                        size++;
                    }
                }
            }
        }

        //内部获取值
        private ThreadLocal1.ThreadLocalMap.Entry getEntry(ThreadLocal1<?> key) {
            int i = key.threadLocalHashCode & (table.length - 1);
            ThreadLocal1.ThreadLocalMap.Entry e = table[i];
            if (e != null && e.get() == key)
                return e;
            else
                return getEntryAfterMiss(key, i, e);
        }

        private ThreadLocal1.ThreadLocalMap.Entry getEntryAfterMiss(ThreadLocal1<?> key, int i, ThreadLocal1.ThreadLocalMap.Entry e) {
            ThreadLocal1.ThreadLocalMap.Entry[] tab = table;
            int len = tab.length;

            while (e != null) {
                ThreadLocal1<?> k = e.get();
                if (k == key)
                    return e;
                if (k == null)
                    expungeStaleEntry(i);
                else
                    i = nextIndex(i, len);
                e = tab[i];
            }
            return null;
        }

        //内部设置值
        private void set(ThreadLocal1<?> key, Object value) {

            ThreadLocal1.ThreadLocalMap.Entry[] tab = table;
            int len = tab.length;
            int i = key.threadLocalHashCode & (len-1);

            for (ThreadLocal1.ThreadLocalMap.Entry e = tab[i]; e != null; e = tab[i = nextIndex(i, len)]) {
                ThreadLocal1<?> k = e.get();

                if (k == key) {
                    e.value = value;
                    return;
                }

                if (k == null) {
                    replaceStaleEntry(key, value, i);
                    return;
                }
            }

            tab[i] = new ThreadLocal1.ThreadLocalMap.Entry(key, value);
            int sz = ++size;
            if (!cleanSomeSlots(i, sz) && sz >= threshold)
                rehash();
        }

        //内部移除值
        private void remove(ThreadLocal1<?> key) {
            ThreadLocal1.ThreadLocalMap.Entry[] tab = table;
            int len = tab.length;
            int i = key.threadLocalHashCode & (len-1);
            for (ThreadLocal1.ThreadLocalMap.Entry e = tab[i];
                 e != null;
                 e = tab[i = nextIndex(i, len)]) {
                if (e.get() == key) {
                    e.clear();
                    expungeStaleEntry(i);
                    return;
                }
            }
        }

        private void replaceStaleEntry(ThreadLocal1<?> key, Object value,
                                       int staleSlot) {
            ThreadLocal1.ThreadLocalMap.Entry[] tab = table;
            int len = tab.length;
            ThreadLocal1.ThreadLocalMap.Entry e;
            int slotToExpunge = staleSlot;
            for (int i = prevIndex(staleSlot, len);
                 (e = tab[i]) != null;
                 i = prevIndex(i, len))
                if (e.get() == null)
                    slotToExpunge = i;
            for (int i = nextIndex(staleSlot, len);
                 (e = tab[i]) != null;
                 i = nextIndex(i, len)) {
                ThreadLocal1<?> k = e.get();
                if (k == key) {
                    e.value = value;

                    tab[i] = tab[staleSlot];
                    tab[staleSlot] = e;
                    if (slotToExpunge == staleSlot)
                        slotToExpunge = i;
                    cleanSomeSlots(expungeStaleEntry(slotToExpunge), len);
                    return;
                }

                if (k == null && slotToExpunge == staleSlot)
                    slotToExpunge = i;
            }
            tab[staleSlot].value = null;
            tab[staleSlot] = new ThreadLocal1.ThreadLocalMap.Entry(key, value);
            if (slotToExpunge != staleSlot)
                cleanSomeSlots(expungeStaleEntry(slotToExpunge), len);
        }

        //清除掉threadlocalMap中所有Entry中Key为null的Value，并将整个Entry设置为null，利于下次内存回收
        private int expungeStaleEntry(int staleSlot) {
            ThreadLocal1.ThreadLocalMap.Entry[] tab = table;
            int len = tab.length;

            tab[staleSlot].value = null;
            tab[staleSlot] = null;
            size--;

            ThreadLocal1.ThreadLocalMap.Entry e;
            int i;
            for (i = nextIndex(staleSlot, len);
                 (e = tab[i]) != null;
                 i = nextIndex(i, len)) {
                ThreadLocal1<?> k = e.get();
                if (k == null) {
                    e.value = null;
                    tab[i] = null;
                    size--;
                } else {
                    int h = k.threadLocalHashCode & (len - 1);
                    if (h != i) {
                        tab[i] = null;
                        while (tab[h] != null)
                            h = nextIndex(h, len);
                        tab[h] = e;
                    }
                }
            }
            return i;
        }

        private boolean cleanSomeSlots(int i, int n) {
            boolean removed = false;
            ThreadLocal1.ThreadLocalMap.Entry[] tab = table;
            int len = tab.length;
            do {
                i = nextIndex(i, len);
                ThreadLocal1.ThreadLocalMap.Entry e = tab[i];
                if (e != null && e.get() == null) {
                    n = len;
                    removed = true;
                    i = expungeStaleEntry(i);
                }
            } while ( (n >>>= 1) != 0);
            return removed;
        }

        private void rehash() {
            expungeStaleEntries();
            if (size >= threshold - threshold / 4)
                resize();
        }

        //扩容，长度增加一倍，迁移到新数组
        private void resize() {
            ThreadLocal1.ThreadLocalMap.Entry[] oldTab = table;
            int oldLen = oldTab.length;
            int newLen = oldLen * 2;
            ThreadLocal1.ThreadLocalMap.Entry[] newTab = new ThreadLocal1.ThreadLocalMap.Entry[newLen];
            int count = 0;

            for (int j = 0; j < oldLen; ++j) {
                ThreadLocal1.ThreadLocalMap.Entry e = oldTab[j];
                if (e != null) {
                    ThreadLocal1<?> k = e.get();
                    if (k == null) {
                        e.value = null; // Help the GC
                    } else {
                        int h = k.threadLocalHashCode & (newLen - 1);
                        while (newTab[h] != null)
                            h = nextIndex(h, newLen);
                        newTab[h] = e;
                        count++;
                    }
                }
            }

            setThreshold(newLen);
            size = count;
            table = newTab;
        }

        //清除掉threadlocalMap中所有Entry中Key为null的Value，并将整个Entry设置为null
        private void expungeStaleEntries() {
            ThreadLocal1.ThreadLocalMap.Entry[] tab = table;
            int len = tab.length;
            for (int j = 0; j < len; j++) {
                ThreadLocal1.ThreadLocalMap.Entry e = tab[j];
                if (e != null && e.get() == null)
                    expungeStaleEntry(j);
            }
        }
    }
}
