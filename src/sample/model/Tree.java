package sample.model;

/**
 * 二叉树需要遵从的接口
 * @author YFCodeDream
 * @version 1.0
 * @date 2020/10/25 13:07
 */
public interface Tree<T> {
    /**
     * 获取根节点的值
     * @return 根节点的值
     */
    T getRootValue();

    /**
     * 获取左节点的值
     * @return 左节点的值
     */
    T getLeftValue();

    /**
     * 获取右节点的值
     * @return 右节点的值
     */
    T getRightValue();

    /**
     * 获取树的高度
     * @return 树的高度
     */
    int getHeight();

    /**
     * 获取树的节点数
     * @return 节点数
     */
    int getNumOfNodes();

    /**
     * 树判空
     * @return true：树为空；false：树不为空
     */
    boolean isEmpty();

    /**
     * 清空树
     */
    void clear();
}
