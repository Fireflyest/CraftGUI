package org.fireflyest.util;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.util.NumberConversions;
import org.fireflyest.util.RegexUtils.VariableContainer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 一些字符串操作
 * 
 * @author Fireflyest
 * @since 1.1.7
 */
public class StringUtils {
    
	private static final Gson gson = new Gson();

    private StringUtils() {
    }

    /**
     * 格式化文本
     * @param text 文本
     * @param vars 变量
     * @return 文本
     */
    public static String format(String text, String... vars) {
        if (text == null) {
            return null;
        }
        return RegexUtils.varReplace(RegexUtils.FORMAT_PATTERN, text, new VariableContainer() {
            int index = 0;
            @Override
            public String getVar(String key) {
                if ("".equals(key)) return vars[index++];
                return vars[NumberConversions.toInt(key)];
            }
        });
    }

    /**
     * TEST_TEST转换为TestTest
     * @param str 文本
     * @return 大驼峰
     */
    public static String toLowerCase(@Nonnull String str) {
        StringBuilder sb = new StringBuilder();
        for (String word : str.split("_")) {
            sb.append(word.charAt(0)).append(word.substring(1).toLowerCase());
        }
        return sb.toString();
    }

	/**
	 * TestTest转换为TEST_TEST
	 * @param str 文本
	 * @return 大写下划线
	 */
	public static String toUpperCase(@Nonnull String str) {
        StringBuilder sb = new StringBuilder();
		// 第一个字符大写
		sb.append(str.substring(0, 1).toUpperCase());
		for (int i = 1; i < str.length(); i++) {
			char aChar = str.charAt(i);
		   	// 在大写字母前添加下划线
		   	if (Character.isUpperCase(aChar) && !Character.isDigit(aChar)) {
				sb.append("_");
			}
			// 其他字符直接转成大写
		   sb.append(Character.toUpperCase(aChar));
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
	 * 列表转换为文本
	 * @param obj 列表
	 * @return 文本
	 */
	public static String toJsonString(@Nonnull Object obj) {
		return gson.toJson(obj);
	}

	/**
	 * 文本转列表
	 * @param <T> 对象类型
	 * @param string 文本
	 * @return 列表
	 */
	public static <T> List<T> stringToList(@Nullable String string) {
        return string == null ? new ArrayList<>() : gson.fromJson(string, new TypeToken<List<T>>() {}.getType());
	}

	/**
	 * 文本转对象
	 * @param <T> 对象类型
	 * @param string 文本
	 * @param type 对象的类
	 * @return 对象
	 */
	public static <T> T stringToObj(@Nullable String string, Class<T> type) {
        return string == null ? null : gson.fromJson(string, type);
	}

	/**
	 * 文本进度条
	 * @param progress 进度
	 * @param bgc 背景颜色
	 * @param fgc 前景颜色
	 * @param max 最大数量
	 * @return 进度条文本
	 */
	public static String stringProgress(double progress, String bgc, String fgc, int max) {
		StringBuilder sb = new StringBuilder(fgc);
		String bar = "▎";
		int progressBar = (int)(max * progress);
		sb.append(bar.repeat(progressBar))
			.append(bgc)
			.append(bar.repeat(max - progressBar));
		return sb.toString();
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
