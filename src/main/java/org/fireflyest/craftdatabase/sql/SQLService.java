package org.fireflyest.craftdatabase.sql;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Fireflyest
 * @since 2022/8/22
 */
public abstract class SQLService {

    private final String url;

    public SQLService(@Nonnull String url) {
        this.url = url;

        try {
            SQLServiceInitial.init(this);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUrl(){
        return url;
    }

    public void execute(@Nonnull String sql){
        // 获取连接
        Connection connection = SQLConnector.getConnect(url);
        // 执行指令
        try (Statement statement = connection.createStatement()){
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
