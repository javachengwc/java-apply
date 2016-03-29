package com.arithmetic.sort;

/**
 * 选择排序
 * 选择排序的基本思想是遍历数组的过程中，以 i 代表当前需要排序的序号，则需要在剩余的 [i…n-1] 中找出其中的最小值，
 * 然后将找到的最小值与 i 指向的值进行交换。因为每一趟确定元素的过程中都会有一个选择最大值的子流程，
 * 所以人们形象地称之为选择排序
 */
public class SelectSort {
	
	public static void main(String[] args) {
		
        int[] a={49,38,65,97,76,13,27,49,78,34,12,64,1,8};
        
        System.out.println("排序之前：");
        
        for (int i = 0; i < a.length; i++) 
        {
            System.out.print(a[i]+" ");
        }
        //简单的选择排序
        for (int i = 0; i < a.length; i++) {
            int min = a[i];
            int n=i; //最小数的索引
            for(int j=i+1;j<a.length;j++){
                if(a[j]<min){  //找出最小的数
                    min = a[j];
                    n = j;
                }
            }
            a[n] = a[i];
            a[i] = min;
            
        }
        
        System.out.println();
        System.out.println("排序之后：");
        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i]+" ");
        }
    }

}
