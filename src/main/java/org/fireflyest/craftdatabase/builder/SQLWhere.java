package org.fireflyest.craftdatabase.builder;

import javax.annotation.Nonnull;

/**
 * 寻找符合条件的范围
 * @author Fireflyest
 * @since 2022/8/14
 */
public abstract class SQLWhere{

    private final StringBuilder whereBuilder = new StringBuilder();
    private final StringBuilder outsetBuilder;

    private final Where where;

    private boolean anywhere;

    /**
     * WHERE
     * @param outsetBuilder 开端
     */
    public SQLWhere(StringBuilder outsetBuilder) {
        this.outsetBuilder = outsetBuilder;
        this.where = new Where();

        whereBuilder.append("\nWHERE");
    }

    public Where anywhere(){
        this.anywhere = true;
        whereBuilder.delete(0, whereBuilder.length());
        return where;
    }

    /**
     * `{column}`＝{number} ，
     * `{column}`＞{number} ，
     * `{column}`＜{number} ，
     * `{column}`＞＝{number} ，
     * `{column}`＜＝{number} ，
     * `{column}`＜＞{number} ，
     * @param column 键
     * @param symbol 比较符号
     * @param number 数值
     * @return 条件语句
     */
    public Where compare(@Nonnull String column, @Nonnull String symbol, @Nonnull Number number){
        whereBuilder.append(" `")
                .append(column)
                .append("`")
                .append(symbol)
                .append(number);
        return where;
    }

    /**
     * BETWEEN {start} AND {end}
     * @param column 键
     * @param start 开始数值
     * @param end 结束数值
     * @return 条件语句
     */
    public Where between(@Nonnull String column, @Nonnull Number start, @Nonnull Number end){
        whereBuilder.append(" `")
                .append(column)
                .append("` BETWEEN ")
                .append(start)
                .append(" AND ")
                .append(end);
        return where;
    }

    /**
     * `{column}`＝'{value}'
     * @param column 键
     * @param value 值
     * @return 条件语句
     */
    public Where equal(@Nonnull String column, @Nonnull String value){
        whereBuilder.append(" `")
                .append(column)
                .append("`='")
                .append(value.replace("'", "''"))
                .append("'");
        return where;
    }

    /**
     * `{column}`＜＞'{value}'
     * @param column 键
     * @param value 值
     * @return 条件语句
     */
    public Where notEqual(@Nonnull String column, @Nonnull String value){
        whereBuilder.append(" `")
                .append(column)
                .append("`<>'")
                .append(value.replace("'", "''"))
                .append("'");
        return where;
    }

    /**
     * `{column}` IS NULL
     * @param column 键
     * @return 条件语句
     */
    public Where isNull(@Nonnull String column){
        whereBuilder.append(" `")
                .append(column)
                .append("` IS NULL");
        return where;
    }

    /**
     * `{column}` IN ('{var1}','{var2}')
     * 通配符 % _ []
     * @param column 键
     * @param values 值
     * @return 条件语句
     */
    public Where in(@Nonnull String column, @Nonnull String... values){
        whereBuilder.append(" `")
                .append(column)
                .append("` IN ('");
        int i = 0;
        for (String value : values) {
            if (i++ > 0) whereBuilder.append("','");
            whereBuilder.append(value.replace("'", "''"));
        }
        whereBuilder.append("')");
        return where;
    }

    /**
     * `{column}` LIKE '{string}'
     * @param column 键
     * @param string 条件
     * @return 条件语句
     */
    public Where like(@Nonnull String column, @Nonnull String string){
        whereBuilder.append(" `")
                .append(column)
                .append("` LIKE '")
                .append(string.replace("'", "''"))
                .append("'");
        return where;
    }

    /**
     * `{column}` NOT LIKE '{string}'
     * @param column 键
     * @param string 条件
     * @return 条件语句
     */
    public Where notLike(@Nonnull String column, @Nonnull String string){
        whereBuilder.append(" `")
                .append(column)
                .append("` NOT LIKE '")
                .append(string.replace("'", "''"))
                .append("'");
        return where;
    }

    public class Where extends SQLLimit implements SQLBuildable{

        public Where() {
            super(outsetBuilder, whereBuilder);
        }

        /**
         * AND
         * 注意AND优先级高于OR
         * @return 条件语句
         */
        public SQLWhere and(){
            if (!anywhere) whereBuilder.append(" AND");
            return SQLWhere.this;
        }

        /**
         * OR
         * @return 条件语句
         */
        public SQLWhere or(){
            if (!anywhere) whereBuilder.append(" OR");
            return SQLWhere.this;
        }

        @Override
        public String build() {
            return outsetBuilder.toString() + whereBuilder + ";";
        }

    }

}
