package com.util.base;

import java.util.*;

public class RandomUtil {
	
	private static String randamStr= "abcdefghijklmnopqrstuvwxyz0123456789";

    private static final Random rand = new Random(System.currentTimeMillis());

    public static int nextRandomInt(int min, int max) {
        int value = max - min + 1;
        return rand.nextInt((value <= 0)?1:value) + min;
    }

    /**
     * 计算概率是否命中 规则，在[1~分母]之间随机抽取，要是抽中的数字落在[1~分子]（包括2端）之间则抽中
     * @param numerator  分子
     * @param denominator  分母
     * @return 抽中true，未中fasle;
     */
    public static boolean randomHit(int numerator, int denominator) {
        int target = nextRandomInt(1, denominator);
        if (target <= numerator) {
            return true;
        }
        return false;
    }

    /**
     * 在区间列表中查找目标数字落在哪个区间
     * @param ranges  区间列表，列表中Integer[] 为区间的2个端点值，
     * @param find  待查找的值
     * @return
     */
    public static Integer[] findRange(Collection<Integer[]> ranges, Integer find) {
        for (Iterator<Integer[]> it = ranges.iterator(); it.hasNext();) {
            Integer[] range = it.next();
            if (find >= range[0] && find <= range[1]) {
                return range;
            }
        }
        return null;
    }

	/**
	 * 生成length长度的字符串
	 * @param length
	 * @return
	 */
	public static String getRandomString(int length) { 
	    Random random = new Random();  
	    StringBuffer sb = new StringBuffer();  
	    for (int i = 0; i < length; i++) {  
	        int number = random.nextInt(randamStr.length());  
	        sb.append(randamStr.charAt(number));  
	    }  
	    return sb.toString();  
	}

    /**
     * 获得UUID
     */
    public static String genUUID()
    {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    /**
     * 按机率取得值。适用于只取一种值，各种机率之间是互斥的（得A就不会得B）。
     * @param probs key:得到的值,value:机率的分子 1/100 中的1
     * @param max  机率中的分母  1/100 中的100
     * @return -1:没随机到任何值,其它:随机到的value
     */
    public static int randomGetInIntMap(Map<Integer, Integer> probs,int max){
        int luck=nextRandomInt(1, max);

        int limitLuckPoint=0;
        for (Integer key : probs.keySet()) {
            limitLuckPoint+=probs.get(key);
            if(luck<=limitLuckPoint) return key;
        }
        return -1;
    }

    public static void main(String args[])
    {
        System.out.println(nextRandomInt(1,100));
        System.out.println(nextRandomInt(1,100));
        System.out.println(randomHit(2, 5));
        System.out.println(randomHit(2,5));
        System.out.println(randomHit(2,5));
        ////////////////////////////////////////////////

        Map<Integer, Integer> resultMap = new HashMap<Integer, Integer>();
        Map<Integer, Integer> propMap = new HashMap<Integer, Integer>();
        propMap.put(10000, 10);
        propMap.put(20000, 20);
        propMap.put(10020, 10);
        propMap.put(13330, 60);
        for(int i=0;i<4000;i++){
            Integer result=randomGetInIntMap(propMap, 100);
            Integer val=resultMap.get(result);
            if(val==null) val=0;
            val++;
            resultMap.put(result, val);
        }
        for (Integer key: resultMap.keySet()) {
            System.out.println(key+":"+resultMap.get(key));
        }
    }
}
