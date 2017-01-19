package com.struct.rbtree;

public class RBTreeTest {

    private static final int a[] = {100, 300, 600, 900, 700, 200, 500, 800};

    private static boolean ti = true;
    private static boolean td = false;

    public static void main(String[] args) {
        int i, ilen = a.length;
        RBTree<Integer> tree=new RBTree<Integer>();

        System.out.println("初始数据: ");
        for(i=0; i<ilen; i++) {
            System.out.printf("%d ", a[i]);
        }
        System.out.println("---------------------");

        for(i=0; i<ilen; i++) {
            tree.insert(a[i]);
            if (ti) {
                System.out.printf("添加节点: %d\n", a[i]);
                System.out.printf("树信息: \n");
                tree.print();
                System.out.println();
            }
        }

        System.out.println("前序遍历--------------");
        tree.preOrder();
        System.out.println("----------------------");

        System.out.println("中序遍历--------------");
        tree.midOrder();
        System.out.println("----------------------");

        System.out.println("后序遍历--------------");
        tree.postOrder();
        System.out.println("----------------------");

        System.out.printf("最小值: %s ", tree.min());
        System.out.printf("最大值: %s \n", tree.max());
        System.out.println("----------------------");
        System.out.println("树信息: \n");
        tree.print();
        System.out.println("----------------------");

        if (td) {
            for(i=0; i<ilen; i++)
            {
                tree.remove(a[i]);

                System.out.printf("删除节点: %d", a[i]);
                System.out.printf("树信息: \n");
                tree.print();
                System.out.println("---------------------");
            }
        }
        tree.clear();
    }
}
