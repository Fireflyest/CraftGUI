package org.fireflyest.craftdatabase.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 类连接池
 * @author Fireflyest
 * @since 2022/8/17
 */
public class SQLConnector {

    public static final String MYSQL = "com.mysql.cj.jdbc.Driver";
    public static final String MYSQL_OLD = "com.mysql.jdbc.Driver";
    public static final String SQLITE = "org.sqlite.JDBC";

    private static final Set<String> loadedClass = new HashSet<>();
    private static final Map<String, ConnectInfo> connectInfoMap = new ConcurrentHashMap<>();
    private static final Map<String, Connection> connectionMap = new ConcurrentHashMap<>();

    private SQLConnector() {
    }

    /**
     * 初始化的时连接数据库
     * @param sqlClass 驱动类
     * @param url 地址
     * @param user 账户
     * @param password 密码
     * @throws ClassNotFoundException 驱动类无法找到
     */
    public static void setupConnect(@Nonnull String sqlClass,
                                     @Nonnull String url,
                                     @Nullable String user,
                                     @Nullable String password) throws ClassNotFoundException {
        // 加载驱动类
        if (!loadedClass.contains(sqlClass)) {
            Class.forName(sqlClass);
            loadedClass.add(sqlClass);
        }
        // 存储连接密码
        connectInfoMap.put(url, new ConnectInfo(url, user, password));
    }

    /**
     * 获取已有连接
     * @param url 地址
     * @return 连接
     */
    public static Connection getConnect(@Nonnull String url) {
        // 获取连接
        Connection connection = connectionMap.get(url);
        try {
            // 判断是否已连接
            if (connection != null && !connection.isClosed()) return connection;
            ConnectInfo connectInfo = connectInfoMap.get(url);
            if (connectInfo != null) {
                // 重新连接
                connection = connect(connectInfo);
            } else {
                // 尝试建立连接
                connection = connect(new ConnectInfo(url));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * 获取连接并缓存
     * @param connectInfo 连接所需信息
     * @return 连接
     * @throws SQLException 数据库连接错误
     */
    public static Connection connect(@Nonnull ConnectInfo connectInfo) throws SQLException {
        // 连接并存储
        Connection connection = DriverManager.getConnection(
                connectInfo.url,
                connectInfo.user,
                connectInfo.password);
        connectionMap.put(connectInfo.url, connection);

        return connection;
    }

    /**
     * 关闭连接
     * @param url 连接地址
     */
    public static void close(@Nonnull String url) {
        Connection connection = connectionMap.get(url);
        if (connection == null) return;
        try {
            if (!connection.isClosed()) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connectionMap.remove(url);
    }

    /**
     * 关闭所有
     */
    public static void closeAll() {
        for (Connection connection : connectionMap.values()) {
            try {
                if (connection == null || connection.isClosed()) continue;
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static class ConnectInfo {

        public ConnectInfo(String url, String user, String password) {
            this.url = url;
            this.user = user;
            this.password = password;
        }

        public ConnectInfo(String url) {
            this.url = url;
            this.user = null;
            this.password = null;
        }

        public final String url;
        public final String user;
        public final String password;
    }

}
