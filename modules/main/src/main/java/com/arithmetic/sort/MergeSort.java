package com.arithmetic.sort;

/**
 * 归并排序
 * 采用的是递归来实现，属于“分而治之”，将目标数组从中间一分为二，之后分别对这两个数组进行排序，
 * 排序完毕之后再将排好序的两个数组“归并”到一起，归并排序最重要的也就是这个“归并”的过程，
 * 归并的过程中需要额外的跟需要归并的两个数组长度一致的空间
 */
public class MergeSort {
	public static void main(String[] args) {
		int[] a = { 49, 38, 65, 97, 76, 13, 27, 49, 78, 34, 12, 64, 1, 8 };
		System.out.println("排序之前：");
		for (int i = 0; i < a.length; i++) {
			System.out.print(a[i] + " ");
		}
		// 归并排序
		mergeSort(a, 0, a.length - 1);
		System.out.println();
		System.out.println("排序之后：");
		for (int i = 0; i < a.length; i++) {
			System.out.print(a[i] + " ");
		}
	}

	private static void mergeSort(int[] a, int left, int right) {
		if (left < right) {
			int middle = (left + right) / 2;
			// 对左边进行递归
			mergeSort(a, left, middle);
			// 对右边进行递归
			mergeSort(a, middle + 1, right);
			// 合并
			merge(a, left, middle, right);
		}
	}

	private static void merge(int[] a, int left, int middle, int right) {
		int[] tmpArray = new int[a.length];
		int tmpIndex = left;

		int aIndex = left;

		int leftStart=left;
		int leftEnd =middle;
		int rightStart = middle + 1; // 右边的起始位置
		int rightEnd = right;
		while (leftStart <= leftEnd && rightStart <= rightEnd) {
			// 从两个数组中选取较小的数放入中间数组
			if (a[leftStart] <= a[rightStart]) {
				tmpArray[tmpIndex++] = a[leftStart++];
			} else {
				tmpArray[tmpIndex++] = a[rightStart++];
			}
		}
		// 将剩余的部分放入中间数组
		while (leftStart <= leftEnd) {
			tmpArray[tmpIndex++] = a[leftStart++];
		}
		while (rightStart <= rightEnd) {
			tmpArray[tmpIndex++] = a[rightStart++];
		}
		// 将中间数组复制回原数组
		while (aIndex <= right) {
			a[aIndex] = tmpArray[aIndex];
			aIndex++;
		}
	}
}
