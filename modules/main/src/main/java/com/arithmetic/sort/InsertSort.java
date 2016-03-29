package com.arithmetic.sort;

/**
 * 插入排序
 * 插入排序的基本思想是在遍历数组的过程中，假设在序号 i 之前的元素即 [0..i-1] 都已经排好序，
 * 本趟需要找到 i 对应的元素 x 的正确位置 k ，并且在寻找这个位置 k 的过程中逐个将比较过的元
 * 素往后移一位，为元素 x “腾位置”，最后将 k 对应的元素值赋为 x ，插入排序也是根据排序的特性来命名的。
 */
public class InsertSort {

	public static void main(String[] args) {
		
		int[] a = { 49, 38, 65, 97, 76, 13, 27, 49, 78, 34, 12, 64, 1 };
		System.out.println("排序之前：");
		for (int i = 0; i < a.length; i++) 
		{
			System.out.print(a[i] + " ");
		}
		
		// 直接插入排序
		for (int i = 1; i < a.length; i++) {
			// 待插入元素
			int temp = a[i];
			int j;
			for (j = i - 1; j >= 0; j--) {
				// 将大于temp的往后移动一位
				if (a[j] > temp) {
					a[j + 1] = a[j];
				} else {
					break;
				}
			}
			a[j + 1] = temp;
		}
		
		System.out.println();
		System.out.println("排序之后：");
		for (int i = 0; i < a.length; i++) 
		{
			System.out.print(a[i] + " ");
		}
	}

}
