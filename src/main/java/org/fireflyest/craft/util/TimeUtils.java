package org.fireflyest.craft.util;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author Fireflyest
 * @since 2022/8/26
 */
public class TimeUtils {

    private TimeUtils() {
    }

    public static String getLocalTime(){
        return LocalTime.now().toString();
    }

    public static String getLocalDate(){
        return LocalDate.now().toString();
    }

}
