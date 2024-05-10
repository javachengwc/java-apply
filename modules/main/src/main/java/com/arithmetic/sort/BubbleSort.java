package com.arithmetic.sort;

/**
 * 冒泡排序
 * 在要排序的一组数中，对当前还未排好序的范围内的全部数，自上而下对相邻的两个数依次进行比较和调整，
 * 让较大的数往下沉，较小的往上冒。即：每当两相邻的数比较后发现它们的排序与排序要求相反时，就将它们互换
 */
public class BubbleSort {
	
	public static void main(String[] args) {
		
        int[] a={49,38,65,97,76,13,27,49,78,34,12,64,1,8};
        
        System.out.println("排序之前：");
        
        for (int i = 0; i < a.length; i++) 
        {
            System.out.print(a[i]+" ");
        }
        
        //冒泡排序
        for (int i = 0; i < a.length; i++) {
            for(int j = 0; j<a.length-i-1; j++){
                //这里-i主要是每遍历一次都把最大的i个数沉到最底下去了，没有必要再替换了
                if(a[j]>a[j+1]){
                    int temp = a[j];
                    a[j] = a[j+1];
                    a[j+1] = temp;
                }
            }
        }

//        for (int i = 0; i < a.length; i++) {
//            for(int j = a.length-1; j>i; j--){
//                if(a[j]<a[j-1]){
//                    int temp = a[j];
//                    a[j] = a[j-1];
//                    a[j-1] = temp;
//                }
//            }
//        }
        
        System.out.println();
        System.out.println("排序之后：");
        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i]+" ");
        }
    }

    public static void sort(Integer [] data) {
        if(data==null ||data.length<=1) {
            return;
        }
        int len = data.length;
        for(int i=0;i<len;i++) {
            for(int j=0;j<len-i-1;j++) {
                if(data[j]>data[j+1]) {
                    int tmp = data[j+1];
                    data[j+1]=data[j];
                    data[j]=tmp;
                }
            }
        }
    }

}
