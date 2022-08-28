package org.fireflyest.craftdatabase.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 列
 * @author Fireflyest
 * @since 2022/8/17
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.SOURCE)
public @interface Column {
    String name() default "";
    String dataType() default "";
    boolean noNull() default false;
    String defaultValue() default "NULL";
}
