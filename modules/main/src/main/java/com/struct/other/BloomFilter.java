package com.struct.other;

import java.io.*;
import java.util.BitSet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 布隆过滤器
 * bloom算法类似一个hash set，用来判断某个元素（key）是否在某个集合中。和一般的hash set不同的是，这个算法无需存储key的值，对于每个key，只需要k个比特位，每个存储一个标志，用来判断key是否在集合中。
 * 算法：
 * 1. 首先需要k个hash函数，每个函数可以把key散列成为1个整数
 * 2. 初始化时，需要一个长度为n比特的数组，每个比特位初始化为0
 * 3. 某个key加入集合时，用k个hash函数计算出k个散列值，并把数组中对应的比特位置为1
 * 4. 判断某个key是否在集合时，用k个hash函数计算出k个散列值，并查询数组中对应的比特位，如果所有的比特位都是1，认为在集合中。
 * 优点：不需要存储key，节省空间；
 * 缺点：1. 算法判断key在集合中时，有一定的概率key其实不在集合中 2. 无法删除。
 * 假设缓存系统，key为userId，value为user。如果有10亿数量，规定失误率不能超过0.01%，
 *
 * 布隆过滤器可以使用google已实现的jar，com.google.guava
 */
public class BloomFilter implements Serializable {

    //位图的size
    private final int size;

    //位图
    private final BitSet notebook;

    //误判精度
    private final MisjudgmentRate rate;

    private final int[] seeds;

    //使用数量
    private final AtomicInteger useCount = new AtomicInteger();

    //自动清除的比例，null表示不自动清除
    private final Double autoClearRate;

    //count表示数据量
    public BloomFilter(int count){
        this(MisjudgmentRate.MIDDLE, count, null);
    }

    public BloomFilter(MisjudgmentRate rate, int count, Double autoClearRate){
        //每个字符串需要的bit位数*总数据量
        long bitSize = rate.seeds.length * count;
        if(bitSize<0 || bitSize>Integer.MAX_VALUE){
            throw new RuntimeException("位数太大溢出了，请降低误判率或者降低数据大小");
        }
        this.rate = rate;
        this.seeds = rate.seeds;
        this.size = (int)bitSize;
        this.notebook = new BitSet(size);
        this.autoClearRate = autoClearRate;
    }

    public boolean addIfNotExist(String data){
        //是否需要清理
        checkNeedClear();
        //seeds.length决定每一个string对应多少个bit位，每一位都有一个索引值
        //给定data，求出data字符串的第一个索引值index，如果第一个index值对应的bit=false说明，该data值不存在，则直接将所有对应bit位置为true即可;
        //如果第一个index值对应bit=true，则将index值保存，但此时并不能说明data已经存在，
        //则继续求解下一个index值，若所有index值不存在则说明该data值不存在，将之前保存的index数组对应的bit位置为true
        int[] indexs = new int[seeds.length];
        //假定data已经存在
        boolean exist = true;
        int index;
        for(int i=0; i<seeds.length; i++){
            //计算位hash值
            indexs[i] = index = hash(data, seeds[i]);
            if(exist){
                //如果某一位bit不存在，则说明该data不存在
                if(!notebook.get(index)){
                    exist = false;
                    //将之前的bit位置为true
                    for(int j=0; j<=i; j++){
                        setTrue(indexs[j]);
                    }
                }
            }else{
                //如果不存在则直接置为true
                setTrue(index);
            }
        }
        return true;
    }

    //是否包含某数据
    public boolean isContain(String data){
        if(data==null) {
            return false;
        }
        for(int i=0; i<seeds.length; i++){
            int index = hash(data, seeds[i]);
            if(!notebook.get(index)){
                return false;
            }
        }
        return true;
    }

    private int hash(String data, int seeds) {
        char[] value = data.toCharArray();
        int hash = 0;
        if(value.length>0){
            for(int i=0; i<value.length; i++){
                hash = i * hash + value[i];
            }
        }
        hash = hash * seeds % size;
        return Math.abs(hash);
    }

    private void setTrue(int index) {
        useCount.incrementAndGet();
        notebook.set(index, true);
    }

    //如果BitSet使用比率超过阈值，则将BitSet清零
    private void checkNeedClear() {
        if(autoClearRate != null){
            if(getUseRate() >= autoClearRate){
                synchronized (this) {
                    if(getUseRate() >= autoClearRate){
                        notebook.clear();
                        useCount.set(0);
                    }
                }
            }
        }
    }

    private Double getUseRate() {
        return (double)useCount.intValue()/(double)size;
    }

    //清空过滤器中的记录信息
    public void clear() {
        useCount.set(0);
        notebook.clear();
    }

    public MisjudgmentRate getRate() {
        return rate;
    }

    /**
     * 分配的位数越多，误判率越低但是越占内存
     * 4个位误判率大概是0.14689159766308
     * 8个位误判率大概是0.02157714146322
     * 16个位误判率大概是0.00046557303372
     * 32个位误判率大概是0.00000021167340
     */
    public enum MisjudgmentRate {

        //选取质数，能很好的降低错误率
        //每个字符串分配4个位
        VERY_SMALL(new int[] { 2, 3, 5, 7 }),

        //每个字符串分配8个位
        SMALL(new int[] { 2, 3, 5, 7, 11, 13, 17, 19 }),

        //每个字符串分配16个位
        MIDDLE(new int[] { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53 }),

        //每个字符串分配32个位
        HIGH(new int[] { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131 });

        private int[] seeds;

        private MisjudgmentRate(int[] seeds) {
            this.seeds = seeds;
        }

        public int[] getSeeds() {
            return seeds;
        }

        public void setSeeds(int[] seeds) {
            this.seeds = seeds;
        }
    }

    public static void main(String[] args) {
        BloomFilter fileter = new BloomFilter(7);
        System.out.println(fileter.addIfNotExist("1"));
        System.out.println(fileter.addIfNotExist("2"));
        System.out.println(fileter.addIfNotExist("3"));
        System.out.println(fileter.addIfNotExist("4"));
        System.out.println(fileter.addIfNotExist("5"));
        System.out.println(fileter.addIfNotExist("66"));
        System.out.println(fileter.addIfNotExist("1"));
        System.out.println(fileter.addIfNotExist("77"));
        System.out.println(fileter.addIfNotExist("88"));
        System.out.println(fileter.getUseRate());
        System.out.println(fileter.addIfNotExist("1"));
        System.out.println("------------------------");
        System.out.println(fileter.isContain("1"));
        System.out.println(fileter.isContain("66"));
        System.out.println(fileter.isContain("7"));
    }

}