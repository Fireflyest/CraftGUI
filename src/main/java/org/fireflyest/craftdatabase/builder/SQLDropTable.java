package org.fireflyest.craftdatabase.builder;

import javax.annotation.Nonnull;

/**
 * 删除表语句
 * @author Fireflyest
 * @since 2022/8/14
 */
public class SQLDropTable implements SQLBuildable{

    private final StringBuilder dropTableBuilder = new StringBuilder();

    /**
     * DROP TABLE `{table}`
     * @param table 表名
     */
    public SQLDropTable(@Nonnull String table) {
        dropTableBuilder.append("DROP TABLE `").append(table).append("`");
    }

    @Override
    public String build() {
        return dropTableBuilder + ";";
    }
}
