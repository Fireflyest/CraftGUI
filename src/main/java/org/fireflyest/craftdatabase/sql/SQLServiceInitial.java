package org.fireflyest.craftdatabase.sql;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import org.fireflyest.craftdatabase.annotation.Auto;
import org.fireflyest.craftdatabase.annotation.Service;
import org.fireflyest.util.ReflectionUtils;

/**
 * 服务初始化
 * @author Fireflyest
 * @since 2022/8/19
 */
public class SQLServiceInitial {

    private static final Pattern jdbcPattern = Pattern.compile("jdbc:([^:]*)");
    private static final Pattern varPattern = Pattern.compile("\\$\\{([^{]*)}");

    private SQLServiceInitial() {
    }

    /**
     * 初始化数据库服务
     * @param <T> 服务类
     * @param service 服务对象
     * @throws ClassNotFoundException 未找到类
     * @throws NoSuchMethodException 未找到方法
     * @throws InvocationTargetException 无效对象
     * @throws InstantiationException 初始化错误
     * @throws IllegalAccessException 非法访问
     */
    public static <T extends SQLService> void  init(@Nonnull T service) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> serviceClass = service.getClass();
        Service serviceAnnotation = serviceClass.getAnnotation(Service.class);
        if (serviceAnnotation == null) return;
        // 遍历dao成员变量
        for (Field declaredField : serviceClass.getDeclaredFields()) {
            // 判断是否自动实现
            if (declaredField.getAnnotation(Auto.class) == null) continue;
            // 获取实例类后实例化对象并自动赋值
            Class<?> daoClass = declaredField.getType();
            Class<?> daoImplClass = Class.forName(String.format("%s.%sImpl", daoClass.getPackageName(), daoClass.getSimpleName()));
            Constructor<?> declaredConstructor = daoImplClass.getDeclaredConstructor(String.class);
            ReflectionUtils.setField(declaredField, service, declaredConstructor.newInstance(service.getUrl()));
            // 判断是否建表
            if (serviceAnnotation.createTable()) {
                // 获取sql
                Method method = daoImplClass.getMethod("getCreateTableSQL");
                String sql = (String) method.invoke(declaredField.get(service));
                Matcher matcher = jdbcPattern.matcher(service.getUrl());
                if (matcher.find()) {
                    // 建表
                    sql = varReplace(matcher.group().substring(5), sql);
                    if (sql != null) service.execute(sql);
                }
            }
        }
    }

    /**
     * 替换数据类型
     * @param type 数据库类型
     * @param sql 建表语句
     * @return 替换数据类型后的建表语句
     */
    private static String varReplace(String type, String sql) {
        // 是否sqlite
        boolean sqliteType = "sqlite".equals(type);
        if (sqliteType) {
            sql = sql.replace("AUTO_INCREMENT", "AUTOINCREMENT");
        }
        Matcher varMatcher = varPattern.matcher(sql);
        StringBuilder stringBuilder = new StringBuilder();
        while (varMatcher.find()) {
            String parameter = varMatcher.group();
            String parameterName = parameter.substring(2, parameter.length() - 1);
            parameterName = sqliteType ?  javaType2SqliteType(parameterName) :  javaType2MysqlType(parameterName);
            varMatcher.appendReplacement(stringBuilder, parameterName);
        }
        varMatcher.appendTail(stringBuilder);
        return stringBuilder.toString();
    }


    /**
     * 将java数据类型转化为sql数据类型
     * @param type java数据类型
     * @return sql数据类型
     */
    private static String javaType2MysqlType(String type) {
        switch (type) {
            case "int":
                return "int";
            case "long":
            case "java.lang.Long":
                return "bigint";
            case "boolean":
            case "java.lang.Boolean":
            case "short":
            case "java.lang.Short":
                return "tinyint";
            case "java.lang.String":
                return "varchar(127)";
            case "java.lang.Double":
            case "java.lang.Float":
            case "double":
            case "float":
                return "decimal(10,3)";
            case "java.lang.Integer":
                return "integer";
            default:
        }
        return "varchar(63)";
    }

    /**
     * 将java数据类型转化为sql数据类型
     * @param type java数据类型
     * @return sql数据类型
     */
    private static String javaType2SqliteType(String type) {
        switch (type) {
            case "int":
            case "java.lang.Integer":
            case "long":
            case "java.lang.Long":
            case "boolean":
            case "java.lang.Boolean":
            case "short":
            case "java.lang.Short":
                return "integer";
            case "java.lang.String":
                return "varchar(127)";
            case "java.lang.Double":
            case "java.lang.Float":
            case "float":
            case "double":
                return "real";
            default:
        }
        return "varchar(63)";
    }

}
