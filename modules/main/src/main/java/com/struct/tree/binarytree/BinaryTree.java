package com.struct.tree.binarytree;

import java.util.*;

//二叉树添加，删除元素都很快，并且在查找方面也有很多的算法优化，
//二叉树既有链表的好处，也有数组的好处，是两者的优化方案，在处理大批量的动态数据方面非常有用
public class BinaryTree<T extends Comparable<T>> {

	private TreeNode<T> root;

	public BinaryTree() {

		root = null;
	}

	public TreeNode<T> getRoot() {
		return root;
	}

	public void setRoot(TreeNode<T> root) {
		this.root = root;
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

	//节点数量
	public int getCount() {
		int cnt =_getCount(root);
		return cnt;
	}

	private int _getCount(TreeNode<T> node) {
		if(node==null) {
			return 0;
		}
		return 1+_getCount(node.left)+_getCount(node.right);
	}

	//叶子节点数量
	public int getLeafCount() {
		int cnt =_getLeafCount(root);
		return cnt;
	}

	private int _getLeafCount(TreeNode<T> node) {
		if(node==null) {
			return 0;
		}
		if(node.right==null && node.left==null) {
			return 1;
		}
		return _getLeafCount(node.left)+_getLeafCount(node.right);
	}

	//树的深度
	public int getDepth() {
		int dpth =_getDepth(root);
		return dpth;
	}

	private int _getDepth(TreeNode<T> node) {
		if(node==null) {
			return 0;
		}
		int left = _getDepth(node.left);
		int right = _getDepth(node.right);
		return left>=right? left+1: right+1;
	}

	//第k层节点个数
	public int kLevelSize(int k) {
		int size = _kLevelSize(root,k);
		return size;
	}

	private int _kLevelSize(TreeNode<T> node,int k) {
		if(k==0 || node==null) {
			return 0;
		}
		if(k==1) {
			return 1;
		}
		int rt= _kLevelSize(node.left,k-1)+_kLevelSize(node.right,k-1);
		return rt;
	}

	//前序遍历
	public void preOrder(TreeNode<T> node)
	{
		if(node==null) {
			return;
		}
		System.out.print(node.data + " ");
		preOrder(node.left);
		preOrder(node.right);
	}

	//中序遍历
	public void midOrder(TreeNode<T> node)
	{
		if(node==null) {
			return;
		}
		midOrder(node.left);
		System.out.print(node.data + " ");
		midOrder(node.right);
	}

	//后序遍历
	public void posOrder(TreeNode<T> node)
	{
		if(node==null) {
			return;
		}
		posOrder(node.left);
		posOrder(node.right);
		System.out.print(node.data + " ");
	}

	//层序遍历
	//广度优先
	public void levelOrder(TreeNode<T> node) {
		if (node == null) {
			return;
		}
		Queue<TreeNode> queue = new LinkedList<TreeNode>();
		queue.add(node);
		while (!queue.isEmpty()) {
			TreeNode<T> cur = queue.poll();
			System.out.print(cur.getData() + "  ");

			if (cur.left != null) {
				queue.offer(cur.left);
			}
			if (cur.right != null) {
				queue.offer(cur.right);
			}
		}
	}

	//二叉树的层次遍历
	//广度优先
	public static <T> List<List<T>> levelEvery(TreeNode<T> node){
		List<List<T>> result = new ArrayList<List<T>>();
		if(node == null){
			return result;
		}
		int level=1;
		LinkedList<TreeNode> queue = new LinkedList<TreeNode>();
		queue.offer(node);
		while(!queue.isEmpty()){
			int size = queue.size();
			List<T> list = new ArrayList<T>();
			for(int i = 0;i < size ;i++){
				TreeNode<T> cur = queue.poll();
				list.add(cur.getData());
				if(cur.left != null){
					queue.offer(cur.left);
				}
				if(cur.right != null){
					queue.offer(cur.right);
				}
			}
			level++;
			result.add(list);
		}
		System.out.println(result);
		return result;
	}

	//深度优先
	public void dfs(TreeNode<T> node) {
		if (node == null) {
			return;
		}
		Stack<TreeNode> stack = new Stack<>();
		stack.push(node);
		while (!stack.isEmpty()) {
			TreeNode cur = stack.pop();
			System.out.print(cur.getData() + " ");
			if (cur.right != null) {
				stack.push(cur.right);
			}
			if (cur.left != null) {
				stack.push(cur.left);
			}
		}
	}

}
