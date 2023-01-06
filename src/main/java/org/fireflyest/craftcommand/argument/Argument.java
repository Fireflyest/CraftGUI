package org.fireflyest.craftcommand.argument;

import java.util.List;

public interface Argument {
    /**
     * tab提示
     * @return 提示
     */
    List<String> tab(String arg);
}
