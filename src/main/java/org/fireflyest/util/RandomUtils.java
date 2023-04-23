package org.fireflyest.util;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RandomUtils {

    private static final Random random = new Random();

    private RandomUtils() {
    }
    
    /**
     * [start, end)区间内随机取一个
     * @param start 起始
     * @param end 末尾
     * @return 随机数
     */
    public static int randomInt(int start, int end) {
        return random.nextInt(end - start) + start;
    }

    /**
     * 随机布尔值
     * @return 随机值
     */
    public static boolean randomBoolean() {
        return random.nextBoolean();
    }

    /**
     * 列表随机取一个
     * @param <T> 泛型
     * @param list 列表
     * @return 随机元素
     */
    @Nullable
    public static <T> T randomGet(@Nonnull List<T> list) {
        if (list.isEmpty()) return null;
        return list.get(randomInt(0, list.size()));
    }

    /**
     * 集里随机取一个
     * @param <T> 泛型
     * @param set 集
     * @return 随机元素
     */
    @Nullable
    public static <T> T randomGet(@Nonnull Set<T> set) {
        if (set.isEmpty()) return null;
        int index = randomInt(0, set.size());
        int pos = 0;
        Iterator<T> iterator = set.iterator();
        while (iterator.hasNext()) {
            T t = iterator.next();
            if (pos ++ == index) return t;
        }
        return null;
    }

}
