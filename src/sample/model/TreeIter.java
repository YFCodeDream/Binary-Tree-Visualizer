package sample.model;

import java.util.LinkedList;

/**
 * 二叉树遍历需要遵循的接口
 * @author YFCodeDream
 * @version 1.0
 * @date 2020/10/25 13:11
 */
public interface TreeIter<T> {
    /**
     * 获取先序遍历的列表
     * @param node 传入的节点
     * @param list 传入的存储链表
     */
    void getPreOrderList(TreeNode<T> node, LinkedList<T> list);

    /**
     * 获取中序遍历的列表
     * @param node 传入的节点
     * @param list 传入的存储链表
     */
    void getInOrderList(TreeNode<T> node, LinkedList<T> list);

    /**
     * 获取后序遍历的列表
     * @param node 传入的节点
     * @param list 传入的存储链表
     */
    void getPostOrderList(TreeNode<T> node, LinkedList<T> list);

    /**
     * 获取层序遍历的列表
     * @param root 传入的节点
     * @param list 传入的存储链表
     */
    void levelTraverseList(TreeNode<T> root, LinkedList<T> list);
}
