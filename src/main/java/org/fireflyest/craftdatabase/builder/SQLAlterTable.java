package org.fireflyest.craftdatabase.builder;

import javax.annotation.Nonnull;

/**
 * 修改表
 * @author Fireflyest
 * @since 2022/8/16
 */
public class SQLAlterTable {

    private final StringBuilder alterTableBuilder = new StringBuilder();

    private final AlterTable alterTable;
    /**
     * ALTER TABLE `{table}`
     * @param table 表名
     */
    public SQLAlterTable(@Nonnull String table) {
        this.alterTable = new AlterTable();

        alterTableBuilder.append("ALTER TABLE `").append(table).append("`");
    }

    /**
     * 添加列
     * ADD COLUMN `{column}` {type}
     * @param column 列名
     * @param type 数据类型
     * @return 修改指令
     */
    public AlterTable add(@Nonnull String column, @Nonnull String type){
        alterTableBuilder.append("\nADD COLUMN `")
                .append(column)
                .append("` ")
                .append(type);
        return alterTable;
    }

    /**
     * 删除列
     * DROP COLUMN `{column}`
     * @param column 列名
     * @return 修改指令
     */
    public AlterTable drop(@Nonnull String column){
        alterTableBuilder.append("\nDROP COLUMN `")
                .append(column)
                .append("` ");
        return alterTable;
    }

    public class AlterTable implements SQLBuildable{

        public AlterTable() {
        }

        @Override
        public String build() {
            return alterTableBuilder + ";";
        }

    }

}
