package sample.util;

import sample.model.BinaryTree;

import java.util.LinkedList;

/**
 * 该工具类仅在测试时使用
 * developer mode
 * @author YFCodeDream
 * @version 1.0
 * @date 2020/10/25 16:54
 */
public class TestBasicInfoUtil<T extends Comparable<T>> {
    public void testBasicInfo(BinaryTree<T> testBinaryTree) {
        // 测试根节点的基本信息
        System.out.println("The root value of this test binary tree is: " + testBinaryTree.getRootValue());
        System.out.println("The left value is: " + testBinaryTree.getLeftValue());
        System.out.println("The right value is: " + testBinaryTree.getRightValue());

        // 测试树的基本信息
        System.out.println("The height of the test binary tree is: " + testBinaryTree.getHeight());
        System.out.println("The number of nodes of this test binary tree is: " + testBinaryTree.getNumOfNodes());

        // 测试先序，中序，后序遍历
        System.out.print("The PreOrder list is: ");
        LinkedList<T> preOrderList = new LinkedList<>();
        testBinaryTree.getPreOrderList(testBinaryTree.getRoot(), preOrderList);
        System.out.println(preOrderList);
        System.out.print("The InOrder list is: ");
        LinkedList<T> inOrderList = new LinkedList<>();
        testBinaryTree.getInOrderList(testBinaryTree.getRoot(), inOrderList);
        System.out.println(inOrderList);
        System.out.print("The PostOrder list is: ");
        LinkedList<T> postOrderList = new LinkedList<>();
        testBinaryTree.getPostOrderList(testBinaryTree.getRoot(), postOrderList);
        System.out.println(postOrderList);
        System.out.print("The level list is: ");
        LinkedList<T> levelList = new LinkedList<>();
        testBinaryTree.levelTraverseList(testBinaryTree.getRoot(), levelList);
        System.out.println(levelList);

        // 测试对节点的信息判断
        System.out.println("Does root node has its parent? " + (testBinaryTree.getRoot().hasParent() ? "yes" : "no"));
        System.out.println("The parent of the root node is null: " + testBinaryTree.getRoot().getParent());
        System.out.println("Is the root node a leaf node? " + (testBinaryTree.getRoot().isLeaf() ? "yes" : "no"));
        System.out.println("Is the root node full? " + (testBinaryTree.getRoot().isFull() ? "yes" : "no"));
        System.out.println("Does the root node have left node and right node? \n" +
                "left: " + (testBinaryTree.getRoot().hasLeft() ? "yes" : "no") + " | " +
                "right: " + (testBinaryTree.getRoot().hasLeft() ? "yes" : "no"));
    }

    public void testClear(BinaryTree<T> testBinaryTree) {
        // 测试清空树
        testBinaryTree.clear();
        System.out.println("The test binary tree has already been cleared");
        System.out.println("Is the tree empty? " + (testBinaryTree.isEmpty() ? "yes" : "no"));
    }

    public void testSetValue(BinaryTree<T> testBinaryTree, T value) {
        // 测试更改节点的值
        if (testBinaryTree.getRoot() == null) {
            System.out.println("The testBinaryTree is empty");
            return;
        }
        System.out.println("Now set the root value again (for example: 9) ");
        testBinaryTree.getRoot().setValue(value);
        System.out.println("Now the root value is: " + testBinaryTree.getRootValue());
    }
}
