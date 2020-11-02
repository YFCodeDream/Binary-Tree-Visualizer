package sample.util;

import sample.model.BinaryTree;
import sample.model.TreeNode;

import java.util.ArrayList;
import java.util.List;


/**
 * @author YFCodeDream
 * @version 1.0
 * @date 2020/10/25 15:28
 */
public class TreeBuilderUtil<T extends Comparable<T>> {
    private CompareValueUtil<T> compareValueUtil;

    public static BinaryTree<Integer> buildSampleTree() {
        // 创建测试二叉树对象
        //           0
        //         /   \
        //        1      2
        //      / \     / \
        //     3  4    5    6
        //    /\ / \  / \   / \
        //   7 8 9 10 11 12 13 14
        BinaryTree<Integer> sampleBinaryTree = new BinaryTree<>();

        // 测试总结点数为15
        int testNumOfNodes = 15;

        // 创建根节点，采用Integer作为实现泛型，声明节点的value类型
        TreeNode<Integer> rootNode = new TreeNode<>(0, 0);
        sampleBinaryTree.setRoot(rootNode);

        // 创建存储TreeNode的ArrayList
        ArrayList<TreeNode<Integer>> nodeArrayList = new ArrayList<>();

        // 匿名内部类生成节点实例对象
        for (int i = 1 ; i < testNumOfNodes ; i++) {
            nodeArrayList.add(new TreeNode<>(i, i));
        }

        // 传统方式自定义创建样例二叉树
        // 完全是作者之前写过，然后不想改直接copy过来的
        // 不得不说，这一堆看起来比较憨憨
        rootNode.setLeft(nodeArrayList.get(0));
        rootNode.setRight(nodeArrayList.get(1));
        rootNode.getLeft().setLeft(nodeArrayList.get(2));
        rootNode.getLeft().setRight(nodeArrayList.get(3));
        rootNode.getRight().setLeft(nodeArrayList.get(4));
        rootNode.getRight().setRight(nodeArrayList.get(5));
        rootNode.getLeft().getLeft().setLeft(nodeArrayList.get(6));
        rootNode.getLeft().getLeft().setRight(nodeArrayList.get(7));
        rootNode.getLeft().getRight().setLeft(nodeArrayList.get(8));
        rootNode.getLeft().getRight().setRight(nodeArrayList.get(9));
        rootNode.getRight().getLeft().setLeft(nodeArrayList.get(10));
        rootNode.getRight().getLeft().setRight(nodeArrayList.get(11));
        rootNode.getRight().getRight().setLeft(nodeArrayList.get(12));
        rootNode.getRight().getRight().setRight(nodeArrayList.get(13));

        return sampleBinaryTree;
    }

    /**
     * 开放给外部类的生成一般二叉树的API
     * 依据参数列表，层序递归生成一般二叉树
     * @param binaryTree 传入的二叉树
     * @param valueList 递归建立一般二叉树的参数列表
     */
    public void buildBinaryTreeByRecursion(BinaryTree<T> binaryTree, ArrayList<T> valueList) {
        binaryTree.setRoot(buildBinaryTreeRoot(valueList, 0));
    }

    /**
     * 递归建立二叉树
     * @param valueList 递归建立一般二叉树的参数列表
     * @param index 当前节点序号
     * @return 建立完毕的二叉树的根节点
     */
    private TreeNode<T> buildBinaryTreeRoot(ArrayList<T> valueList, int index) {
        TreeNode<T> root = new TreeNode<>(index, valueList.get(index));

        if (index * 2 + 1 < valueList.size()) {
            root.setLeft(buildBinaryTreeRoot(valueList, index * 2 + 1));
        }

        if (index * 2 + 2 < valueList.size()) {
            root.setRight(buildBinaryTreeRoot(valueList, index * 2 + 2));
        }

        return root;
    }

    /**
     * 创建二叉搜索树，创建规则：
     *     如果创建节点的值小于当前节点，那么创建的为左子节点
     *     如果创建节点的值大于当前节点，那么创建的为右子节点
     * @param binaryTree 传入的初始空二叉树
     * @param value 传入的节点值
     */
    private void buildBinarySearchTree(BinaryTree<T> binaryTree, int id, T value) {
        // 以value为值，新建TreeNode
        TreeNode<T> node = new TreeNode<>(id, value);

        // 若无根节点，则以该节点为根节点
        if (binaryTree.getRoot() == null) {
            binaryTree.setRoot(node);
        } else {
            // 获取根节点
            TreeNode<T> tempNode = binaryTree.getRoot();
            // 如果根节点非空，进入循环
            while (tempNode != null) {
                // 泛型对象比较过程
                // 如果：创建的节点的value小于tempNode的value，则将其作为左子节点
                if (compareValueUtil.isGreater(value, tempNode.getValue()) == tempNode.getValue()) {
                    // 若tempNode左节点为空，则直接创建，并将新节点的父节点设置为tempNode
                    if (!tempNode.hasLeft()) {
                        tempNode.setLeft(node);
                        node.setParent(tempNode);
                        return;
                    } else {
                        // 若tempNode的左子节点非空，则继续往下寻找左子节点
                        tempNode = tempNode.getLeft();
                    }
                } else {
                    // 如果：创建的节点的value大于tempNode的value，则将其作为右子节点
                    // 若tempNode右节点为空，则直接创建，并将新节点的父节点设置为tempNode
                    if (!tempNode.hasRight()) {
                        tempNode.setRight(node);
                        node.setParent(tempNode);
                        return;
                    } else {
                        // 若tempNode的右子节点非空，则继续往下寻找右子节点
                        tempNode = tempNode.getRight();
                    }
                }
            }
        }
    }

    /**
     * 开放给外部类调用的建立二叉搜索树的API
     * @param binaryTree 传入的二叉树
     * @param list 建立二叉搜索树的参数列表
     */
    public void buildBinarySearchTree(BinaryTree<T> binaryTree, List<T> list) {
        for (int i = 0; i < list.size(); i++) {
            this.buildBinarySearchTree(binaryTree, i, list.get(i));
        }
    }

    /*
    public void buildAVLTree(BinaryTree<T> binaryTree) {
        // TODO: build AVL Tree, a little difficult
    }
    */

    public TreeBuilderUtil() {
        this.compareValueUtil = new CompareValueUtil<>();
    }
}
