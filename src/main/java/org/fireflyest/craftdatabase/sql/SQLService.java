package org.fireflyest.craftdatabase.sql;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.Nonnull;

/**
 * 数据库服务
 * @author Fireflyest
 * @since 2022/8/22
 */
public abstract class SQLService {

    @Nonnull
    private final String url;

    /**
     * 初始化数据库服务对象
     * @param url 连接地址
     */
    protected SQLService(@Nonnull String url) {
        this.url = url;

        try {
            SQLServiceInitial.init(this);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException 
            | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取数据库连接地址
     * @return 连接地址
     */
    public String getUrl() {
        return url;
    }

    /**
     * 执行数据库指令
     * @param sql 指令
     */
    public void execute(@Nonnull String sql) {
        // 获取连接
        Connection connection = SQLConnector.getConnect(url);
        // 执行指令
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
