package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import sample.model.BinaryTree;
import sample.model.BinaryTreeWrapper;
import sample.util.TreeBuilderUtil;
import sample.view.IntroductionViewController;
import sample.view.TreeEditDialogController;
import sample.view.TreeOverviewController;
import sample.view.UpdateLogViewController;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.prefs.Preferences;


public class Main extends Application {
    /**
     * 应用主界面
     */
    private Stage primaryStage;

    /**
     * 左侧工作区选项列表
     */
    private ObservableList<BinaryTreeWrapper> treeChoice = FXCollections.observableArrayList();

    /**
     * 构造方法，初始化左侧工作区选项列表各项对应的初始化二叉树包装类对象
     */
    public Main() {
        treeChoice.add(new BinaryTreeWrapper("Sample Binary Tree"));
        treeChoice.add(new BinaryTreeWrapper("Binary Tree By Recursion"));
        treeChoice.add(new BinaryTreeWrapper("Binary Search Tree"));
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // 设置应用标题
        this.primaryStage.setTitle("Binary Tree Visualizer");

        // 设置主应用的图标
        this.primaryStage.getIcons().add(new Image("file:resources/images/tree-icon.png"));

        // 展示主界面（主界面为TreeOverview.fxml）
        showTreeOverview();
        showIntroductionView();
    }


    public static void main(String[] args) {
        launch(args);
    }

    /**
     * 展示主界面（主界面为TreeOverview.fxml）
     */
    public void showTreeOverview() {
        try {
            // 新建加载器
            FXMLLoader loader = new FXMLLoader();
            // 加载资源
            loader.setLocation(Main.class.getResource("view/TreeOverview.fxml"));
            AnchorPane rootPane = loader.load();

            this.primaryStage.setScene(new Scene(rootPane));

            // 连接视图与控制器
            TreeOverviewController controller = loader.getController();
            controller.setMainApp(this);

            // 展示视图
            this.primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开编辑当前二叉树的对话框
     * 如果OK按钮被按下，则更改当前二叉树对象，并且该方法返回true
     * @param binaryTreeWrapper 传入的二叉树控制器包装类
     * @return true if the user clicked OK, false otherwise.
     */
    public boolean showTreeEditDialog(BinaryTreeWrapper binaryTreeWrapper) {
        try {
            // 加载编辑二叉树的对话框
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/TreeEditDialog.fxml"));
            AnchorPane anchorPane = loader.load();

            // 创建对话框的stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Binary Tree");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(anchorPane);
            dialogStage.setScene(scene);

            // 将当前的二叉树控制器包装类对象传入后台控制器
            TreeEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setBinaryTree(binaryTreeWrapper);

            // 设置对话框的图标
            dialogStage.getIcons().add(new Image("file:resources/images/edit.png"));

            // 展示对话框，直到用户手动关闭它
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 显示欢迎介绍界面
     */
    public void showIntroductionView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/IntroductionView.fxml"));
            AnchorPane anchorPane = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Welcome!");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(anchorPane);
            dialogStage.setScene(scene);

            IntroductionViewController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            // 展示对话框，直到用户手动关闭它
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示更新日志界面
     */
    public void showUpdateLogView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/UpdateLogView.fxml"));
            AnchorPane anchorPane = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Update");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(anchorPane);
            dialogStage.setScene(scene);

            UpdateLogViewController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            // 展示对话框，直到用户手动关闭它
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前文件目录，貌似有点小bug，说不上来，呜呜呜=_=
     * @return 对应的文件目录
     */
    public File getTreeFilePath() {
        Preferences preferences = Preferences.userNodeForPackage(Main.class);
        String filePath = preferences.get("filePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    /**
     * 设置文件目录
     * @param file 传入的文件
     */
    public void setTreeFilePath(File file) {
        Preferences preferences = Preferences.userNodeForPackage(Main.class);
        // 如果文件路径非空，则获取该文件路径
        if (file != null) {
            preferences.put("filePath", file.getPath());
            // 更改应用标题
            this.primaryStage.setTitle("Binary Tree Visualizer - " + file.getName());
        } else {
            preferences.remove("filePath");
            primaryStage.setTitle("Binary Tree Visualizer");
        }
    }

    /**
     * 读取用户指定的XML文档，当前工作区的数据将被替换
     * @param file 待存储的XML文档
     */
    public void loadTreeChoiceFromFile(File file) {
        try {
            // 创建一个SAXBuilder对象
            SAXBuilder builder = new SAXBuilder();
            // 创建一个输入流
            InputStream in = new FileInputStream(file);
            // 处理乱码情况
            InputStreamReader inputStreamReader = new InputStreamReader(in, StandardCharsets.UTF_8);
            // 利用输入流新建文档
            Document document = builder.build(inputStreamReader);
            // 获取XML文档的根节点
            Element treeTableElement = document.getRootElement();
            // 获取根节点下的所有tree元素
            List<Element> allTreeList = treeTableElement.getChildren();
            // 循环遍历读取所有的tree元素
            for (Element treeElement : allTreeList) {
                // 获取每一个tree元素下的对应属性
                List<Element> treeInfoElement = treeElement.getChildren();
                // 初始化currentTreeChoice，currentTreeValueList，记录当前tree元素的创建方式和参数列表
                String currentTreeChoice = "";
                String currentTreeValueList = "";
                // 获取对应的currentTreeChoice，currentTreeValueList
                for (Element element : treeInfoElement) {
                    if (element.getName().equals("treeChoice")) {
                        currentTreeChoice = element.getValue().replaceAll("-", " ");
                    }
                    if (element.getName().equals("treeValueList")) {
                        currentTreeValueList = element.getValue();
                    }
                }
                // 依据currentTreeChoice新建二叉树包装类
                BinaryTreeWrapper tempTreeWrapper = new BinaryTreeWrapper(currentTreeChoice);
                TreeBuilderUtil<Integer> treeBuilderUtil = new TreeBuilderUtil<>();
                BinaryTree<Integer> binaryTree = new BinaryTree<>();
                // 循环遍历左侧工作区选项，并依据选项指定的创建方式和参数列表建立对应的二叉树
                for (int i = 0; i < this.getTreeChoice().size(); i++) {
                    if (this.getTreeChoice().get(i).getChoice().equals(currentTreeChoice)) {
                        if (currentTreeChoice.equals("Sample Binary Tree")) {
                            binaryTree = TreeBuilderUtil.buildSampleTree();
                        }
                        if (currentTreeChoice.equals("Binary Tree By Recursion")) {
                            treeBuilderUtil.buildBinaryTreeByRecursion(binaryTree, valueListToList(currentTreeValueList));
                        }
                        if (currentTreeChoice.equals("Binary Search Tree")) {
                            treeBuilderUtil.buildBinarySearchTree(binaryTree, Objects.requireNonNull(valueListToList(currentTreeValueList)));
                        }
                        // 将依据XML文件信息创建完毕的二叉树传入二叉树包装类
                        tempTreeWrapper.setBinaryTree(binaryTree);
                        // 对应的参数列表传入二叉树包装类存储
                        tempTreeWrapper.setValueList(currentTreeValueList);
                        // 选中当前创建完毕的选项
                        this.getTreeChoice().set(i, tempTreeWrapper);
                    }
                }
            }
            // 保存文件路径
            setTreeFilePath(file);
        } catch (Exception e) {
            // 捕获所有异常
            // 不得不说，这种操作比较拉垮，有点不负责任的感jio，不能分门别类地捕获异常并给予具体提示
            // 但是省事儿啊（笑哭），一个星期懒得考虑辣么多了
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load data");
            alert.setContentText("Could not load data from file:\n" + file.getPath());

            alert.showAndWait();
        }
    }

    /**
     * 保存当前工作区数据至XML文档
     * @param file 待存储的XML文档
     */
    public void saveTreeChoiceToFile(File file) {
        try {
            // 1、生成一个根节点
            Element treeTableElement = new Element("treeTable");
            // 2、为节点添加属性
            treeTableElement.setAttribute("version", "1.0");
            // 3、生成一个document对象
            Document document = new Document(treeTableElement);

            for (int i = 0 ; i < this.getTreeChoice().size() ; i++) {
                Element treeElement = new Element("tree");
                // 注意这里为了保存，把treeChoice的空格换成了-，在读取时要重新replace
                String treeChoiceText = this.getTreeChoice().get(i).getChoice().replaceAll(" ", "-");
                Element treeChoiceElement = new Element("treeChoice");
                treeChoiceElement.setText(treeChoiceText);

                // 获取待存储的数据信息（创建方式、参数列表）
                BinaryTree<Integer> currentTree = this.getTreeChoice().get(i).getBinaryTree();
                LinkedList<Integer> currentValueList = new LinkedList<>();
                currentTree.levelTraverseList(currentTree.getRoot(), currentValueList);

                // 保存时把两端的括号去除，方便读取时处理数据
                String treeValueListText = currentValueList.toString().
                        substring(1, currentValueList.toString().length() - 1);
                Element treeValueListElement = new Element("treeValueList");
                treeValueListElement.setText(treeValueListText);

                // 将数据写入XML文档
                treeElement.addContent(treeChoiceElement);
                treeElement.addContent(treeValueListElement);
                treeTableElement.addContent(treeElement);
            }

            Format format = Format.getCompactFormat();
            // 设置换行Tab或空格
            format.setIndent("	");
            format.setEncoding("UTF-8");

            XMLOutputter outputUtil = new XMLOutputter(format);
            outputUtil.output(document, new FileOutputStream(file));
            // 保存文件路径
            setTreeFilePath(file);
        } catch (Exception e) {
            // 同样捕获所有异常（偷懒ing）
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not save data");
            alert.setContentText("Could not save data to file:\n" + file.getPath());

            alert.showAndWait();
        }
    }

    /**
     * 转换保存的valueList至ArrayList，方便后续处理
     * @param listString 列表形式的字符串
     * @return 转换完毕的列表
     */
    private ArrayList<Integer> valueListToList(String listString) {
        // 注意读取的时候分隔符是 ,加一个空格！这里天坑
        // 作者在经历了一堆NPE的绝望之后才发现的
        String[] userValueStringList = listString.split(", ");
        ArrayList<Integer> convertedList = new ArrayList<>();
        for (String s : userValueStringList) {
            try {
                convertedList.add(Integer.parseInt(s));
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(primaryStage);
                alert.setTitle("Invalid Fields");
                alert.setHeaderText("Please correct invalid fields");
                alert.setContentText("Must be integer list!");

                alert.showAndWait();
                return null;
            }
        }
        return convertedList;
    }

    /**
     * 获取主界面的引用
     * @return 主界面的引用
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * 获取当前左侧工作区选项列表
     * @return 左侧工作区选项列表
     */
    public ObservableList<BinaryTreeWrapper> getTreeChoice() {
        return this.treeChoice;
    }
}
