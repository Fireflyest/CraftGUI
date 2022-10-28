package org.fireflyest.craftcommand.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.util.NumberConversions;
import org.fireflyest.craftcommand.argument.Argument;

public abstract class AbstractCommand {

    protected List<Argument> arguments = Arrays.asList(Argument.EMPTY, Argument.EMPTY, Argument.EMPTY);

    /**
     * 抽象指令
     */
    protected AbstractCommand() {
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
    public abstract boolean execute(@Nonnull CommandSender sender);

    /**
     * 单参指令
     * @param sender 发送者
     * @param arg1 参数1
     * @return 是否正确
     */
    public boolean execute(@Nonnull CommandSender sender, @Nonnull String arg1) {
        return false;
    }

    /**
     * 
     * @param sender 发送者
     * @param arg1 参数1
     * @param arg2 参数2
     * @return 是否正确
     */
    public boolean execute(@Nonnull CommandSender sender, @Nonnull String arg1, @Nonnull String arg2) {
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
    public boolean execute(@Nonnull CommandSender sender, @Nonnull String arg1, @Nonnull String arg2, @Nonnull String arg3) {
        return false;
    }

    /**
     * 
     * @param sender 发送者
     * @param args 参数
     * @return 是否正确
     */
    public boolean execute(@Nonnull CommandSender sender, @Nonnull String[] args) {
        return false;
    }

    /**
     * 第一个参数提示
     * @param sender 发送者
     * @param arg 参数
     * @return 提示列表
     */
    @Nullable
    public List<String> firstArgumentTab(@Nonnull CommandSender sender, @Nonnull String arg) {
        return this.getArgumentTab(0, arg);
    }

    /**
     * 第二个参数提示
     * @param sender 发送者
     * @param arg 参数
     * @return 提示列表
     */
    @Nullable
    public List<String> secondArgumentTab(@Nonnull CommandSender sender, String arg) {
        return this.getArgumentTab(1, arg);
    }

    /**
     * 第三个参数提示
     * @param sender 发送者
     * @param arg 参数
     * @return 提示列表
     */
    @Nullable
    public List<String> thirdArgumentTab(@Nonnull CommandSender sender, String arg) {
        return this.getArgumentTab(2, arg);
    }

    /**
     * 获取参数
     * @param argIndex 参数位置
     * @param arg 参数
     * @return 提示列表
     */
    @Nullable
    private List<String> getArgumentTab(int argIndex, String arg) {
        List<String> argList = new ArrayList<>();
        switch (arguments.get(argIndex)) {
            case PLAYER: //在线玩家
                return null;
            case NUMBER: // 数字
                int a = NumberConversions.toInt(arg);
                for (int i = a; i < 10; i++) {
                    argList.add(String.valueOf(i));
                }
                return argList;
            case OFFLINE_PLAYER: // 所有玩家
                for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
                    if (argList.size() > 20) {
                        break;
                    }
                    String name = offlinePlayer.getName();
                    if (name.startsWith(arg)) {
                        argList.add(name);
                    }
                }
                return argList;
            case BOOLEAN: // 布尔
                if (arg.startsWith("true")) {
                    argList.add("true");
                }
                if (arg.startsWith("false")) {
                    argList.add("false");
                }
                return argList;
            case EMPTY: // 空
            default:
                return argList;
        }
    }

}
