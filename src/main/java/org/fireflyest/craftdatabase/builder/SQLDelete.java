package org.fireflyest.craftdatabase.builder;

import javax.annotation.Nonnull;

/**
 * 删除语句
 * @author Fireflyest
 * @since 2022/8/14
 */
public class SQLDelete {

    private final StringBuilder deleteBuilder = new StringBuilder();

    private final Delete delete;

    /**
     * DELETE
     */
    public SQLDelete() {
        this.delete = new Delete(deleteBuilder);

        deleteBuilder.append("DELETE");
    }

    /**
     * FROM `{table}`,`{table}`...
     * @param tables 查找的所有表
     * @return 查找语句
     */
    public Delete from(@Nonnull String... tables){
        int i = 0;
        deleteBuilder.append(" FROM ");
        for (String table : tables) {
            if (i++ > 0) deleteBuilder.append(",");
            deleteBuilder.append("`").append(table).append("`");
        }
        return delete;
    }

    public class Delete extends SQLWhere implements SQLBuildable {

        public Delete(StringBuilder outsetBuilder) {
            super(outsetBuilder);
        }

        @Override
        public String build() {
            return deleteBuilder + ";";
        }

    }
}
