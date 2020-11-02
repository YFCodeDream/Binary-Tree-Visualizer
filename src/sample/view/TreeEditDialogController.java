package sample.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import sample.model.BinaryTree;
import sample.model.BinaryTreeWrapper;
import sample.util.GenerateRandomListUtil;
import sample.util.TreeBuilderUtil;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * 显示二叉树编辑具体对话框
 *
 * @author YFCodeDream
 */
public class TreeEditDialogController {

    @FXML
    private TextField fromIndexField;
    @FXML
    private TextField endIndexField;
    @FXML
    private TextField valueListField;
    @FXML
    private ComboBox<String> choiceComboBox;

    private String userChoice = "random list";
    private Stage dialogStage;
    private BinaryTreeWrapper binaryTreeWrapper;
    private boolean okClicked = false;

    /**
     * 初始化对话框，在视图被加载时该方法被自动调用
     */
    public void initialize() {
        // 复选框设置两种输入参数列表的方式
        // random list：用户指定上下限，生成该上下限区间的自然数随机列表
        // self defined list：用户指定列表
        choiceComboBox.getItems().addAll("random list",
                "self defined list");
        choiceComboBox.setValue("random list");
        setUserChoiceEditable();
    }

    /**
     * 设置用户编辑对话框主界面
     * @param dialogStage 对话框主界面
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;

        // 设置对话框图标
        this.dialogStage.getIcons().add(new Image("file:resources/images/edit.png"));
    }

    /**
     * 修改传入二叉树包装类的底层二叉树
     * @param binaryTreeWrapper 传入的二叉树控制器包装类
     */
    public void setBinaryTree(BinaryTreeWrapper binaryTreeWrapper) {
        this.binaryTreeWrapper = binaryTreeWrapper;
        // 清空输入框
        fromIndexField.setText("");
        endIndexField.setText("");
        valueListField.setText("");
        // 监听用户的复选框的选择，并针对不同选择启用/禁用相关编辑选项
        choiceComboBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    userChoice = newValue;
                    setUserChoiceEditable();});
    }

    /**
     * 如果用户触发OK按钮，返回true
     *
     * @return true：用户按下OK按钮
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * 当用户按下OK按钮后，从用户编辑框获取数据
     * 若触发异常，由相关异常处理逻辑捕获，并以提示框形式显示
     * 根据获取的数据创建二叉树，并由该二叉树对象再创建对应的二叉树包装类对象
     * 二叉树包装类对象对应控制器，将相关信息显示在视图面板上
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            BinaryTree<Integer> binaryTree = new BinaryTree<>();
            ArrayList<Integer> userValueList = new ArrayList<>();

            if (userChoice.equals("random list")) {
                int fromIndex = Integer.parseInt(fromIndexField.getText());
                int endIndex = Integer.parseInt(endIndexField.getText());
                // 输入上限高于输入下限异常处理逻辑
                if (fromIndex >= endIndex) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.initOwner(dialogStage);
                    alert.setTitle("Invalid Fields");
                    alert.setHeaderText("Please correct invalid fields");
                    alert.setContentText("The end index MUST be greater than from index!");

                    alert.showAndWait();
                    return;
                }
                userValueList = GenerateRandomListUtil.generateRandomIntList(fromIndex, endIndex);
                valueListField.setText(userValueList.toString());
            } else {
                String[] userValueStringList = valueListField.getText().split(",");
                // 对每一输入均进行参数合法性校验，非法数据直接结束方法
                for (String s : userValueStringList) {
                    try {
                        userValueList.add(Integer.parseInt(s));
                    } catch (NumberFormatException e) {
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.initOwner(dialogStage);
                        alert.setTitle("Invalid Fields");
                        alert.setHeaderText("Please correct invalid fields");
                        alert.setContentText("Must be integer list!");

                        alert.showAndWait();
                        return;
                    }
                }
                long originalLength = userValueList.stream().distinct().count();
                if (originalLength != userValueList.size()) {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.initOwner(dialogStage);
                    alert.setTitle("Invalid Fields");
                    alert.setHeaderText("Please correct invalid fields");
                    alert.setContentText("The input value list has repeated item, which will be remained only once.");

                    alert.showAndWait();

                    removeDuplicatedValue(userValueList);
                }
            }
            // 新建构建二叉树工具类
            TreeBuilderUtil<Integer> treeBuilderUtil = new TreeBuilderUtil<>();

            // 针对不同创建方式创建对应二叉树
            if (this.binaryTreeWrapper.getChoice().equals("Sample Binary Tree")) {
                binaryTree = TreeBuilderUtil.buildSampleTree();
            }
            if (this.binaryTreeWrapper.getChoice().equals("Binary Tree By Recursion")) {
                treeBuilderUtil.buildBinaryTreeByRecursion(binaryTree, userValueList);
            }
            if (this.binaryTreeWrapper.getChoice().equals("Binary Search Tree")) {
                treeBuilderUtil.buildBinarySearchTree(binaryTree, userValueList);
            }
            // 更改二叉树包装类对象的底层二叉树
            this.binaryTreeWrapper.setBinaryTree(binaryTree);
            // 更改valueList存储数据
            this.binaryTreeWrapper.setValueList(listToValueList(userValueList));

            okClicked = true;
            dialogStage.close();
        }
    }

    /**
     * 用户触发Cancel按钮代码逻辑
     * 当前对话框关闭
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * 用户输入参数合法性校验
     *
     * @return true：用户输入参数合法
     */
    private boolean isInputValid() {
        String errorMessage = "";

        // 如果用户选择random list，则对fromIndexField和endIndexField传回的参数进行合法性校验
        if (userChoice.equals("random list")) {
            if (fromIndexField.getText() == null || fromIndexField.getText().length() == 0) {
                errorMessage += "No valid first index!\n";
            } else {
                try {
                    Integer.parseInt(fromIndexField.getText());
                } catch (NumberFormatException e) {
                    errorMessage += "No valid first index (must be an integer)!\n";
                }
            }

            if (endIndexField.getText() == null || endIndexField.getText().length() == 0) {
                errorMessage += "No valid end index!\n";
            } else {
                try {
                    Integer.parseInt(endIndexField.getText());
                } catch (NumberFormatException e) {
                    errorMessage += "No valid end index (must be an integer)!";
                }
            }
        }

        // 如果用户选择self defined list，则对valueListField传回的参数进行合法性校验
        if (userChoice.equals("self defined list")) {
            if (valueListField.getText() == null || valueListField.getText().length() == 0) {
                errorMessage += "No valid value list!\n";
            }
        }

        // 如果没有异常信息，返回输入有效标志位，并结束校验
        if (errorMessage.length() == 0) {
            return true;
        } else {
            // 异常提示
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }

    /**
     * 设置控件输入权限
     */
    private void setUserChoiceEditable() {
        // 如果用户选择random list
        // 启用fromIndexField，endIndexField，禁用valueListField
        if (userChoice.equals("random list")) {
            valueListField.setEditable(false);

            fromIndexField.setEditable(true);
            endIndexField.setEditable(true);
        }
        // 如果用户选择self defined list
        // 启用valueListField，禁用fromIndexField，endIndexField
        if (userChoice.equals("self defined list")) {
            fromIndexField.setEditable(false);
            endIndexField.setEditable(false);

            valueListField.setEditable(true);
        }
    }

    private void removeDuplicatedValue(List<Integer> list) {
        LinkedHashSet<Integer> linkedHashSet = new LinkedHashSet<>(list);
        list.clear();
        list.addAll(linkedHashSet);
    }

    /**
     * 转换列表至字符串，方便后续控件处理
     * @param list 传入的列表
     * @return 对应的列表字符串
     */
    private String listToValueList(List<Integer> list) {
        String listString = list.toString();
        return listString.substring(1, listString.length() - 1);
    }
}