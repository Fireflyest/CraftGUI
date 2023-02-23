package org.fireflyest.craftcommand.argument;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.command.CommandSender;

public class EmailArgs implements Argument {

    @Override
    public List<String> tab(@Nonnull CommandSender sender, @Nonnull String arg) {
        List<String> argList = new ArrayList<>();
        if (!arg.contains("@")) {
            argList.add(arg + "@qq.com");
            argList.add(arg + "@163.com");
            argList.add(arg + "@outlook.com");
            argList.add(arg + "@gmail.com");
        }
        return argList;
    }
    
}
