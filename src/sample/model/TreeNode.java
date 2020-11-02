package sample.model;

/**
 * 树的节点类
 * @author YFCodeDream
 * @version 1.0
 * @date 2020/10/25 13:13
 */
public class TreeNode<T> {
    /**
     * 该节点的值
     */
    private T value;

    private int id;

    /**
     * 该节点的父节点
     */
    private TreeNode<T> parent;

    /**
     * 该节点的左节点
     */
    private TreeNode<T> left;

    /**
     * 该节点的右节点
     */
    private TreeNode<T> right;

    /**
     * 返回以该节点为根节点的树的高度
     * @param node 传入的节点
     * @return 以该节点为根节点的树的高度
     */
    public int getHeight(TreeNode<T> node) {
        int height = 0;
        if (node != null) {
            height = Math.max(getHeight(node.left), getHeight(node.right)) + 1;
        }
        return height;
    }

    /**
     * 返回以该节点为根节点的树的节点数
     * @param node 传入的节点
     * @return 以该节点为根节点的树的节点数
     */
    public int getNumOfNodes(TreeNode<T> node) {
        int numOfNodes = 1;
        if (node.left != null) {
            numOfNodes += getNumOfNodes(node.left);
        }
        if (node.right != null) {
            numOfNodes += getNumOfNodes(node.right);
        }
        return numOfNodes;
    }

    /**
     * 父节点判空
     * @return true：父节点非空；false：父节点空
     */
    public boolean hasParent() {
        return this.parent != null;
    }

    /**
     * 左节点判空
     * @return true：左节点非空；false：左节点空
     */
    public boolean hasLeft() {
        return this.left != null;
    }

    /**
     * 右节点判空
     * @return true：右节点非空；false：右节点空
     */
    public boolean hasRight() {
        return this.right != null;
    }

    /**
     * 判定当前节点是否为叶节点
     * @return true：为叶节点；false：非叶节点
     */
    public boolean isLeaf() {
        return !this.hasLeft() && !this.hasRight();
    }

    /**
     * 判定当前节点是否为满
     * @return true：当前节点已满；false：当前节点未满
     */
    public boolean isFull() {
        return this.hasLeft() && this.hasRight();
    }

    /**
     * 构造方法（1、仅传入值）
     * @param value 传入的值
     */
    public TreeNode(int id, T value) {
        this(id, value, null, null);
    }

    /**
     * 构造方法（2、传入值，左节点，右节点）
     * @param value 传入的值
     * @param left 左节点
     * @param right 右节点
     */
    public TreeNode(int id, T value, TreeNode<T> left, TreeNode<T> right) {
        this.id = id;
        this.value = value;
        this.left = left;
        this.right = right;
    }

    /*
     * getter/setter方法
     * @return 相应的返回值
     */

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TreeNode<T> getParent() {
        return parent;
    }

    public T getParentValue() {
        return this.parent != null ? this.parent.value : null;
    }

    public void setParent(TreeNode<T> parent) {
        this.parent = parent;
    }

    public void setParentValue(T value) {
        this.parent.value = value;
    }

    public TreeNode<T> getLeft() {
        return left;
    }

    public T getLeftValue() {
        return this.left != null ? this.left.value : null;
    }

    public void setLeft(TreeNode<T> left) {
        this.left = left;
    }

    public void setLeftValue(T value) {
        this.left.value = value;
    }

    public TreeNode<T> getRight() {
        return right;
    }

    public T getRightValue() {
        return this.right != null ? this.right.value : null;
    }

    public void setRight(TreeNode<T> right) {
        this.right = right;
    }

    public void setRightValue(T value) {
        this.right.value = value;
    }
}
