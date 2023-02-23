package org.fireflyest.craftcommand.command;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.command.CommandSender;
import org.fireflyest.craftcommand.argument.Argument;
import org.fireflyest.craftcommand.argument.EmptyArgs;

public abstract class AbstractCommand {

    protected List<Argument> arguments = new ArrayList<>(5);

    /**
     * 抽象指令
     */
    protected AbstractCommand() {
        arguments.add(new EmptyArgs());
        arguments.add(new EmptyArgs());
        arguments.add(new EmptyArgs());
    }

    /**
     * 添加参数
     * @param index 位置0-2
     * @param arg 参数类型
     */
    public void setArgument(int index, @Nonnull Argument arg) {
        if (index < 0) index = 0;
        if (index > 2) index = 2;
        arguments.set(index, arg);
    }

    /**
     * 无参指令
     * @param sender 发送者
     * @return 是否正确
     */
    protected abstract boolean execute(@Nonnull CommandSender sender);

    /**
     * 单参指令
     * @param sender 发送者
     * @param arg1 参数1
     * @return 是否正确
     */
    protected boolean execute(@Nonnull CommandSender sender, @Nonnull String arg1) {
        return false;
    }

    /**
     * 
     * @param sender 发送者
     * @param arg1 参数1
     * @param arg2 参数2
     * @return 是否正确
     */
    protected boolean execute(@Nonnull CommandSender sender, @Nonnull String arg1, @Nonnull String arg2) {
        return false;
    }

    /**
     * 
     * @param sender 发送者
     * @param arg1 参数1
     * @param arg2 参数2
     * @param arg3 参数3
     * @return 是否正确
     */
    protected boolean execute(@Nonnull CommandSender sender, @Nonnull String arg1, @Nonnull String arg2, @Nonnull String arg3) {
        return false;
    }

    /**
     * 
     * @param sender 发送者
     * @param args 参数
     * @return 是否正确
     */
    protected boolean execute(@Nonnull CommandSender sender, @Nonnull String[] args) {
        return false;
    }

    /**
     * 第一个参数提示
     * @param sender 发送者
     * @param arg 参数
     * @return 提示列表
     */
    @Nullable
    protected List<String> firstArgumentTab(@Nonnull CommandSender sender, @Nonnull String arg) {
        return this.getArgumentTab(0, sender, arg);
    }

    /**
     * 第二个参数提示
     * @param sender 发送者
     * @param arg 参数
     * @return 提示列表
     */
    @Nullable
    protected List<String> secondArgumentTab(@Nonnull CommandSender sender, String arg) {
        return this.getArgumentTab(1, sender, arg);
    }

    /**
     * 第三个参数提示
     * @param sender 发送者
     * @param arg 参数
     * @return 提示列表
     */
    @Nullable
    protected List<String> thirdArgumentTab(@Nonnull CommandSender sender, String arg) {
        return this.getArgumentTab(2, sender, arg);
    }

    /**
     * 获取参数
     * @param argIndex 参数位置
     * @param arg 参数
     * @return 提示列表
     */
    @Nullable
    private List<String> getArgumentTab(int argIndex, @Nonnull CommandSender sender, String arg) {
        return arguments.get(argIndex).tab(sender, arg);
    }


}
