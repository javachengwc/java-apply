package com.arithmetic.find;

/**
 * 折半查找
 */
public class ZheBanSearch {

	public static int[] data = { 12, 16, 19, 22, 25, 32, 39, 48, 55, 57, 58,
			63, 68, 69, 70, 78, 84, 88, 90, 97 }; // 数据数组
	public static int counter = 1; // 计数器

	public static void main(String args[]) {
		int KeyValue = 22;
		// 调用折半查找
		if (BinarySearch((int) KeyValue)) {
			// 输出查找次数
			System.out.println("");
			System.out.println("Search Time = " + counter);
		} else {
			// 输出没有找到数据
			System.out.println("");
			System.out.println("No Found!!");
		}
	}

	// ---------------------------------------------------
	// 折半查找法
	// ---------------------------------------------------
	public static boolean BinarySearch(int KeyValue) {
		int left = 0; // 左边界变量
		int right = data.length - 1; // 右边界变量
		int middle; // 中位数变量

		while (left <= right) {
			middle = (left + right) / 2;
			if (KeyValue < data[middle])
			{   // 欲查找值较小 查找前半段
				right = middle - 1;
			}
			else if (KeyValue > data[middle])
			{    // 欲查找值较大 查找后半段
				left = middle + 1;
			}
			else if (KeyValue == data[middle]) 
			{    // 查找到数据
				System.out.println("data[" + middle + "] = " + data[middle]);
				return true;
			}
			counter++;
		}
		return false;
	}

}
