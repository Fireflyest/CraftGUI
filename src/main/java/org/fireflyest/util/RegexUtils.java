package org.fireflyest.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

/**
 * 正则表达式工具类
 * @author Fireflyest
 * @since 1.2
 */
public class RegexUtils {
    
    /*
     * . 匹配任意字符
     * 
     * [ ] 字符集合
     * 字符集合的匹配结果是能够与该集合里的任意一个成员相匹配的文本
     * ( ) 子表达式
     * 
     * 取非匹配 ^ 的效果将作用于给定宇符集合里的所有字符或字符区间，而不是仅限于紧跟在 ^ 字符后面的那一个字符或字符区间
     * 
     * \d 数字 [0-9]
     * \D 非数字 [^0-9]
     * \w 字母数字下划线 [a-zA-Z0-9_]
     * \W 非字母数字下划线 [^a-zA-Z0-9_]
     * \s 空白 [\f\n\r\t\v]
     * \S 非空白 [^\f\n\r\t\v]
     * \b 单词边界 \w与\W之间
     * \B 非单词边界 \w与\w之间或\W与\W之间
     * 
     * +后缀 重复匹配至少一个 大括号1,
     * * 后缀 连续出现零次或多次  大括号0,
     * ? 后缀 出现一次或不出现 大括号0,1
     * ? 加在+ *后面成懒惰型
     * 
     * ^ 开头 $结尾
     * 
     * 回溯引用 \1代表模式里第一个子表达式 \2代表第二个以此类推
     * 而\0代表整个表达式
     * 
     * 向前查找以?=开头的表达式 只匹配但不消费
     * 负向前查找?!
     * 向后查找?<=
     * 负向后查找?<!
     * 
     * 回溯引用条件  (?(backreference)true-regex)或(?(backreference)true-regex|false-regex
     * 前后查找条件  (?(...)...)
     */



    public static final Pattern PERCENT_PATTERN = Pattern.compile("%([^%]*)%");
    public static final Pattern BRACE_PATTERN = Pattern.compile("\\$\\{([^{]*)}");
    public static final Pattern FORMAT_PATTERN = Pattern.compile("\\{([^{]*)}");

    public interface VariableContainer {
        String getVar(String key);
    }

    private RegexUtils() {
        
    }

    /**
     * 变量替换
     * @param pattern 正则表达式的编译表示
     * @param text 需要变量替换的文本
     * @param container 存储变量的容器
     * @return 替换后的文本
     */
    public static String varReplace(@Nonnull Pattern pattern, @Nonnull String text, @Nonnull VariableContainer container) {
        // 替换变量
        Matcher varMatcher = pattern.matcher(text);
        StringBuilder stringBuilder = new StringBuilder();
        while (varMatcher.find()) {
            String parameter = varMatcher.group();
            String parameterName = parameter.substring(1, parameter.length() - 1);
            varMatcher.appendReplacement(stringBuilder, container.getVar(parameterName));
        }
        varMatcher.appendTail(stringBuilder);
        return stringBuilder.toString();
    }

}
