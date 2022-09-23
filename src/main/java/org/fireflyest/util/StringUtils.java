package org.fireflyest.util;

import javax.annotation.Nonnull;

/**
 * 一些字符串操作
 * 
 * @author Fireflyest
 * @since 1.1.7
 */
public class StringUtils {
    
    private StringUtils() {
    }

    /**
     * TEST_TEST转换为TestTest
     * @param str 文本
     * @return 拼接
     */
    public static String toLowerCase(@Nonnull String str) {
        StringBuilder sb = new StringBuilder();
        for (String word : str.split("_")) {
            sb.append(word.charAt(0))
                    .append(word.substring(1).toLowerCase());
        }
        return sb.toString();
    }

    /**
     * test转换为Test
     * @param str 文本
     * @return 首字母大写
     */
    public static String toFirstUpCase(@Nonnull String str) {
        return Character.toUpperCase(str.charAt(0)) + str.substring(1).toLowerCase();
    }

}
