package org.fireflyest.craftcommand.argument;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.command.CommandSender;
import org.bukkit.util.NumberConversions;

public class NumberArgs implements Argument {

    @Override
    public List<String> tab(@Nonnull CommandSender sender, @Nonnull String arg) {
        List<String> argList = new ArrayList<>();
        int a = NumberConversions.toInt(arg);
        argList.add(String.valueOf(a++));
        argList.add(String.valueOf(a++));
        argList.add(String.valueOf(a++));
        argList.add(String.valueOf(a++));
        argList.add(String.valueOf(a++));
        argList.add(String.valueOf(a++));
        argList.add(String.valueOf(a++));
        argList.add(String.valueOf(a++));
        argList.add(String.valueOf(a++));
        argList.add(String.valueOf(a));
        return argList;
    }
    
}
