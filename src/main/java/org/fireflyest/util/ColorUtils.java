package org.fireflyest.util;

import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import org.bukkit.Color;

/**
 * 颜色工具类
 * @author Fireflyest
 * @since 1.1.6
 */
public class ColorUtils {

    private static final Pattern colorPattern = Pattern.compile("^#([0-9a-fA-F]{6})$");

    private ColorUtils() {
    }

    /**
     * 将颜色转为字符串
     * @param color 颜色
     * @return 颜色字符串
     */
    public static String toString(@Nonnull Color color) {
        return "#" + Integer.toHexString(color.asRGB());
    }

    /**
     * 文本转换为颜色
     * @param color 颜色
     * @return 颜色
     */
    public static Color toColor(@Nonnull String color) {
        color = color.replace("#", "");
        return Color.fromRGB(Integer.parseInt(color, 16));
    }

    /**
     * 渐变颜色
     * @param start 起始颜色
     * @param end 结束颜色
     * @param num 过渡颜色数量，不少于2
     * @return 过渡颜色
     */
    public static String[] gradient(@Nonnull String start, @Nonnull String end, int num) {
        // 判断格式是否正确
        if (num < 2) num = 2;
        if (num > 255) num = 255;
        String[] colors = new String[num];
        if (!colorPattern.matcher(start).matches() || !colorPattern.matcher(end).matches()) return colors;
        // 转化成整数计算
        int startColor = Integer.parseInt(start.substring(1), 16);
        int endColor = Integer.parseInt(end.substring(1), 16);
        // 计算过渡
        int startR = rr(startColor);
        int startG = gg(startColor);
        int startB = bb(startColor);
        float deltaR = (rr(endColor) - startR) / (num - 1F);
        float deltaG = (gg(endColor) - startG) / (num - 1F);
        float deltaB = (bb(endColor) - startB) / (num - 1F);
        for (int i = 0; i < num; i++) {
            colors[i] = "#" 
                + Integer.toHexString(startR + (int) (i * deltaR))
                + Integer.toHexString(startG + (int) (i * deltaG))
                + Integer.toHexString(startB + (int) (i * deltaB));
        }
        return colors;
    }

    /**
     * 两种颜色的距离
     * @param color1 颜色1
     * @param color2 颜色2
     * @return 距离
     */
    public static int distance(Color color1, Color color2) {
        return (int)(Math.pow(color1.getRed() - (double)color2.getRed(), 2) 
            + Math.pow(color1.getGreen() - (double)color2.getGreen(), 2) 
            + Math.pow(color1.getBlue() - (double)color2.getBlue(), 2));
    }

    /**
     * 获取红色
     * @param color 颜色
     * @return 红色
     */
    public static int rr(int color) {
        return (color & 0xFF0000) >> 16;
    }

    /**
     * 获取绿色
     * @param color 颜色
     * @return 绿色
     */
    public static int gg(int color) {
        return (color & 0x00FF00) >> 8;
    }

    /**
     * 获取蓝色
     * @param color 颜色
     * @return 蓝色
     */
    public static int bb(int color) {
        return color & 0x0000FF;
    }

}
