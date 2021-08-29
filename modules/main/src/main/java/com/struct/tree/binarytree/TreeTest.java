package com.struct.tree.binarytree;

public class TreeTest {

	public static void main(String[] args) {

		BinaryTree<Integer> biTree = new BinaryTree<Integer>();

		Integer[] data = { 6, 8, 7, 4,3,9,21,33,73,15 };

		biTree.buildTree(data);

		biTree.printTree();
		
		System.out.println(biTree.getRoot().getData());

		int cnt = biTree.getCount();

		int leafCnt = biTree.getLeafCount();

		int depth = biTree.getDepth();

		System.out.println("count-->"+cnt+",leafCount-->"+leafCnt+",depth-->"+depth);

		int kSize = biTree.kLevelSize(2);
		int kSize3 = biTree.kLevelSize(3);
		int kSize4 = biTree.kLevelSize(4);
		System.out.println("前二层节点数量为:"+kSize+",前三层节点数量为:"+kSize3+",前四层节点数为:"+kSize4);
	}
}
