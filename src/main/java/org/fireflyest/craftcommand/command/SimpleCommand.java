package org.fireflyest.craftcommand.command;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public abstract class SimpleCommand  extends AbstractCommand implements CommandExecutor, TabCompleter {
    
    @Nullable
    @Override
    public List<String> onTabComplete(
            @Nonnull CommandSender sender, 
            @Nonnull Command command, 
            @Nonnull String alias, 
            @Nonnull String[] args) {
        switch (args.length) {
            case 1:
                return this.firstArgumentTab(sender, args[0]);
            case 2:
                return this.secondArgumentTab(sender, args[1]);     
            case 3:
                return this.thirdArgumentTab(sender, args[2]);     
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
        // 执行指令
        switch (args.length) {
            case 0:
                return execute(sender);
            case 1:
                return execute(sender, args[0]);
            case 2:
                return execute(sender, args[0], args[1]);
            case 3:
                return execute(sender, args[0], args[1], args[2]);
            default:
                return execute(sender, args);
        }
    }

}
