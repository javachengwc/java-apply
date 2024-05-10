package com.arithmetic.find;

/**
 * 线性查找
 */
public class LineSearch {

	public static int[] data = { 12, 76, 29, 22, 15, 62, 29, 58, 35, 67, 58,
			33, 28, 89, 90, 28, 64, 48, 20, 77 }; // 输入数据数组

	public static int counter = 0; // 查找次数计数变量

	public static void main(String args[]) {

		int KeyValue = 22;
		// 调用线性查找
		if (lineSearch(KeyValue)) {
			// 输出查找次数
			System.out.println("");
			System.out.println("Search Time = " + counter);
		} else {
			// 输出没有找到数据
			System.out.println("");
			System.out.println("No Found!!");
		}
	}

	public static boolean lineSearch(int Key) {
		for (int i = 0; i < data.length; i++) {
			
			System.out.print("[" + data[i] + "]");
			counter++;
			if (Key == data[i])
			{
				// 查找到数据时 传回true
				return true;
			}
		}
		return false; // 传回false
	}
}
