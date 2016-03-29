package com.arithmetic.find;

/**
 * 折半查找
 * 递归，循环实现
 */
public class ZheBanSearch2 {

	private int rCount = 0;
	private int lCount = 0;
	
	public int getrCount() {
		return rCount;
	}
    
	public int getlCount() {
		return lCount;
	}

	/**
	 * 执行递归二分查找，返回第一次出现该值的位置
	 * @param sortedData 已排序的数组
	 * @param start 开始位置
	 * @param end   结束位置
	 * @param findValue 需要找的值
	 * @return 值在数组中的位置，从0开始。找不到返回-1
	 */
	public int searchRecursive(int[] sortedData, int start, int end,
			int findValue) {
		rCount++;
		if (start <= end) {
			// 中间位置
			int middle = (start + end) >> 1; // 相当于(start+end)/2
			// 中值
			int middleValue = sortedData[middle];

			if (findValue == middleValue) {
				// 等于中值直接返回
				return middle;
			} else if (findValue < middleValue) {
				// 小于中值时在中值前面找
				return searchRecursive(sortedData, start, middle - 1, findValue);
			} else {
				// 大于中值在中值后面找
				return searchRecursive(sortedData, middle + 1, end, findValue);
			}
		} else {
			// 找不到
			return -1;
		}
	}

	/**
	 * 循环二分查找，返回第一次出现该值的位置
	 * @param sortedData 已排序的数组
	 * @param findValue  需要找的值
	 * @return 值在数组中的位置，从0开始。找不到返回-1
	 */
	public int searchLoop(int[] sortedData, int findValue) {
		int start = 0;
		int end = sortedData.length - 1;

		while (start <= end) {
			lCount++;
			// 中间位置
			int middle = (start + end) >> 1; // 相当于(start+end)/2
			// 中值
			int middleValue = sortedData[middle];

			if (findValue == middleValue) {
				// 等于中值直接返回
				return middle;
			} else if (findValue < middleValue) {
				// 小于中值时在中值前面找
				end = middle - 1;
			} else {
				// 大于中值在中值后面找
				start = middle + 1;
			}
		}
		// 找不到
		return -1;
	}
	
	public static void main(String args [])
	{
		ZheBanSearch2 bs=new ZheBanSearch2();  
        
        int[] sortedData={1,2,3,4,5,6,6,7,8,8,9,10};  
        int findValue=9;  
        int length=sortedData.length;  
          
        int pos=bs.searchRecursive(sortedData, 0, length-1, findValue);  
        System.out.println("Recursice:"+findValue+" found in pos "+pos+";count:"+bs.getrCount());  
        int pos2=bs.searchLoop(sortedData, findValue);  
          
        System.out.println("Loop:"+findValue+" found in pos "+pos2+";count:"+bs.getlCount());
	}

}
