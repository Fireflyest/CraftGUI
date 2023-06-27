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

    /**
	 * 版本号比较
	 * @param v1 版本1
	 * @param v2 版本2
	 * @return 0代表相等，1代表左边大，-1代表右边大
	 */
	public static int compareVersion(String v1, String v2) {
		if (v1.equals(v2)) {
			return 0;
		}
		String[] version1Array = v1.split("[._]");
		String[] version2Array = v2.split("[._]");
		int index = 0;
		int minLen = Math.min(version1Array.length, version2Array.length);
		long diff = 0;

		while (index < minLen
			&& (diff = Long.parseLong(version1Array[index])
			- Long.parseLong(version2Array[index])) == 0) {
			index++;
		}
		if (diff == 0) {
			for (int i = index; i < version1Array.length; i++) {
				if (Long.parseLong(version1Array[i]) > 0) {
					return 1;
				}
			}
			for (int i = index; i < version2Array.length; i++) {
				if (Long.parseLong(version2Array[i]) > 0) {
					return -1;
				}
			}
			return 0;
		}
        return diff > 0 ? 1 : -1;
	}

}
