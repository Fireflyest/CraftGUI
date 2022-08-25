package org.fireflyest.craftdatabase.builder;

/**
 * @author Fireflyest
 * @since 2022/8/14
 */
public interface SQLBuildable {

    /**
     * 构建sql语句
     * @return sql语句
     */
    String build();

}
