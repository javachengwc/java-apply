package com.struct.binarytree;

public class TreeNode<T> {

	protected TreeNode<T> left;

	protected TreeNode<T> right;

	protected T data;

	TreeNode(T newData) {

		left = null;

		right = null;

		data = newData;

	}

	public TreeNode<T> getLeft() {
		return left;
	}

	public void setLeft(TreeNode<T> left) {
		this.left = left;
	}

	public TreeNode<T> getRight() {
		return right;
	}

	public void setRight(TreeNode<T> right) {
		this.right = right;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
	
}
