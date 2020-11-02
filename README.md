# README

## Binary-Tree-Visualizer-master

这是一个基于JavaFX Application的二叉树的可视化工具，提供了以下功能：

1. 提供以下类型的二叉树的可视化：
   1. 样例二叉树。这是一棵四层的满二叉树，节点值等于它的编号值
   2. 按照递归生成的一般二叉树
   3. 二叉搜索树
2. 每一种二叉树的建立均在Tree Figure中生成图片进行直观的可视化
3. 对于二叉搜索树，还支持查询、插入、删除节点，并在右侧的Tree Structure里生成树结构目录，每个节点均有tag标签表明它是左节点还是右节点
4. 想保存当前工作区的数据？该可视化工具还提供了保存数据至XML文档的功能，你完全可以由Save和Save As保存至你想要的位置
5. 每一次生成二叉树都会在你当前的工作目录下生成一张testGraph.png的图片，它就是在Tree Figure中实时显示的图像。你可以自己保留或者删除它
6. 请先阅读Introduction，它会给予一些提示，帮助你更好地使用这个工具

这是Version1.0的版本，囿于作者技术水平和时间有限，可能存在若干bug，欢迎在issues留言。也可以在CSDN私信作者（CSDN ID：YFCodeDream）

### Version2.0
对用户选择自定义参数列表进行重复值校验，如果用户输入重复值，则仅保留一个元素，并显示警告弹窗

在使用二叉搜索树时，拒绝用户重复插入已经存在于值列表中的节点

> Thanks:

> JavaFX教程：https://code.makery.ch/zh-cn/library/javafx-tutorial/

> 绘制二叉树工具类参考资料：https://blog.csdn.net/hehenwm/article/details/38563523

> fontawesome字体图标库
