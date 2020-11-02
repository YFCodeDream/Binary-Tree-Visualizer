package sample.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import sample.Main;
import sample.model.BinaryTree;
import sample.model.BinaryTreeWrapper;
import sample.model.TreeNode;
import sample.util.GraphTreeUtil;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

/**
 * @author YFCodeDream
 * @version 1.0
 * @date 2020/10/27 12:39
 */
public class TreeOverviewController {
    // FXML控件成员变量
    @FXML
    private TableView<BinaryTreeWrapper> treeTable;
    @FXML
    private TableColumn<BinaryTreeWrapper, String> treeBuildOptionColumn;
    @FXML
    private Label rootValueLabel;
    @FXML
    private Label heightLabel;
    @FXML
    private Label numOfNodesLabel;
    @FXML
    private Label preOrderListLabel;
    @FXML
    private Label inOrderListLabel;
    @FXML
    private Label postOrderListLabel;
    @FXML
    private Label levelListLabel;
    @FXML
    private Label originalListLabel;
    @FXML
    private TextField searchValueField;
    @FXML
    private Label searchResultLabel;
    @FXML
    private Button editButton;
    @FXML
    private TreeView<String> treeView;
    @FXML
    private ImageView treeFigureView;

    // 对于MainApp的引用，连接controller和view
    private Main mainApp;

    /**
     * 在TreeOverview.fxml被加载后自动调用
     * 初始化左状态栏三个用户选项
     * 清空显示信息
     * 禁用搜索输入框
     * 监听用户在状态栏的选择，并针对不同选择进行相应初始化
     */
    @FXML
    private void initialize() throws IOException {
        // 初始化TreeBuildOption的选项
        treeBuildOptionColumn.setCellValueFactory(cellData -> cellData.getValue().choiceProperty());

        // 清空显示的信息
        showTreeBasicInfo(null);
        searchValueField.setEditable(false);
        searchValueField.setText("");
        searchResultLabel.setText("");

        // 监听用户在TreeBuildOption状态栏的选择
        treeTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        showTreeBasicInfo(newValue);
                        if (newValue != null) {
                            // 仅有在用户选择二叉搜索树功能时，开放搜索框的输入权限
                            // 其余情况禁用搜索框，并清空搜索框
                            // 其余情况禁用TreeView，别问，问就是作者太菜，不会一般树的TreeView构建
                            if (newValue.getChoice().equals("Binary Search Tree")) {
                                searchValueField.setEditable(true);
                            } else {
                                searchValueField.setEditable(false);
                                searchValueField.setText("");
                                searchResultLabel.setText("");
                                treeView.setRoot(null);
                            }
                            // 用户在选择样例树时，禁用编辑
                            if (newValue.getChoice().equals("Sample Binary Tree")) {
                                editButton.setVisible(false);
                            } else {
                                editButton.setVisible(true);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    /**
     * 重置按钮触发逻辑
     * 还原至初始状态
     */
    @FXML
    private void handleResetTree() {
        int selectedIndex = treeTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            // 获取用户当前选择
            String currentChoice = treeTable.getItems().get(selectedIndex).getChoice();
            // 重新创建对应的二叉树包装类的实例
            BinaryTreeWrapper tempTreeWrapper = new BinaryTreeWrapper(currentChoice);
            // 找到对应位置并替换为新创建的初始化实例对象
            for (int i = 0 ; i < mainApp.getTreeChoice().size() ; i++) {
                if (mainApp.getTreeChoice().get(i).getChoice().equals(currentChoice)) {
                    treeTable.getSelectionModel().select(tempTreeWrapper);
                    mainApp.getTreeChoice().set(i, tempTreeWrapper);
                }
            }
            // 清空所有信息
            clearMessage();
        } else {
            // 未选择代码逻辑
            // 显示用户选择提示框
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Binary Tree Selected");
            alert.setContentText("Please select a binary tree in the table.");

            alert.showAndWait();
        }
    }

    /**
     * 编辑按钮触发逻辑
     * 显示用户编辑对话框，在用户按下OK后创建对应实例对象并显示该二叉树信息
     */
    @FXML
    private void handleEditTree() {
        int selectedIndex = treeTable.getSelectionModel().getSelectedIndex();
        String currentChoice;
        if (selectedIndex >= 0) {
            // 获取用户当前选择
            currentChoice = treeTable.getItems().get(selectedIndex).getChoice();
        } else {
            // 未选择代码逻辑
            // 显示用户选择提示框，并结束该方法
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Binary Tree Selected");
            alert.setContentText("Please select a binary tree in the table.");

            alert.showAndWait();
            return;
        }
        // 依据用户选择创建新的初始化实例对象
        BinaryTreeWrapper tempTreeWrapper = new BinaryTreeWrapper(currentChoice);
        // 显示用户编辑对话框，并监听OK按钮，状态标识记录在okClicked标志位
        // 并将创建完毕的二叉树包装对象交由用户编辑
        boolean okClicked = mainApp.showTreeEditDialog(tempTreeWrapper);
        // 监听到用户触发OK按钮
        if (okClicked) {
            // 检索用户选择
            for (int i = 0 ; i < mainApp.getTreeChoice().size() ; i++) {
                if (mainApp.getTreeChoice().get(i).getChoice().equals(currentChoice)) {
                    // 选中当前选项
                    treeTable.getSelectionModel().select(tempTreeWrapper);
                    // 将用户编辑完毕的二叉树包装对象传入对应位置
                    mainApp.getTreeChoice().set(i, tempTreeWrapper);
                }
            }
        }
    }

    /**
     * 显示二叉树图片逻辑
     */
    @FXML
    private void showTreeFigure() {
        // 找到由GraphTreeUtil绘制的二叉树图
        Image treeFigure = new Image("file:testGraph.png");
        // 依据图片大小确定图片显示框的尺寸
        treeFigureView.setFitWidth(treeFigure.getWidth());
        treeFigureView.setFitHeight(treeFigure.getHeight());
        // 显示图片
        treeFigureView.setImage(treeFigure);
    }

    /**
     * 插入节点，仅在用户选择二叉搜索树时生效
     * 其余情况均被NumberFormatException捕获
     * @throws IOException 输入输出异常
     */
    @FXML
    private void handleInsertNode() throws IOException {
        // 获取搜索框传入的数字
        int insertValue = getSearchValue();
        // 若为非法数字，退出当前方法
        if (insertValue == Integer.MIN_VALUE) {
            return;
        }
        // 获取用户选择
        BinaryTreeWrapper selectedTree = treeTable.getSelectionModel().getSelectedItem();
        if (selectedTree != null) {
            // 从用户选择的二叉树包装类中获取底层二叉树存储结构
            BinaryTree<Integer> currentTree = selectedTree.getBinaryTree();
            // 获取当前二叉树的valueList
            LinkedList<Integer> currentValueList = valueListToList(selectedTree.getValueList());
            // 如果待插入的值已经存在于当前valueList中，显示警告弹窗，并结束方法
            assert currentValueList != null;
            if (currentValueList.contains(insertValue)) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.initOwner(mainApp.getPrimaryStage());
                alert.setTitle("Invalid Fields");
                alert.setHeaderText("Please correct invalid fields");
                alert.setContentText("The value has already been in current tree!");

                alert.showAndWait();
                return;
            }
            // 对底层二叉树进行插入节点操作
            currentTree.insertNode(insertValue);
            // 更新相关信息，并提示用户完成相关操作
            getNewValueList(selectedTree, currentTree, "Insert completed.");
        }
    }

    /**
     * 删除节点，仅在用户选择二叉搜索树时生效
     * 其余情况均被NumberFormatException捕获
     * @throws IOException 输入输出异常
     */
    @SuppressWarnings("unused")
    @FXML
    private void handleDeleteNode() throws IOException {
        // 获取搜索框传入的数字
        int deleteValue = getSearchValue();
        // 若为非法数字，退出当前方法
        if (deleteValue == Integer.MIN_VALUE) {
            return;
        }

        // 获取用户选择
        BinaryTreeWrapper selectedTree = treeTable.getSelectionModel().getSelectedItem();
        if (selectedTree != null) {
            // 从用户选择的二叉树包装类中获取底层二叉树存储结构
            BinaryTree<Integer> currentTree = selectedTree.getBinaryTree();
            // 对底层二叉树进行删除节点操作
            TreeNode<Integer> deletedNode = currentTree.deleteNode(deleteValue);
            // 更新相关信息，并提示用户完成相关操作
            getNewValueList(selectedTree, currentTree, "Delete completed.");
        }
    }

    /**
     * 更新节点值列表信息，并弹窗提示用户完成相关操作
     * @param selectedTree 用户选择
     * @param currentTree 对应用户选择的底层二叉树
     * @param message 弹窗的提示信息
     * @throws IOException 输入输出异常
     */
    private void getNewValueList(BinaryTreeWrapper selectedTree,
                                 BinaryTree<Integer> currentTree,
                                 String message) throws IOException {
        // 对当前二叉树重新进行层序遍历
        LinkedList<Integer> currentValueList = new LinkedList<>();
        currentTree.levelTraverseList(currentTree.getRoot(), currentValueList);
        // 更改对应的二叉树包装类对象的节点值列表信息
        selectedTree.setValueList(currentValueList.toString().substring(1, currentValueList.toString().length() - 1));

        // 显示完成弹窗
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.initOwner(mainApp.getPrimaryStage());
        alert.setTitle("Valid Fields");
        alert.setHeaderText(message);
        alert.setContentText("Finish.");

        alert.showAndWait();

        // 显示已操作的二叉树的信息
        showTreeBasicInfo(selectedTree);
    }

    /**
     * 搜索节点，仅在用户选择二叉搜索树时生效
     * 其余情况均被NumberFormatException捕获
     */
    @FXML
    private void handleSearchNode() {
        // 获取搜索框传入的数字
        int searchValue = getSearchValue();
        // 若为非法数字，退出当前方法
        if (searchValue == Integer.MIN_VALUE) {
            return;
        }

        // 获取用户选择
        BinaryTreeWrapper selectedTree = treeTable.getSelectionModel().getSelectedItem();
        if (selectedTree != null) {
            // 从用户选择的二叉树包装类中获取底层二叉树存储结构
            BinaryTree<Integer> currentTree = selectedTree.getBinaryTree();
            // 依据查找结果显示节点信息
            searchResultLabel.setText(currentTree.searchNode(searchValue) != null ?
                    String.valueOf(currentTree.searchNode(searchValue).getId()) :
                    "the value not found");
        }
    }

    /**
     * 显示二叉树的基本信息
     * @param binaryTreeWrapper 传入的二叉树包装类
     * @throws IOException 输入输出异常
     */
    private void showTreeBasicInfo(BinaryTreeWrapper binaryTreeWrapper) throws IOException {
        if (binaryTreeWrapper != null) {
            // 从用户选择的二叉树包装类中获取底层二叉树存储结构
            BinaryTree<Integer> currentTree = binaryTreeWrapper.getBinaryTree();

            // 更新界面信息

            rootValueLabel.setText((currentTree.getRootValue() != null ? currentTree.getRootValue().toString() : ""));
            heightLabel.setText(String.valueOf(currentTree.getHeight()));
            numOfNodesLabel.setText(String.valueOf(currentTree.getNumOfNodes()));

            LinkedList<Integer> preOrderList = new LinkedList<>();
            currentTree.getPreOrderList(currentTree.getRoot(), preOrderList);
            preOrderListLabel.setText(setLineBreak(preOrderList.toString()));

            LinkedList<Integer> inOrderList = new LinkedList<>();
            currentTree.getInOrderList(currentTree.getRoot(), inOrderList);
            inOrderListLabel.setText(setLineBreak(inOrderList.toString()));

            LinkedList<Integer> postOrderList = new LinkedList<>();
            currentTree.getPostOrderList(currentTree.getRoot(), postOrderList);
            postOrderListLabel.setText(setLineBreak(postOrderList.toString()));

            LinkedList<Integer> levelList = new LinkedList<>();
            currentTree.levelTraverseList(currentTree.getRoot(), levelList);
            levelListLabel.setText(setLineBreak(levelList.toString()));

            originalListLabel.setText(setLineBreak(binaryTreeWrapper.getValueList()));

            showTreeView(binaryTreeWrapper);

            GraphTreeUtil<Integer> graphTreeUtil = new GraphTreeUtil<>();
            graphTreeUtil.PaintTree(currentTree.getRoot(), "testGraph");
            showTreeFigure();
        } else {
            clearMessage();
        }
    }

    /**
     * 该功能仅有用户选择Binary Search Tree时生效
     * @param binaryTreeWrapper 传入的二叉树包装类
     */
    private void showTreeView(BinaryTreeWrapper binaryTreeWrapper) {
        // 先对传入的二叉树包装类判空
        if (binaryTreeWrapper != null) {
            // 从传入的二叉树包装类中获取底层二叉树结构
            BinaryTree<Integer> currentTree = binaryTreeWrapper.getBinaryTree();
            // 若树为空，直接结束方法
            if (currentTree.isEmpty()) {
                return;
            }
            // 先清空TreeView面板
            this.treeView.setRoot(null);
            // 从包装类中获取当前二叉树的valueList
            LinkedList<Integer> currentValueList = valueListToList(binaryTreeWrapper.getValueList());
            // 断言二叉树的valueList非空
            assert currentValueList != null;
            // 依据当前二叉树的valueList建立TreeView
            for (int i = 0; i < currentValueList.size(); i++) {
                buildTreeView(this.treeView, i, currentValueList.get(i));
            }
        }
    }

    /**
     * 建立二叉搜索树的TreeView
     * 该方法模仿二叉搜索树的建立过程
     * @param treeView 传入的TreeView画布
     * @param id 当前树目录视图的选项的编号
     * @param value 当前树目录视图的选项的值
     */
    private void buildTreeView(TreeView<String> treeView, int id, int value) {
        // 新建TreeItem
        TreeItem<String> treeItem = new TreeItem<>();
        // 如果当前TreeView为空，直接以当前id和value的新建节点为根节点
        if (treeView.getRoot() == null) {
            treeItem.setValue("tag: " + "root" + " id: " + id + " value: " + value);
            treeView.setRoot(treeItem);
        } else {
            // 如果当前TreeView非空，先记录根节点至tempItem
            TreeItem<String> tempItem = treeView.getRoot();
            // 循环建立过程
            while (tempItem != null) {
                // 分割tempItem的value，提取出当前节点的值
                int tempValue = Integer.parseInt(tempItem.getValue().split("value: ")[1]);
                // 如果当前节点的值小于tempValue，则记录其为左节点
                // 注意：TreeItem无法像TreeNode一样直接有左右节点的判断，所以必须有tag标记左右子节点
                // 所以要对TreeItem的value判断设计独立的方法
                if (value < tempValue) {
                    // 如果当前TreeItem没有左节点
                    if (isNoLeftOrRightItem(tempItem, "left")) {
                        // 更改新建完毕的treeItem的标签，tag标记为left
                        treeItem.setValue("tag: " + "left" + " id: " + id + " value: " + value);
                        // 将该节点加入TreeView，并结束方法
                        tempItem.getChildren().add(treeItem);
                        return;
                    } else {
                        // 从tempItem的子列表里找到左节点（tag标记为left的节点），并将当前节点设置为该节点
                        for (int i = 0; i < tempItem.getChildren().size(); i++) {
                            if (tempItem.getChildren().get(i).getValue().contains("left")) {
                                tempItem = tempItem.getChildren().get(i);
                                break;
                            }
                        }
                    }
                } else {
                    // 如果当前节点的值大于等于tempValue，则记录其为右节点
                    if (isNoLeftOrRightItem(tempItem, "right")) {
                        // 更改新建完毕的treeItem的标签，tag标记为right
                        treeItem.setValue("tag: " + "right" + " id: " + id + " value: " + value);
                        // 将该节点加入TreeView，并结束方法
                        tempItem.getChildren().add(treeItem);
                        return;
                    } else {
                        // 从tempItem的子列表里找到右节点（tag标记为right的节点），并将当前节点设置为该节点
                        for (int i = 0; i < tempItem.getChildren().size(); i++) {
                            if (tempItem.getChildren().get(i).getValue().contains("right")) {
                                tempItem = tempItem.getChildren().get(i);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 判断当前的treeItem是否有左/右子节点
     * @param treeItem 传入的treeItem
     * @param directionTag 节点标记
     * @return true：没有左/右节点；false：有左/右节点
     */
    private boolean isNoLeftOrRightItem(TreeItem<String> treeItem, String directionTag) {
        for (int i = 0; i < treeItem.getChildren().size(); i++) {
            if (treeItem.getChildren().get(i).getValue().contains(directionTag)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 新建工作区
     */
    @FXML
    private void handleNew() {
        for (int i = 0 ; i < mainApp.getTreeChoice().size() ; i++) {
            String currentChoice = mainApp.getTreeChoice().get(i).getChoice();
            BinaryTreeWrapper tempTreeWrapper = new BinaryTreeWrapper(currentChoice);
            mainApp.getTreeChoice().set(i, tempTreeWrapper);
        }
        mainApp.setTreeFilePath(null);
    }

    /**
     * 打开文件选择器，用户选择文件打开到工作区
     */
    @FXML
    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();

        // 设置文件扩展名过滤器
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // 显示保存对话框
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if (file != null) {
            mainApp.loadTreeChoiceFromFile(file);
        }
    }

    /**
     * 保存当前工作区数据至XML文档
     * XML文档生成在该软件目录下
     */
    @FXML
    private void handleSave() {
        // 获取文件路径
        File treeFilePath = mainApp.getTreeFilePath();
        if (treeFilePath != null) {
            mainApp.saveTreeChoiceToFile(treeFilePath);
        } else {
            handleSaveAs();
        }
    }

    /**
     * 另存为工作区数据至用户指定XML文档
     * 打开文件保存浏览，用户在此界面保存数据
     */
    @FXML
    private void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();

        // 设置文件扩展名选择器
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // 显示保存文件对话框
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

        if (file != null) {
            // 确保有正确的扩展名
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            mainApp.saveTreeChoiceToFile(file);
        }
    }

    /**
     * 打开欢迎介绍界面
     */
    @FXML
    private void handleIntroduction() {
        this.mainApp.showIntroductionView();
    }

    @FXML
    private void handleUpdate() {
        this.mainApp.showUpdateLogView();
    }

    /**
     * 打开关于界面
     */
    @FXML
    private void handleAbout() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.initOwner(mainApp.getPrimaryStage());
        alert.setTitle("Binary Tree Visualizer");
        alert.setHeaderText("About");
        alert.setContentText("Author: YFCodeDream\nVersion: 2.0");

        alert.showAndWait();
    }

    /**
     * 退出应用
     */
    @FXML
    private void handleExit() {
        System.exit(0);
    }

    /**
     * 从搜索框获取操作值
     * 该方法捕获NumberFormatException，捕获即结束当前方法并弹窗提示用户
     * @return 用户输入的值
     */
    private int getSearchValue() {
        int searchValue;
        // 用户输入参数合法性校验
        try {
            searchValue = Integer.parseInt(searchValueField.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText("Must be integer!");

            alert.showAndWait();
            return Integer.MIN_VALUE;
        }
        return searchValue;
    }

    /**
     * 清空所有信息
     */
    private void clearMessage() {
        String emptyMessage = "";
        rootValueLabel.setText(emptyMessage);
        heightLabel.setText(emptyMessage);
        numOfNodesLabel.setText(emptyMessage);
        preOrderListLabel.setText(emptyMessage);
        inOrderListLabel.setText(emptyMessage);
        postOrderListLabel.setText(emptyMessage);
        levelListLabel.setText(emptyMessage);
        searchResultLabel.setText(emptyMessage);
        originalListLabel.setText(emptyMessage);
        treeView.setRoot(null);
    }

    /**
     * 在字符串过长时，在合适的地方插入换行
     * @param listString 待插入换行的字符串
     * @return 操作完毕的字符串
     */
    private String setLineBreak(String listString) {
        // 获取对应的字符串构造器
        StringBuilder listStringBuilder = new StringBuilder(listString);
        // 设定换行长度为80个字符
        int lineBreakLength = 80;
        if (listString.length() > lineBreakLength) {
            for (int i = 1 ; i <= listString.length() / lineBreakLength; i++) {
                listStringBuilder.insert(i * lineBreakLength, "\r\n");
            }
        }
        return listStringBuilder.toString();
    }

    /**
     * 转换保存的valueList至LinkedList，方便后续处理
     * @param listString 列表形式的字符串
     * @return 转换完毕的列表
     */
    private LinkedList<Integer> valueListToList(String listString) {
        // 注意读取的时候分隔符是 ,加一个空格！这里天坑
        // 作者在经历了一堆NPE的绝望之后才发现的
        String[] userValueStringList = listString.split(", ");
        LinkedList<Integer> convertedList = new LinkedList<>();
        for (String s : userValueStringList) {
            // 对,分割之后的每一个参数均进行参数合法性校验
            try {
                convertedList.add(Integer.parseInt(s));
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(mainApp.getPrimaryStage());
                alert.setTitle("Invalid Fields");
                alert.setHeaderText("Please correct invalid fields");
                alert.setContentText("Must be integer list!");

                alert.showAndWait();
                return null;
            }
        }
        return convertedList;
    }

    // 指向Main应用主体
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
        treeTable.setItems(mainApp.getTreeChoice());
    }
}
