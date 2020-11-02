package sample.util;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 生成随机列表工具类
 * @author YFCodeDream
 * @version 1.0
 * @date 2020/10/26 11:20
 */
public class GenerateRandomListUtil {
    public static ArrayList<Integer> generateRandomIntList(int fromIndex, int endIndex) {
        // 如果上限大于下限，该ArrayIndexOutOfBoundsException由用户输入参数时捕获校验
        ArrayList<Integer> randomIntList = new ArrayList<>();

        for (int i = fromIndex ; i < endIndex ; i++) {
            randomIntList.add(i);
        }
        // 调用Collections的shuffle随机打乱列表
        Collections.shuffle(randomIntList);

        return randomIntList;
    }
}
