package sample.util;

import sample.model.TreeNode;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * 绘制二叉树的工具类参考以下资料
 * 版权声明：本文为博主原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接和本声明。
 * 本文链接：https://blog.csdn.net/hehenwm/article/details/38563523
 * @author hehenwm
 * @modified YFCodeDream
 * @version 1.0
 * @date 2020/10/26 13:26
 */
public class GraphTreeUtil<T> {
    /**
     * 树的总高度，由TreeNode类的getHeight方法获取
     */
    private int totalLevel = 0;

    /**
     * 画布绘制点的左边距定位
     */
    private int leftPadding = Integer.MAX_VALUE;

    /**
     * 画布右边距，默认值为15（单位同层相邻点间隔）
     */
    private int rightBorder;

    /**
     * 画布底边距，默认值为80（单位层间隔）
     */
    private int bottomBorder;

    /**
     * 绘制二叉树对外API
     * @param root 二叉树的根节点
     * @param fileName 保存文件名
     * @throws IOException 文件路径无效，则抛出异常
     */
    public void PaintTree(TreeNode<T> root, String fileName) throws IOException {
        if (root == null) {
            System.out.println("The root of the tree is null");
            return;
        }

        totalLevel = root.getHeight(root) - 1;

        BufferedImage bufferedImage = new BufferedImage(rightBorder, bottomBorder, BufferedImage.TYPE_INT_BGR);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        graphics2D.setBackground(Color.cyan);
        graphics2D.setColor(Color.blue);

        ArrayList<TreeNode<T>> nodeList = new ArrayList<>();
        nodeList.add(root);

        GraphTreeByBFS(graphics2D, nodeList, 0, false);

        bufferedImage = new BufferedImage(rightBorder, bottomBorder, BufferedImage.TYPE_INT_BGR);
        graphics2D = bufferedImage.createGraphics();

        GraphTreeByBFS(graphics2D, nodeList, 0, true);

        ImageIO.write(bufferedImage, "png", new File(fileName + ".png"));
        this.leftPadding = Integer.MAX_VALUE;
    }

    /**
     * 采用广度优先遍历（层序遍历）绘制二叉树
     * @param graphics2D 待绘制画布
     * @param list 存储所有节点的ArrayList
     * @param level 当前层数
     * @param repaint 覆盖绘制标志位
     */
    private void GraphTreeByBFS(Graphics2D graphics2D, ArrayList<TreeNode<T>> list, int level, boolean repaint) {
        ArrayList<TreeNode<T>> buffer = new ArrayList<>();
        for (int i = 0 ; i < list.size() ; i++) {
            TreeNode<T> tempNode = list.get(i);
            GraphicNode graphicNode;
            if (tempNode != null) {
                graphicNode = new GraphicNode(level, i, totalLevel);
                int graphicNodeX = graphicNode.getPoint(repaint).x;
                int graphicNodeY = graphicNode.getPoint(repaint).y;

                if (graphicNodeX > rightBorder) {
                    rightBorder = graphicNodeX + GraphicNode.LOWEST_PADDING * 2;
                }
                if (graphicNodeY > bottomBorder) {
                    bottomBorder = graphicNodeY + GraphicNode.LEVEL_PADDING;
                }

                graphics2D.drawString(tempNode.getValue().toString() + " ", graphicNodeX, graphicNodeY);
                if ((graphicNodeX < leftPadding) && !repaint) {
                    leftPadding = graphicNodeX;
                }
                if (tempNode.getLeft() != null) {
                    int leftIndex = i * 2;
                    getGraphicNode(graphics2D, level, repaint, graphicNodeX, graphicNodeY, leftIndex);
                }
                buffer.add(tempNode.getLeft());
                if (tempNode.getRight() != null) {
                    int rightIndex = i * 2 + 1;
                    getGraphicNode(graphics2D, level, repaint, graphicNodeX, graphicNodeY, rightIndex);
                }
                buffer.add(tempNode.getRight());
            } else {
                buffer.add(null);
                buffer.add(null);
            }
        }
        if (!isBufferEmpty(buffer)) {
            GraphTreeByBFS(graphics2D, buffer, level + 1, repaint);
        }
    }

    /**
     * 获取绘制画布点，并更改右边界和底边界
     * @param graphics2D 待绘制画布
     * @param level 当前层数
     * @param repaint 覆盖绘制标志位
     * @param graphicNodeX 画布绘制点的x轴坐标
     * @param graphicNodeY 画布绘制点的y轴坐标
     * @param index 当前序号
     */
    private void getGraphicNode(Graphics2D graphics2D, int level, boolean repaint, int graphicNodeX, int graphicNodeY, int index) {
        GraphicNode graphicNode = new GraphicNode(level + 1, index, totalLevel);
        graphics2D.drawLine(graphicNodeX + 5, graphicNodeY,
                graphicNode.getPoint(repaint).x + 5,
                graphicNode.getPoint(repaint).y - 10);

        // x轴加10，y轴加40为了给画布留出一定余量
        if (graphicNode.getPoint(repaint).x > rightBorder) {
            rightBorder = graphicNode.getPoint(repaint).x + GraphicNode.LOWEST_PADDING * 2;
        }
        if (graphicNode.getPoint(repaint).y > bottomBorder) {
            bottomBorder = graphicNode.getPoint(repaint).y + GraphicNode.LEVEL_PADDING;
        }
    }

    /**
     * 缓冲队列判空
     * @param list 缓冲队列ArrayList
     * @return true：缓冲队列为空；false：缓冲队列非空
     */
    private boolean isBufferEmpty(List<TreeNode<T>> list) {
        boolean result = true;
        for (TreeNode<T> node : list) {
            if (node != null) {
                result = false;
                break;
            }
        }
        return result;
    }

    /**
     * 点内部类
     * x：横坐标
     * y：纵坐标
     */
    private static class Point{
        private int x;
        private int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    /**
     * 画布点内部类
     */
    private class GraphicNode{
        /**
         * 实例点
         */
        private Point point;

        /**
         * 同层实例点相距间隔，默认值为15
         */
        public static final int LOWEST_PADDING = 60;

        /**
         * 层之间相距间隔，默认值为80
         */
        public static final int LEVEL_PADDING = 80;

        /**
         * 总层数
         */
        private int totalLevel;

        /**
         * 有参构造方法
         * @param level 当前层数
         * @param index 实例点的序号
         * @param totalLevel 总层数
         */
        public GraphicNode(int level, int index, int totalLevel){
            this.totalLevel = totalLevel;
            this.point = computePoint(level, index);
        }

        /**
         * 获取实例点
         * @param repaint 覆盖绘制标志位
         * @return 实例点对象
         */
        public Point getPoint(boolean repaint){
            if(repaint){
                return new Point(point.x - leftPadding, point.y);
            }
            return point;
        }

        /**
         * 计算点的坐标
         * @param level 当前层数
         * @param index 实例点的序号
         * @return 满足坐标要求的实例点
         */
        private Point computePoint(int level, int index) {
            int x = 0;
            for(int i = this.totalLevel; i >level; i--){
                x += getLevelPadding(i) / 2;
            }
            x += getLevelPadding(level) * index;
            int y = LEVEL_PADDING * (level + 1);
            return new Point(x, y);
        }

        /**
         * 获取当前层数的点间隔
         * @param level 当前层数
         * @return 当前层相邻点的间隔
         */
        private int getLevelPadding(int level){
            return (int) (LOWEST_PADDING * Math.pow(2, this.totalLevel - level));
        }
    }

    public GraphTreeUtil() {
        this(GraphicNode.LOWEST_PADDING * 2, GraphicNode.LEVEL_PADDING * 2);
    }

    public GraphTreeUtil(int rightBorder, int bottomBorder) {
        this.rightBorder = rightBorder;
        this.bottomBorder = bottomBorder;
    }
}
