package org.fireflyest.craftdatabase.builder;

/**
 * @author Fireflyest
 * @since 2022/8/14
 */
public abstract class SQLLimit {

    private final StringBuilder limitBuilder = new StringBuilder();
    private final StringBuilder outsetBuilder;
    private final StringBuilder whereBuilder;
    private final Limit limit;

    public SQLLimit(StringBuilder outsetBuilder, StringBuilder whereBuilder) {
        this.outsetBuilder = outsetBuilder;
        this.whereBuilder = whereBuilder;
        this.limit = new Limit();
    }

    /**
     * @return 限量语句
     */
    public Limit unlimited(){
        return limit;
    }

    /**
     * LIMIT 1
     * @return 限量语句
     */
    public Limit once(){
        limitBuilder.append(" LIMIT ").append(1);
        return limit;
    }

    /**
     * LIMIT {num}
     * @return 限量语句
     */
    public Limit limit(int num){
        limitBuilder.append(" LIMIT ").append(num);
        return limit;
    }

    /**
     * LIMIT {start},{num}
     * @return 限量语句
     */
    public Limit limit(int start, int num){
        limitBuilder.append("\nLIMIT ")
                .append(start)
                .append(",")
                .append(num);
        return limit;
    }

    public class Limit implements SQLBuildable{

        @Override
        public String build() {
            return outsetBuilder.toString() + whereBuilder.toString() + limitBuilder + ";";
        }

    }

}
