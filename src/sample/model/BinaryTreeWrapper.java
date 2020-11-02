package sample.model;

import javafx.beans.property.*;
import sample.util.GenerateRandomListUtil;
import sample.util.TreeBuilderUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 二叉树的包装类，便于JavaFXApplication的view使用
 * 底层的二叉树结构以成员变量形式存储
 * @author YFCodeDream
 * @version 1.0
 * @date 2020/10/27 12:55
 */
public class BinaryTreeWrapper {
    private IntegerProperty fromIndex;
    private IntegerProperty endIndex;
    private StringProperty valueList;
    private StringProperty choice;
    private BinaryTree<Integer> binaryTree;

    public BinaryTreeWrapper(String choice) {
        this(0, 1, null, choice);
    }

    public BinaryTree<Integer> getBinaryTree() {
        return binaryTree;
    }

    public void setBinaryTree(BinaryTree<Integer> binaryTree) {
        this.binaryTree = binaryTree;
    }

    public BinaryTreeWrapper(int fromIndex,
                             int endIndex,
                             String valueList,
                             String choice) {
        this.fromIndex = new SimpleIntegerProperty(fromIndex);
        this.endIndex = new SimpleIntegerProperty(endIndex);
        this.valueList = new SimpleStringProperty(valueList);
        this.choice = new SimpleStringProperty(choice);
        this.binaryTree = TreeBuilderUtil.buildSampleTree();

        // 依据不同的创建方式创建二叉树
        if (choice.equals("Sample Binary Tree") || choice.equals("Binary Tree By Recursion")) {
            this.binaryTree = TreeBuilderUtil.buildSampleTree();

            LinkedList<Integer> userValueList = new LinkedList<>();
            this.binaryTree.levelTraverseList(this.binaryTree.getRoot(), userValueList);
            this.valueList = new SimpleStringProperty(listToValueList(userValueList));

        } else if (choice.equals("Binary Search Tree")) {
            this.binaryTree = new BinaryTree<>();
            TreeBuilderUtil<Integer> treeBuilderUtil = new TreeBuilderUtil<>();
            ArrayList<Integer> defaultValueList = GenerateRandomListUtil.generateRandomIntList(0, 3);
            this.valueList = new SimpleStringProperty(listToValueList(defaultValueList));
            treeBuilderUtil.buildBinarySearchTree(this.binaryTree, defaultValueList);
        }
    }

    /**
     * 将参数列表转换成字符串存储，方便在视图上展示
     * @param list 待转换的列表
     * @return 转换完毕的字符串
     */
    private String listToValueList(List<Integer> list) {
        String listString = list.toString();
        return listString.substring(1, listString.length() - 1);
    }

    // getter和setter方法

    @SuppressWarnings("unused")
    public int getFromIndex() {
        return fromIndex.get();
    }

    @SuppressWarnings("unused")
    public IntegerProperty fromIndexProperty() {
        return fromIndex;
    }

    @SuppressWarnings("unused")
    public void setFromIndex(int fromIndex) {
        this.fromIndex.set(fromIndex);
    }

    @SuppressWarnings("unused")
    public int getEndIndex() {
        return endIndex.get();
    }

    @SuppressWarnings("unused")
    public IntegerProperty endIndexProperty() {
        return endIndex;
    }

    @SuppressWarnings("unused")
    public void setEndIndex(int endIndex) {
        this.endIndex.set(endIndex);
    }

    public String getValueList() {
        return valueList.get();
    }

    @SuppressWarnings("unused")
    public StringProperty valueListProperty() {
        return valueList;
    }

    public void setValueList(String valueList) {
        this.valueList.set(valueList);
    }

    public String getChoice() {
        return choice.get();
    }

    public StringProperty choiceProperty() {
        return choice;
    }

    @SuppressWarnings("unused")
    public void setChoice(String choice) {
        this.choice.set(choice);
    }
}
