package com.arithmetic.dp;

/**
 * 动态规划算法（Dynamic Programming，简称DP）是一种通过将复杂问题分解为相互关联的子问题，并存储子问题的解以避免重复计算的高效算法设计方法.
 * 经典问题‌：斐波那契数列、最短路径（如Dijkstra算法）
 * 字符串处理‌：最长公共子序列（LCS）、编辑距离计算
 * 求数组中不相邻元素的最大和
 */
public class DynamicProg {
    public static int maxNonAdjacentSum(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        if (nums.length == 1) {
            return nums[0];
        }

        int prevMax = nums[0];
        int currMax = Math.max(nums[0], nums[1]);

        for (int i = 2; i < nums.length; i++) {
            int newMax = Math.max(currMax, prevMax + nums[i]);
            System.out.println("i="+i+",prevMax="+prevMax+",currMax="+currMax+",newMax="+newMax);
            prevMax = currMax;
            currMax = newMax;
        }

        return currMax;
    }

    public static void main(String[] args) {
        int[] arr = {2, 1, 9, 1,10,20,1};
        System.out.println("数组中不相邻元素的最大和为: " + maxNonAdjacentSum(arr));
    }
}
