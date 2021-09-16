package com.arithmetic.sort;

/**
 * 堆排序
 * 1,构建初始堆，将待排序列构成一个大顶堆(或者小顶堆)，升序大顶堆，降序小顶堆；
 * 2,将堆顶元素与堆尾元素交换，并断开(从待排序列中移除)堆尾元素。
 * 3,重新构建堆。
 * 4,重复2~3，直到待排序列中只剩下一个元素(堆顶元素)。
 */
public class HeapSort {

	public static void main(String[] args) {
		int[] a = { 49, 38, 65, 97, 76, 13, 27, 49, 78, 34, 12, 64 };
		heapSort(a);
		for (int i : a) {
			System.out.print(i + " ");
		}
	}

	//堆排序
	private static void heapSort(int[] arr) {

		//创建初始堆
		for (int i = (arr.length - 1) / 2; i >= 0; i--) {
			//从一半结点从下至上，从右至左调整结构
			adjustHeap(arr, i, arr.length);
		}

		for (int i = arr.length - 1; i > 0; i--) {
			//将堆顶元素与末尾元素进行交换
			int temp = arr[i];
			arr[i] = arr[0];
			arr[0] = temp;

			//重新对堆进行调整
			adjustHeap(arr, 0, i);
		}
	}

	//调整堆
	private static void adjustHeap(int[] arr, int parent, int length) {
		//将temp作为父节点
		int temp = arr[parent];
		//左孩子
		int lChild = 2 * parent + 1;

		while (lChild < length) {
			//右孩子
			int rChild = lChild + 1;
			// 如果有右孩子结点，并且右孩子结点的值大于左孩子结点，则选取右孩子结点
			if (rChild < length && arr[lChild] < arr[rChild]) {
				lChild++;
			}

			// 如果父结点的值已经大于孩子结点的值，则直接结束
			if (temp >= arr[lChild]) {
				break;
			}

			// 把孩子结点的值赋给父结点
			arr[parent] = arr[lChild];

			//选取孩子结点的左孩子结点,继续向下筛选
			parent = lChild;
			lChild = 2 * parent + 1;
		}
		arr[parent] = temp;
	}
}

