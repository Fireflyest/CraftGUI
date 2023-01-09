package org.fireflyest.craftcommand.argument;

import java.util.List;

public interface Argument {
    /**
     * tab提示
     * @param arg 指令
     * @return 提示
     */
    List<String> tab(String arg);
}
