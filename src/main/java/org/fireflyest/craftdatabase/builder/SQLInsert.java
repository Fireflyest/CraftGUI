package org.fireflyest.craftdatabase.builder;

import javax.annotation.Nonnull;

/**
 * 插入语句
 * @author Fireflyest
 * @since 2022/8/14
 */
public class SQLInsert {

    private final StringBuilder insertBuilder = new StringBuilder();

    private final Insert insert;

    private boolean firstValue = true;
    private int columnsNum = 0;
    private int valuesNum = 0;

    /**
     * INSERT INTO `{table}`
     * @param table 表名
     */
    public SQLInsert(@Nonnull String table) {
        this.insert = new Insert();

        insertBuilder.append("INSERT INTO `").append(table).append("`");
    }

    /**
     * (`{column}`,`{column}`...})
     * @param columns 键
     * @return 插入语句
     */
    public Insert columns(@Nonnull String... columns){
        columnsNum = columns.length;

        insertBuilder.append(" (`");
        int i = 0;
        for (String column : columns) {
            if (i++ > 0) insertBuilder.append("`,`");
            insertBuilder.append(column);
        }
        insertBuilder.append("`)");
        return insert;
    }

    /**
     * todo insert 配合 select
     */
    public class Insert implements SQLBuildable {

        public Insert() {
        }

        /**
         *  VALUES ('{string}','{string}'...)
         * @param strings 值
         * @return 插入语句
         */
        public Insert values(@Nonnull String... strings){
            for (String string : strings) {
                if (valuesNum++ == columnsNum) break;
                if (firstValue) {
                    insertBuilder.append("\nVALUES ('");
                    firstValue = false;
                } else {
                    insertBuilder.append(",'");
                }
                insertBuilder
                        .append(string.replace("'", "''"))
                        .append("'");
            }
            return this;
        }

        /**
         *  VALUES ({number},{number}...)
         * @param numbers 值
         * @return 插入语句
         */
        public Insert values(@Nonnull Number... numbers){
            for (Number number : numbers) {
                if (valuesNum++ == columnsNum) break;
                if (firstValue) {
                    insertBuilder.append("\nVALUES (");
                    firstValue = false;
                } else {
                    insertBuilder.append(",");
                }
                insertBuilder.append(number);
            }
            return this;
        }

        /**
         *  VALUES ({boolean},{boolean}...)
         * @param booleans 值
         * @return 插入语句
         */
        public Insert values(boolean... booleans){
            for (boolean bool : booleans) {
                if (valuesNum++ == columnsNum) break;
                if (firstValue) {
                    insertBuilder.append("\nVALUES (");
                    firstValue = false;
                } else {
                    insertBuilder.append(",");
                }
                insertBuilder.append(bool ? 1 : 0);
            }
            return this;
        }

        @Override
        public String build() {
            return insertBuilder + ");";
        }
    }

}
