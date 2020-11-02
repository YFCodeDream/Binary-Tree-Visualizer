package sample.model;

import sample.util.CompareValueUtil;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 底层的二叉树存储结构
 * @author YFCodeDream
 * @version 1.0
 * @date 2020/10/25 13:32
 */
public class BinaryTree<T extends Comparable<T>> implements Tree<T>, TreeIter<T>{
    /**
     * 根节点的引用
     */
    private TreeNode<T> root;

    public void setRoot(TreeNode<T> node) {
        this.root = node;
    }

    public TreeNode<T> getRoot() {
        return this.root;
    }

    @Override
    public T getRootValue() {
        return this.root != null ? this.root.getValue() : null;
    }

    @Override
    public T getLeftValue() {
        return this.root.getLeft() != null ? this.root.getLeftValue() : null;
    }

    @Override
    public T getRightValue() {
        return this.root.getRight() != null ? this.root.getRightValue() : null;
    }

    /**
     * 获取树的高度
     * @return 树的高度
     */
    @Override
    public int getHeight() {
        return this.root.getHeight(this.root);
    }

    /**
     * 获取树的节点总数
     * @return 树的节点总数
     */
    @Override
    public int getNumOfNodes() {
        return this.root.getNumOfNodes(this.root);
    }

    @Override
    public void clear() {
        this.root = null;
    }

    @Override
    public boolean isEmpty() {
        return this.root == null;
    }

    /**
     * 先序遍历
     * @param node 传入的节点
     * @param list 传入的存储链表
     */
    @Override
    public void getPreOrderList(TreeNode<T> node, LinkedList<T> list) {
        if (node != null) {
            list.add(node.getValue());
            getPreOrderList(node.getLeft(), list);
            getPreOrderList(node.getRight(), list);
        }
    }

    /**
     * 中序遍历
     * @param node 传入的节点
     * @param list 传入的存储链表
     */
    @Override
    public void getInOrderList(TreeNode<T> node, LinkedList<T> list) {
        if (node != null) {
            getInOrderList(node.getLeft(), list);
            list.add(node.getValue());
            getInOrderList(node.getRight(), list);
        }
    }

    /**
     * 后序遍历
     * @param node 传入的节点
     * @param list 传入的存储链表
     */
    @Override
    public void getPostOrderList(TreeNode<T> node, LinkedList<T> list) {
        if (node != null) {
            getPostOrderList(node.getLeft(), list);
            getPostOrderList(node.getRight(), list);
            list.add(node.getValue());
        }
    }

    /**
     * 层序遍历，非递归算法
     * @param root 传入的根节点
     * @param list 传入的存储列表
     */
    @Override
    public void levelTraverseList(TreeNode<T> root, LinkedList<T> list){
        if(root == null)
            return;

        //层序遍历时保存结点的队列
        Queue<TreeNode<T>> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            TreeNode<T> node = queue.poll();
            list.add(node.getValue());
            if(node.hasLeft())
                queue.offer(node.getLeft());
            if(node.hasRight())
                queue.offer(node.getRight());
        }
    }

    // 以下算法均需在二叉搜索树中实现
    // 为保证这一点，作者在其余情况将searchValueField禁用编辑

    /**
     * Binary Search Tree的查找，比较节点值，小于当前节点则往左子树查找，否则往右子树查找
     * @param value 待查找的值
     * @return 查找节点
     */
    public TreeNode<T> searchNode(T value) {
        TreeNode<T> tempNode = this.root;
        CompareValueUtil<T> compareValueUtil = new CompareValueUtil<>();
        while (tempNode != null && tempNode.getValue() != value) {
            if (compareValueUtil.isGreater(value, tempNode.getValue()).equals(tempNode.getValue())) {
                tempNode = tempNode.getLeft();
            } else {
                tempNode = tempNode.getRight();
            }
        }
        return tempNode;
    }

    /**
     * Binary Search Tree的插入节点
     * @param value 待插入的值
     */
    public void insertNode(T value) {
        // 若根节点为空，直接将此值作为根节点即可
        if (this.root == null) {
            this.root = new TreeNode<>(0, value);
            return;
        }

        // currentNode进行遍历操作，parentNode记录当前遍历节点，便于后续的插入操作
        TreeNode<T> currentNode = this.root;
        TreeNode<T> parentNode = this.root;

        // 左插标志位，true表示往左子树遍历，false表示往右子树遍历
        boolean tempLeftFlag = true;
        CompareValueUtil<T> compareValueUtil = new CompareValueUtil<>();

        // 循环遍历过程
        while (currentNode != null) {
            // 记录当前节点
            parentNode = currentNode;
            // 比较当前值与当前节点的值，小于则往左子树遍历，否则往右子树遍历
            if (compareValueUtil.isGreater(value, currentNode.getValue()).equals(currentNode.getValue())) {
                currentNode = currentNode.getLeft();
                tempLeftFlag = true;
            } else {
                currentNode = currentNode.getRight();
                tempLeftFlag = false;
            }
        }

        // 新建待插入节点
        TreeNode<T> nextNode = new TreeNode<>(this.getNumOfNodes(), value);

        // 依据左插标志位进行插入操作，并设置父节点
        if (tempLeftFlag) {
            parentNode.setLeft(nextNode);
        } else {
            parentNode.setRight(nextNode);
        }
        nextNode.setParent(parentNode);
    }

    /**
     * 删除节点
     * @param value 待删除的值
     * @return 已删除的节点，可暂存至变量，方便用户撤销操作，类似于回收站功能（当然撤销还没写，hhh）
     */
    public TreeNode<T> deleteNode(T value) {
        // 对待删除节点进行搜索
        TreeNode<T> currentNode = searchNode(value);

        // 若查找失败，直接返回null
        if (currentNode == null) {
            return null;
        }
        // 获取当前节点的父节点
        TreeNode<T> parentNode = currentNode.getParent();
        // 二叉搜索树的删除分为三种情况
        // 没有右子树
        if (!currentNode.hasRight()) {
            if (parentNode != null) {
                if (parentNode.getLeft() == currentNode) {
                    parentNode.setLeft(currentNode.getLeft());
                } else {
                    parentNode.setRight(currentNode.getLeft());
                }
            } else {
                this.setRoot(currentNode.getLeft());
            }
        } else if (!currentNode.hasLeft()) {
            // 没有左子树
            if (parentNode != null) {
                if (parentNode.getLeft() == currentNode) {
                    parentNode.setLeft(currentNode.getRight());
                } else {
                    parentNode.setRight(currentNode.getRight());
                }
            } else {
                this.setRoot(currentNode.getRight());
            }
        } else {
            // 既有左子树，又有右子树
            TreeNode<T> replaceNode = currentNode.getLeft();
            TreeNode<T> replaceParentNode = currentNode;

            // 这里取前驱节点补齐
            while (replaceNode.hasRight()) {
                replaceParentNode = replaceNode;
                replaceNode = replaceNode.getRight();
            }

            if (replaceParentNode == currentNode) {
                replaceParentNode.setLeft(replaceNode.getLeft());
            } else {
                replaceParentNode.setRight(replaceNode.getLeft());
            }

            currentNode.setValue(replaceNode.getValue());
        }

        return currentNode;
    }
}
