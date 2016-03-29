package com.arithmetic.floyd;

/**
 * Floyd算法又称为，插点法，
 * 是一种用于寻找给定的加权图中多源点之间最短路径的算法
 * @author cwc
 * 以无向图G为入口，得出任意两点之间的路径长度length[i][j]，路径path[i][j][k]，
 * 途中无连接得点距离用0表示，点自身也用0表示
 */
public class Floyd {

	int[][] length = null;// 任意两点之间路径长度
	int[][][] path = null;// 任意两点之间的路径
	
	public Floyd(int[][] G) 
	{
		
		int MAX = 100;int row = G.length;// 图G的行数
		int[][] spot = new int[row][row];// 定义任意两点之间经过的点
		int[] onePath = new int[row];// 记录一条路径
		length = new int[row][row];
		path = new int[row][row][];
		for (int i = 0; i < row; i++)// 处理图两点之间的路径
		for (int j = 0; j < row; j++) {
			if (G[i][j] == 0)G[i][j] = MAX;// 没有路径的两个点之间的路径为默认最大
			if (i == j)G[i][j] = 0;// 本身的路径长度为0
		}
		for (int i = 0; i < row; i++)// 初始化为任意两点之间没有路径
		for (int j = 0; j < row; j++)
		spot[i][j] = -1;
		for (int i = 0; i < row; i++)// 假设任意两点之间的没有路径
		onePath[i] = -1;
		for (int v = 0; v < row; ++v)
		for (int w = 0; w < row; ++w)
		length[v][w] = G[v][w];
		for (int u = 0; u < row; ++u)
		for (int v = 0; v < row; ++v)
		for (int w = 0; w < row; ++w)
		if (length[v][w] > length[v][u] + length[u][w]) {
			length[v][w] = length[v][u] + length[u][w];// 如果存在更短路径则取更短路径
			spot[v][w] = u;// 把经过的点加入
		}
		for (int i = 0; i < row; i++) {// 求出所有的路径
			int[] point = new int[1];
			for (int j = 0; j < row; j++) {
				point[0] = 0;
				onePath[point[0]++] = i;
				outputPath(spot, i, j, onePath, point);
				path[i][j] = new int[point[0]];
				for (int s = 0; s < point[0]; s++)
				path[i][j][s] = onePath[s];
		    }
		}
	}
	
	public void outputPath(int[][] spot, int i, int j, int[] onePath, int[] point) {// 输出i// 到j// 的路径的实际代码，point[]记录一条路径的长度
		if (i == j)return;
		
		if (spot[i][j] == -1){
			onePath[point[0]++] = j;
			//System.out.print(" "+j+" ");
		}else {
			outputPath(spot, i, spot[i][j], onePath, point);
			outputPath(spot, spot[i][j], j, onePath, point);
		}
	}
	
	public static void main(String[] args) {
		int data[][] = {
		{ 0, 27, 44, 17, 11, 27, 42, 0, 0, 0, 20, 25, 21, 21, 18, 27, 0 },// x1
		{ 27, 0, 31, 27, 49, 0, 0, 0, 0, 0, 0, 0, 52, 21, 41, 0, 0 },// 1
		{ 44, 31, 0, 19, 0, 27, 32, 0, 0, 0, 47, 0, 0, 0, 32, 0, 0 },// 2
		{ 17, 27, 19, 0, 14, 0, 0, 0, 0, 0, 30, 0, 0, 0, 31, 0, 0 },// 3
		{ 11, 49, 0, 14, 0, 13, 20, 0, 0, 28, 15, 0, 0, 0, 15, 25, 30 },// 4
		{ 27, 0, 27, 0, 13, 0, 9, 21, 0, 26, 26, 0, 0, 0, 28, 29, 0 },// 5
		{ 42, 0, 32, 0, 20, 9, 0, 13, 0, 32, 0, 0, 0, 0, 0, 33, 0 },// 6
		{ 0, 0, 0, 0, 0, 21, 13, 0, 19, 0, 0, 0, 0, 0, 0, 0, 0 },// 7
		{ 0, 0, 0, 0, 0, 0, 0, 19, 0, 11, 20, 0, 0, 0, 0, 33, 21 },// 8
		{ 0, 0, 0, 0, 28, 26, 32, 0, 11, 0, 10, 20, 0, 0, 29, 14, 13 },// 9
		{ 20, 0, 47, 30, 15, 26, 0, 0, 20, 10, 0, 18, 0, 0, 14, 9, 20 },// 10
		{ 25, 0, 0, 0, 0, 0, 0, 0, 0, 20, 18, 0, 23, 0, 0, 14, 0 },// 11
		{ 21, 52, 0, 0, 0, 0, 0, 0, 0, 0, 0, 23, 0, 27, 22, 0, 0 },// 12
		{ 21, 21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 27, 0, 0, 0, 0 },// 13
		{ 18, 41, 32, 31, 15, 28, 0, 0, 0, 29, 14, 0, 22, 0, 0, 11, 0 },// 14
		{ 27, 0, 0, 0, 25, 29, 33, 0, 33, 14, 9, 14, 0, 0, 11, 0, 9 },// 15
		{ 0, 0, 0, 0, 30, 0, 0, 0, 21, 13, 20, 0, 0, 0, 0, 9, 0 } // 16
		};
		for (int i = 0; i < data.length; i++)
		for (int j = i; j < data.length; j++)
		if (data[i][j] != data[j][i])return;
		Floyd test=new Floyd(data);
		for (int i = 0; i < data.length; i++)
		for (int j = i; j < data[i].length; j++) {
			System.out.println();
			System.out.print("From " + i + " to " + j + " path is: ");
			for (int k = 0; k < test.path[i][j].length; k++)
			System.out.print(test.path[i][j][k] + " ");
			System.out.println();
			System.out.println("From " + i + " to " + j + " length :"+ test.length[i][j]);
		}
	}
}