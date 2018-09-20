package com.struct.rbtree;

/**
 * 红黑树,是一种自平衡二叉查找树,它一种特殊的二叉查找树。红黑树的每个节点上都有存储位表示节点的颜色，可以是红(Red)或黑(Black)
 * 红黑树的特性:
 *（1）每个节点或者是黑色，或者是红色。
 *（2）根节点是黑色。
 *（3）每个叶子节点（NIL）是黑色。 这里叶子节点，是指为空(NIL或NULL)的叶子节点。[注意：这里叶子节点，是指为空的叶子节点！]
 *（4）如果一个节点是红色的，则它的子节点必须是黑色的。
 *（5）对于任意结点而言，其到叶结点树尾端NIL指针的每条路径都包含相同数目的黑结点。
 * 这些约束强制了红黑树的关键性质: 从根到叶子的最长的可能路径不多于最短的可能路径的两倍长。结果是这个树大致上是平衡的。
 * 最短的可能路径都是黑色节点，最长的可能路径有交替的红色和黑色节点。
 * 因为根据性质5所有最长的路径都有相同数目的黑色节点，这就表明了没有路径能多于任何其他路径的两倍长。
 * 一棵含有n个节点的红黑树的高度至多为2log(n+1)
 * 注意：
 * 特性(5)，确保没有一条路径会比其他路径长出俩倍。因而，红黑树是相对是接近平衡的二叉树。
 * 红黑树主要是用它来存储有序的数据，它的时间复杂度是O(lgn)，效率非常之高
 * java集合中的TreeSet和TreeMap,Linux虚拟内存的管理都是通过红黑树实现的。
 * 默认插入的结点为红色。为何?
 * 因为红黑树中黑节点至少是红节点的两倍，因此插入节点的父节点为黑色的概率较大，而此时并不需要作任何调整，因此效率较高。
 */
public class RBTree<T extends Comparable<T>> {

    private RBTNode<T> mRoot;    // 根节点

    public RBTree() {
        mRoot=null;
    }

    private RBTNode<T> parentOf(RBTNode<T> node) {
        return node!=null ? node.getParent() : null;
    }

    private boolean colorOf(RBTNode<T> node) {
        return node!=null ? node.getColor() : RBTNode.BLACK;
    }

    private boolean isRed(RBTNode<T> node) {
        return ((node!=null)&&(node.getColor()==RBTNode.RED)) ? true : false;
    }

    private boolean isBlack(RBTNode<T> node) {
        return !isRed(node);
    }

    private void setBlack(RBTNode<T> node) {
        if (node!=null) {
            node.setColor(RBTNode.BLACK);
        }
    }
    private void setRed(RBTNode<T> node) {
        if (node!=null) {
            node.setColor(RBTNode.RED);
        }
    }

    private void setColor(RBTNode<T> node, boolean color) {
        if (node!=null) {
            node.setColor(color);
        }
    }

    private void setParent(RBTNode<T> node, RBTNode<T> parent) {
        if (node!=null) {
            node.setParent(parent);
        }
    }

    //前序遍历
    public void preOrder(RBTNode<T> tree) {
        if(tree != null) {
            System.out.print(tree.getKey() + " ");
            preOrder(tree.getLeft());
            preOrder(tree.getRight());
        }
    }

    public void preOrder() {
        preOrder(mRoot);
    }

    //中序遍历
    public void midOrder(RBTNode<T> tree) {
        if(tree != null) {
            midOrder(tree.getLeft());
            System.out.print(tree.getKey()+" ");
            midOrder(tree.getRight());
        }
    }

    public void midOrder() {
        midOrder(mRoot);
    }

    //后序遍历
    public void postOrder(RBTNode<T> tree) {
        if(tree != null)
        {
            postOrder(tree.getLeft());
            postOrder(tree.getRight());
            System.out.print(tree.getKey()+ " ");
        }
    }

    public void postOrder() {
        postOrder(mRoot);
    }


    //递归查找键值key的节点
    public RBTNode<T> search(RBTNode<T> x, T key) {
        if (x==null) {
            return x;
        }

        int cmp = key.compareTo(x.getKey());
        if (cmp < 0)
            return search(x.getLeft(), key);
        else if (cmp > 0)
            return search(x.getRight(), key);
        else
            return x;
    }

    public RBTNode<T> search(T key) {
        return search(mRoot, key);
    }

    //迭代查找键值key的节点
    public RBTNode<T> iterativeSearch(RBTNode<T> x, T key) {
        while (x!=null) {
            int cmp = key.compareTo(x.getKey());

            if (cmp < 0)
                x = x.getLeft();
            else if (cmp > 0)
                x = x.getRight();
            else
                return x;
        }
        return x;
    }

    public RBTNode<T> iterativeSearch(T key) {
        return iterativeSearch(mRoot, key);
    }

    //查找最小节点
    public RBTNode<T> min(RBTNode<T> tree) {
        if (tree == null) {
            return null;
        }

        while(tree.getLeft() != null) {
            tree = tree.getLeft();
        }
        return tree;
    }

    public T min() {
        RBTNode<T> p = min(mRoot);
        if (p != null) {
            return p.getKey();
        }
        return null;
    }

    //查找最大节点
    public RBTNode<T> max(RBTNode<T> tree) {
        if (tree == null) {
            return null;
        }
        while(tree.getRight() != null) {
            tree = tree.getRight();
        }
        return tree;
    }

    public T max() {
        RBTNode<T> p = max(mRoot);
        if (p != null) {
            return p.getKey();
        }
        return null;
    }

    /*
     * 查找节点x的后继节点。
     * 也就是查找中数据值大于该节点的最小节点。
     */
    public RBTNode<T> findAfter(RBTNode<T> x) {
        // 如果x存在右子节点，则x的后继节点为以其右子节点为根的子树的最小节点。
        if (x.getRight() != null) {
            return min(x.getRight());
        }
        // 如果x没有右子节点。
        // x是一个左子节点，则x的后继节点为它的父节点。
        // x是一个右子节点，则查找x的最低的父节点，并且该父节点的左分支下(或左或右)包含x节点，找到的这个最低的父节点就是x的后继节点。
        RBTNode<T> y = x.getParent();
        while ((y!=null) && (x==y.getRight())) {
            x = y;
            y = y.getParent();
        }
        return y;
    }

    /*
     * 找节点x的前驱节点。
     * 也就是查找树中数据值小于该节点的最大节点。
     */
    public RBTNode<T> findBefore(RBTNode<T> x) {
        // 如果x存在左子节点，则x的前驱节点为以其左子节点为根的子树的最大节点。
        if (x.getLeft() != null) {
            return max(x.getLeft());
        }

        // 如果x没有左子节点。
        // x是一个右子节点，则x的前驱节点为它的父节点。
        // x是"一个左子节点，则查找x的最低的父节点，并且该父节点的右分支下(或左或右)包含x节点，找到的这个最低的父节点就是x的前驱节点。
        RBTNode<T> y = x.getParent();
        while ((y!=null) && (x==y.getLeft())) {
            x = y;
            y = y.getParent();
        }
        return y;
    }

    /*
     * 对红黑树的节点x进行左旋转,对x进行左旋，意味着"将x变成一个左节点"
     * 对x进行左旋，意味着，将“x的右孩子”设为“x的父亲节点”；即，将 x变成了一个左节点(x成了为y的左孩子)
     *      px                              px
     *     /                               /
     *    x                               y
     *   /  \      --(左旋)--           / \
     *  lx   y                          x  ry
     *     /   \                       /  \
     *    ly   ry                     lx  ly
     */
    private void leftRotate(RBTNode<T> x) {
        // 设置x的右子节点为y
        RBTNode<T> y = x.getRight();

        // 将y的左子节点设为x的右子节点,将 “y的左孩子” 设为 “x的右孩子”
        // 如果y的左子节点非空，将x设为y的左子节点的父节点
        x.setRight(y.getLeft());
        if (y.getLeft() != null) {
            y.getLeft().setParent(x); //// 将 “x” 设为 “y的左孩子的父亲”
        }

        // 将x的父节点设为y的父节点
        y.setParent(x.getParent());

        if (x.getParent() == null) {
            // 如果x的父节点是空节点，则将y设为根节点
            // 情况1：如果 “x的父亲” 是空节点，则将y设为根节点
            this.mRoot = y;
        } else {
            if (x.getParent().getLeft() == x) {
                //// 情况2：如果 x是它父节点的左孩子，则将y设为“x的父节点的左孩子”
                // 如果x是它父节点的左子节点，则将y设为x的父节点的左子节点
                x.getParent().setLeft(y) ;
            }
            else {
                //情况3：(x是它父节点的右孩子) 将y设为“x的父节点的右孩子”
                x.getParent().setRight(y);
            }
        }

        // 将x设为y的左子节点
        y.setLeft(x);
        // 将x的父节点设为y
        x.setParent(y);
    }

    /*
     * 对红黑树的节点y进行右旋转，对y进行右旋，意味着"将y变成一个右节点"
     * 对y进行右旋，意味着，将“y的左孩子”设为“y的父亲节点”；即，将 y变成了一个右节点(y成了为x的右孩子)！
     *            py                               py
     *           /                                /
     *          y                                x
     *         /  \      --(右旋)--            /  \
     *        x   ry                           lx   y
     *       / \                                   / \
     *      lx  rx                                rx  ry
     */
    private void rightRotate(RBTNode<T> y) {
        // 设置x是当前节点的左子节点
        RBTNode<T> x = y.getLeft();

        // 将x的右子节点设为y的左子节点, 将 “x的右孩子” 设为 “y的左孩子”
        // 如果x的右子节点不为空，将y设为x的右子节点的父节点,将 “y” 设为 “x的右孩子的父亲”
        y.setLeft(x.getRight());
        if (x.getRight() != null) {
            x.getRight().setParent(y);
        }

        // 将y的父节点设为x的父节点,将 “y的父亲” 设为 “x的父亲”
        x.setParent(y.getParent());

        if (y.getParent() == null) {
            // 情况1：如果 “y的父亲” 是空节点，则将x设为根节点
            // 如果y的父节点是空节点，则将x设为根节点
            this.mRoot = x;
        } else {
            if (y == y.getParent().getRight()) {
                // 情况2：如果 y是它父节点的右孩子，则将x设为“y的父节点的右孩子”
                // 如果y是它父节点的右子节点，则将x设为y的父节点的右子节点
                y.getParent().setRight(x);
            }
            else {
                // 情况3：(y是它父节点的左孩子) 将x设为“y的父节点的左孩子”
                y.getParent().setLeft(x);
            }
        }

        // 将y设为x的右子节点
        x.setRight(y);
        // 将y的父节点设为x
        y.setParent(x);
    }

    /*
     * 红黑树插入修正函数
     * 在向红黑树中插入节点之后(失去平衡)，再调用该函数，将它重新塑造成一颗红黑树。
     */
    public void insertFixUp(RBTNode<T> node) {
        RBTNode<T> parent, gparent;

        //若父节点存在，并且父节点的颜色是红色
        while (((parent = parentOf(node))!=null) && isRed(parent)) {
            gparent = parentOf(parent);

            //若父节点是祖父节点的左子节点
            if (parent == gparent.getLeft()) {
                //叔叔节点是红色
                RBTNode<T> uncle = gparent.getRight();
                if ((uncle!=null) && isRed(uncle)) {
                    setBlack(uncle);
                    setBlack(parent);
                    setRed(gparent);
                    node = gparent;
                    continue;
                }

                //叔叔节点是黑色，且当前节点是右子节点
                if (parent.getRight() == node) {
                    RBTNode<T> tmp;
                    leftRotate(parent);
                    tmp = parent;
                    parent = node;
                    node = tmp;
                }

                // 叔叔节点是黑色，且当前节点是左子节点。
                setBlack(parent);
                setRed(gparent);
                rightRotate(gparent);
            } else {
                //若z的父节点是z的祖父节点的右子节点
                //叔叔节点是红色
                RBTNode<T> uncle = gparent.getLeft();
                if ((uncle!=null) && isRed(uncle)) {
                    setBlack(uncle);
                    setBlack(parent);
                    setRed(gparent);
                    node = gparent;
                    continue;
                }

                //叔叔节点是黑色，且当前节点是左子节点
                if (parent.getLeft() == node) {
                    RBTNode<T> tmp;
                    rightRotate(parent);
                    tmp = parent;
                    parent = node;
                    node = tmp;
                }

                //叔叔节点是黑色，且当前节点是右子节点。
                setBlack(parent);
                setRed(gparent);
                leftRotate(gparent);
            }
        }

        // 将根节点设为黑色
        setBlack(this.mRoot);
    }

    //节点添加
    public void insert(RBTNode<T> node) {
        int cmp;
        RBTNode<T> y = null;
        RBTNode<T> x = this.mRoot;

        //1,当作二叉树，将节点添加到二叉树中。
        while (x != null) {
            y = x;
            cmp = node.getKey().compareTo(x.getKey());
            if (cmp < 0)
                x = x.getLeft();
            else
                x = x.getRight();
        }

        node.setParent(y);
        if (y!=null) {
            cmp = node.getKey().compareTo(y.getKey());
            if (cmp < 0)
                y.setLeft(node);
            else
                y.setRight(node);
        } else {
            this.mRoot = node;
        }

        //2,设置节点的颜色为红色,默认插入的结点为红色
        node.setColor(RBTNode.RED);

        //3,将它重新修正为一颗红黑树
        insertFixUp(node);
    }

    //节点添加
    public void insert(T key) {
        RBTNode<T> node=new RBTNode<T>(key,RBTNode.BLACK,null,null,null);
        if (node != null) {
            insert(node);
        }
    }

    /*
     * 红黑树删除修正函数
     * 在从红黑树中删除节点之后(红黑树失去平衡)，再调用该函数，将它重新塑造成一颗红黑树。
     */
    private void removeFixUp(RBTNode<T> node, RBTNode<T> parent) {
        RBTNode<T> other;

        while ((node==null || isBlack(node)) && (node != this.mRoot)) {
            if (parent.getLeft() == node) {
                other = parent.getRight();
                if (isRed(other)) {
                    //x的兄弟节点w是红色的
                    setBlack(other);
                    setRed(parent);
                    leftRotate(parent);
                    other = parent.getRight();
                }

                if ((other.getLeft()==null || isBlack(other.getLeft())) && (other.getRight()==null || isBlack(other.getRight()))) {
                    //x的兄弟节点w是黑色，且w的俩个子节点也都是黑色的
                    setRed(other);
                    node = parent;
                    parent = parentOf(node);
                } else {

                    if (other.getRight()==null || isBlack(other.getRight())) {
                        // x的兄弟节点w是黑色的，并且w的左子节点是红色，右子节点为黑色。
                        setBlack(other.getLeft());
                        setRed(other);
                        rightRotate(other);
                        other = parent.getRight();
                    }
                    //x的兄弟节点w是黑色的；并且w的右子节点是红色的，左子节点任意颜色。
                    setColor(other, colorOf(parent));
                    setBlack(parent);
                    setBlack(other.getRight());
                    leftRotate(parent);
                    node = this.mRoot;
                    break;
                }
            } else {

                other = parent.getLeft();
                if (isRed(other)) {
                    //x的兄弟节点w是红色的
                    setBlack(other);
                    setRed(parent);
                    rightRotate(parent);
                    other = parent.getLeft();
                }

                if ((other.getLeft()==null || isBlack(other.getLeft())) && (other.getRight()==null || isBlack(other.getRight()))) {
                    //x的兄弟节点w是黑色，且w的两个子节点也都是黑色的
                    setRed(other);
                    node = parent;
                    parent = parentOf(node);
                } else {

                    if (other.getLeft()==null || isBlack(other.getLeft())) {
                        //x的兄弟节点w是黑色的，并且w的左子节点是红色，右子节点为黑色。
                        setBlack(other.getRight());
                        setRed(other);
                        leftRotate(other);
                        other = parent.getLeft();
                    }

                    //x的兄弟节点w是黑色的；并且w的右子节点是红色的，左子节点任意颜色。
                    setColor(other, colorOf(parent));
                    setBlack(parent);
                    setBlack(other.getLeft());
                    rightRotate(parent);
                    node = this.mRoot;
                    break;
                }
            }
        }

        if (node!=null) {
            setBlack(node);
        }
    }

    //删除节点node
    public void remove(RBTNode<T> node) {
        RBTNode<T> child, parent;
        boolean color;

        // 被删除节点的左右子节点都不为空
        if ( (node.getLeft()!=null) && (node.getRight()!=null) ) {
            // 被删节点的后继节点(称为"取代节点")
            // 用它来取代被删节点的位置，然后再将被删节点去掉。
            RBTNode<T> replace = node;

            // 获取后继节点
            replace = replace.getRight();
            while (replace.getLeft() != null) {
                replace = replace.getLeft();
            }

            // node节点不是根节点(只有根节点不存在父节点)
            if (parentOf(node)!=null) {
                if (parentOf(node).getLeft() == node) {
                    parentOf(node).setLeft(replace);
                }
                else {
                    parentOf(node).setRight(replace);
                }
            } else {
                // node节点是根节点，更新根节点。
                this.mRoot = replace;
            }

            // child是取代节点的右子节点，也是需要调整的节点。
            // 取代节点不存在左子节点,因为它是一个后继节点。
            child = replace.getRight();
            parent = parentOf(replace);
            // 保存取代节点的颜色
            color = colorOf(replace);

            // 被删除节点是它的后继节点的父节点
            if (parent == node) {
                parent = replace;
            } else {
                // child不为空
                if (child!=null) {
                    setParent(child, parent);
                }
                parent.setLeft( child);
                replace.setRight(node.getRight());
                setParent(node.getRight(), replace);
            }

            replace.setParent( node.getParent());
            replace.setColor( node.getColor());
            replace.setLeft(node.getLeft());
            node.getLeft().setParent(replace);

            if (color == RBTNode.BLACK) {
                removeFixUp(child, parent);
            }
            node = null;
            return ;
        }

        if (node.getLeft() !=null) {
            child = node.getLeft();
        } else {
            child = node.getRight();
        }

        parent = node.getParent();
        // 设置取代节点的颜色
        color = node.getColor();

        if (child!=null) {
            child.setParent( parent);
        }
        // node节点不是根节点
        if (parent!=null) {
            if (parent.getLeft() == node) {
                parent.setLeft(child);
            }
            else {
                parent.setRight(child);
            }
        } else {
            this.mRoot = child;
        }

        if (color == RBTNode.BLACK) {
            removeFixUp(child, parent);
        }
        node = null;
    }

    //删除节点(node)
    public void remove(T key) {
        RBTNode<T> node=null;
        if ((node = search(mRoot, key)) != null) {
            remove(node);
        }
    }

    //销毁红黑树
    public void destroy(RBTNode<T> tree) {
        if (tree==null) {
            return;
        }
        if (tree.getLeft() != null) {
            destroy(tree.getLeft());
        }
        if (tree.getRight() != null) {
            destroy(tree.getRight());
        }
        tree=null;
    }

    public void clear() {
        destroy(mRoot);
        mRoot = null;
    }

    /*
     * 打印树
     * key        -- 节点的键值
     * direction  --  0，表示该节点是根节点;-1，表示该节点是它的父节点的左子节点; 1，表示该节点是它的父节点的右子节点。
     */
    public void print(RBTNode<T> tree, T key, int direction) {

        if(tree != null) {

            if(direction==0) {   // tree是根节点
                System.out.printf("%2d(B) is root\n", tree.getKey());
            }
            else {               // tree是分支节点
                System.out.printf("%2d(%s) is %2d's %6s child\n", tree.getKey(), isRed(tree) ? "R" : "B", key, direction == 1 ? "right" : "left");
            }
            print(tree.getLeft(), tree.getKey(), -1);
            print(tree.getRight(),tree.getKey(),  1);
        }
    }

    public void print() {
        if (mRoot != null)
            print(mRoot, mRoot.getKey(), 0);
    }
}
