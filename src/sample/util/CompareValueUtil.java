package sample.util;

/**
 * 泛型比较工具
 * 规定泛型上限：Comparable<T>
 * 实现两个泛型对象的比较
 * 用于：
 *     1、构建二叉搜索树
 * @author YFCodeDream
 * @version 1.0
 * @date 2020/10/25 20:06
 */
public class CompareValueUtil<T extends Comparable<T>> {
    /**
     * 传入的两个泛型对象
     * @param value1 泛型对象1
     * @param value2 泛型对象2
     * @return 比较的结果，返回两个泛型对象的较大者
     */
    public T isGreater(T value1, T value2) {
        return value1.compareTo(value2) > 0 ? value1 : value2;
    }
}
