package com.arithmetic.array;

import java.util.ArrayList;
import java.util.List;

/**
 * 一个集合中所有子集
 */
public class SubArray {

    public static void main(String [] args) {
        int [] a = {1,2,3};
        buildSubSet(a).forEach(System.out::println);
    }

    public static List<List<Integer>> buildSubSet(int [] nums) {
        List<List<Integer>> result = new ArrayList<>();
        result.add(new ArrayList<>());
        for(int i=0;i<nums.length;i++) {
            int size = result.size();
            for(int j=0;j<size;j++) {
                List<Integer> clone = new ArrayList<>(result.get(j));
                clone.add(nums[i]);
                result.add(clone);
            }
        }
        result.remove(0);
        return result;
    }
}
