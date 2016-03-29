package com.struct.binarytree;

public class TreeTest {

	public static void main(String[] args) {

		BinaryTree<Integer> biTree = new BinaryTree<Integer>();

		Integer[] data = { 6, 8, 7, 4 };

		biTree.buildTree(data);

		biTree.printTree();
		
		System.out.println(biTree.getRoot().getData());
	}
}
