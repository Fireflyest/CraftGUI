package org.fireflyest.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

/**
 * 时间工具类
 * @author Fireflyest
 * @since 1.1.6
 */
public class TimeUtils {

    private TimeUtils() {
    }

    /**
     * 获取当前纪元时间
     * @return 毫秒
     */
    public static long getTime() {
        return Instant.now().toEpochMilli();
    }

    /**
     * 获取当前时间
     * @return 时间字符串
     */
    public static String getLocalTime() {
        String string = LocalTime.now().toString();
        return string.substring(0, string.lastIndexOf("."));
    }

    /**
     * 转化成时间字符串
     * @param time 纪元时间
     * @return 时间字符串
     */
    public static String getLocalTime(long time) {
        String string =  LocalTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()).toString();
        return string.substring(0, string.lastIndexOf("."));
    }

    /**
     * 获取当前日期
     * @return 日期字符串
     */
    public static String getLocalDate() {
        return LocalDate.now().toString();
    }

    /**
     * 转化成日期字符串
     * @param time 纪元时间
     * @return 日期字符串
     */
    public static String getLocalDate(long time) {
        return LocalDate.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()).toString();
    }

    /**
     * 转换时间为Instant对象
     * @param time 时间
     * @return
     */
    public static Instant getInstant(long time) {
        return Instant.ofEpochMilli(time);
    }

    /**
     * 持续时间时间
     * @param time 时间数据
     * @return String
     */
    public static String duration(long time) {
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;
        long day = time / dd;
        long hour = (time - day * dd) / hh;
        long minute = (time - day * dd - hour * hh) / mi;
        long second = (time - day * dd - hour * hh - minute * mi) / ss;
        StringBuilder stringBuilder = new StringBuilder();
        if (day > 0) stringBuilder.append(String.format("%s天", day));
        if (hour > 0) stringBuilder.append(String.format("%s时", hour));
        if (minute > 0) stringBuilder.append(String.format("%s分", minute));
        if (second > 0) stringBuilder.append(String.format("%s秒", second));
        return stringBuilder.toString();
    }

}
