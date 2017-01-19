package com.struct.rbtree;

/**
 * 红黑树节点
 */
public class RBTNode<T extends Comparable<T>> {

    public static final boolean RED   = false;
    public static final boolean BLACK = true;

    private boolean color;        //颜色
    private T key;
    private RBTNode<T> left;    //左子节点
    private RBTNode<T> right;    //右子节点
    private RBTNode<T> parent;    //父节点

    public RBTNode(T key, boolean color, RBTNode<T> parent, RBTNode<T> left, RBTNode<T> right) {
        this.key = key;
        this.color = color;
        this.parent = parent;
        this.left = left;
        this.right = right;
    }

    public boolean getColor() {
        return color;
    }

    public void setColor(boolean color) {
        this.color = color;
    }

    public T getKey() {
        return key;
    }

    public void setKey(T key) {
        this.key = key;
    }

    public RBTNode<T> getLeft() {
        return left;
    }

    public void setLeft(RBTNode<T> left) {
        this.left = left;
    }

    public RBTNode<T> getRight() {
        return right;
    }

    public void setRight(RBTNode<T> right) {
        this.right = right;
    }

    public RBTNode<T> getParent() {
        return parent;
    }

    public void setParent(RBTNode<T> parent) {
        this.parent = parent;
    }

    public String toString() {
        return ""+key+(this.color==RED?"(R)":"(B)");
    }
}
