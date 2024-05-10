package com.arithmetic.greed;

import java.util.Arrays;
import java.util.Scanner;

/**
 * 任务积分
 *
 */
public class TaskScore {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();  // 任务数
        int time = scanner.nextInt();  // 可用时间

        // 每个任务的处理时间和积分值
        int[] dtime = new int[n];
        int[] v = new int[n];
        for (int i = 0; i < n; i++) {
            dtime[i] = scanner.nextInt();
            v[i] = scanner.nextInt();
        }

        // dp[t] 表示在时间 t 内能获得的最大积分
        int[] dp = new int[time + 1];

        // 动态规划处理
        for (int i = 0; i < n; i++) {
            //从后向前更新，避免一维数组中的值被提前覆盖
            System.out.println("-------------"+i);
            for (int t = time; t > 0;t--) {
                int left = t - dtime[i];
                if (left>=0) {
                    // 在允许的时间内处理任务
                    dp[t] = Math.max(dp[t], dp[left] + v[i]);
                    System.out.println(Arrays.toString(dp));
                }
            }
        }

        System.out.println(Arrays.toString(dp));
        int maxScore = 0;
        for (int t = 1; t <= time; t++) {
            maxScore = Math.max(maxScore, dp[t]);
        }
        System.out.println(maxScore);
    }
}

