package org.fireflyest.util;

import org.bukkit.Color;

import javax.annotation.Nonnull;
import java.util.regex.Pattern;

/**
 * @author Fireflyest
 * @since 2022/8/29
 */
public class ColorUtils {

    private static final Pattern colorPattern = Pattern.compile("^#([0-9a-fA-F]{6})$");

    private ColorUtils() {
    }

    public static String toString(@Nonnull Color color){
        return "#" + Integer.toHexString(color.asRGB());
    }

    /**
     * 渐变颜色
     * @param start 起始颜色
     * @param end 结束颜色
     * @param num 过渡颜色数量，不少于2
     * @return 过渡颜色
     */
    public static Color[] gradient(@Nonnull String start, @Nonnull String end, int num){
        Color[] colors = new Color[num];
        // 判断格式是否正确
        if (num < 2) num = 2;
        if (!colorPattern.matcher(start).matches() || !colorPattern.matcher(end).matches()) return colors;
        // 转化成整数计算
        int startColor = Integer.parseInt(start.substring(1), 16);
        int endColor = Integer.parseInt(end.substring(1), 16);
        // 计算过渡
        int startR, startG, startB;
        float deltaR = (r(endColor) - (startR = r(startColor))) / (num-1F);
        float deltaG = (g(endColor) - (startG = g(startColor))) / (num-1F);
        float deltaB = (b(endColor) - (startB = b(startColor))) / (num-1F);
        for (int i = 0; i < num; i++) {
            Color color = Color.fromRGB(
                    startR + (int) (i * deltaR),
                    startG + (int) (i * deltaG),
                    startB + (int) (i * deltaB));
            colors[i] = color;
        }
        return colors;
    }

    /**
     * 获取红色
     * @param color 颜色
     * @return 红色
     */
    public static int r(int color){
        return color>>>16;
    }

    /**
     * 获取绿色
     * @param color 颜色
     * @return 绿色
     */
    public static int g(int color){
        color<<=16;
        return color>>>24;
    }

    /**
     * 获取蓝色
     * @param color 颜色
     * @return 蓝色
     */
    public static int b(int color){
        color<<=24;
        return color>>>24;
    }

}
