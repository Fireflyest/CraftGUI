package org.fireflyest.craftcommand.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public abstract class ComplexCommand  extends AbstractCommand implements CommandExecutor, TabCompleter {
    
    protected final Map<String, SubCommand> subCommands = new HashMap<>();

    /**
     * 添加子指令
     * @param name 指令名称
     * @param subCommand 子指令
     */
    public void addSubCommand(@Nonnull String name, @Nonnull SubCommand subCommand) {
        this.subCommands.put(name, subCommand);
    }

    @Nullable
    @Override
    public List<String> onTabComplete(
            @Nonnull CommandSender sender, 
            @Nonnull Command command, 
            @Nonnull String alias, 
            @Nonnull String[] args) {
        SubCommand subCommand;
        switch (args.length) {
            case 1:
                return this.subCommandTab(args[0]);
            case 2:
                subCommand = subCommands.get(args[0]);
                if (subCommand == null) {
                    break;
                }
                return subCommand.firstArgumentTab(sender, args[1]);
            case 3:
                subCommand = subCommands.get(args[0]);
                if (subCommand == null) {
                    break;
                }
                return subCommand.secondArgumentTab(sender, args[2]);
            case 4:
                subCommand = subCommands.get(args[0]);
                if (subCommand == null) {
                    break;
                }
                return subCommand.thirdArgumentTab(sender, args[3]);
            default:
                break;
        }
        return Collections.emptyList();
    }

    @Override
    public boolean onCommand(
            @Nonnull CommandSender sender, 
            @Nonnull Command command, 
            @Nonnull String label, 
            @Nonnull String[] args) {
        if (args.length == 0) {
            // 无参
            return execute(sender);
        } else if (subCommands.containsKey(args[0])) {
            // 子指令执行
            SubCommand subCommand = subCommands.get(args[0]);
            switch (args.length) {
                case 1:
                    return subCommand.execute(sender);
                case 2:
                    return subCommand.execute(sender, args[1]);
                case 3:
                    return subCommand.execute(sender, args[1], args[2]);
                case 4:
                    return subCommand.execute(sender, args[1], args[2], args[3]);
                default:
                    // 注意这里的处理应该和夫指令不同，参数不是从第一个开始取
                    return subCommand.execute(sender, args);
            }
        }
        return false;
    }

    /**
     * 子指令提示
     * @param arg 参数
     * @return 提示列表
     */
    private List<String> subCommandTab(String arg) {
        List<String> argList = new ArrayList<>();
        for (String string : subCommands.keySet()) {
            if (string.startsWith(arg)) {
                argList.add(string);
            }
        }
        return argList;
    }

}
