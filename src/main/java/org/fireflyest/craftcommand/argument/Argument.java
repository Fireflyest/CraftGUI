package org.fireflyest.craftcommand.argument;

import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.command.CommandSender;

public interface Argument {
    /**
     * tab提示
     * @param sender 指令发送者
     * @param arg 指令
     * @return 提示
     */
    List<String> tab(@Nonnull CommandSender sender, @Nonnull String arg);
}
