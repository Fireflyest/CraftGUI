package org.fireflyest.craft.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

/**
 * @author Fireflyest
 * @since 2022/8/26
 */
public class TimeUtils {

    private TimeUtils() {
    }

    /**
     * 获取当前纪元时间
     * @return 毫秒
     */
    public static long getTime(){
        return Instant.now().toEpochMilli();
    }

    /**
     * 获取当前时间
     * @return 时间字符串
     */
    public static String getLocalTime(){
        String string =LocalTime.now().toString();
        return string.substring(0, string.lastIndexOf("."));
    }

    /**
     * 转化成时间字符串
     * @param time 纪元时间
     * @return 时间字符串
     */
    public static String getLocalTime(long time){
        String string =  LocalTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()).toString();
        return string.substring(0, string.lastIndexOf("."));
    }

    /**
     * 获取当前日期
     * @return 日期字符串
     */
    public static String getLocalDate(){
        return LocalDate.now().toString();
    }

    /**
     * 转化成日期字符串
     * @param time 纪元时间
     * @return 日期字符串
     */
    public static String getLocalDate(long time){
        return LocalDate.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault()).toString();
    }

}
