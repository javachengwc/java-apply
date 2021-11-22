package com.struct.array;

//数组中的子集
public class SubArray {

    public static void main(String [] args ) {
        int[] arr={1,2,3};
        getSubArray(arr,arr.length);
    }

    //按位对应法
    private static void getSubArray(int[] arr, int length) {
        int mark=0;
        int nEnd=1<<length;//8
        boolean bNullSet=false;
        for(mark=0;mark<nEnd;mark++){
            bNullSet=true;
            for(int i=0;i<length;i++){
                if(((1<<i)&mark)!=0){//该位有元素输出
                    bNullSet=false;
                    System.out.print(arr[i]+",");
                }
            }
            if(bNullSet){//空集合
                System.out.print("[]");
            }
            System.out.println();
        }
    }
}
