package com.struct.binarytree;

public class BinaryTree<T extends Comparable<T>> {

	private TreeNode<T> root;

	public BinaryTree() {

		root = null;
	}

	public void insert(T data) {

		root = insert(root, data);

	}
	
	private TreeNode<T> insert(TreeNode<T> node,T data) {

		if (node == null) {

			node = new TreeNode<T>(data);
		}
		else {

			if ( data.compareTo(node.getData())<=0 ) {

				node.left = insert(node.left, data);
			}
			else {

				node.right = insert(node.right, data);
			}
		}

		return (node); // in any case, return the new pointer to the caller

	}

	public void buildTree(T[] data) {

		for (int i = 0; i < data.length; i++) {

			insert(data[i]);
		}
	}

	public void printTree() {

		printTree(root);

		System.out.println();

	}

	private void printTree(TreeNode<T> node) {

		if (node == null)
			return;

		printTree(node.left);

		System.out.print(node.data + "  ");

		printTree(node.right);

	}

	public TreeNode<T> getRoot() {
		return root;
	}

	public void setRoot(TreeNode<T> root) {
		this.root = root;
	}
	
}
