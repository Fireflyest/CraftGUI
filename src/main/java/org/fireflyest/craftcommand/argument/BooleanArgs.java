package org.fireflyest.craftcommand.argument;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.command.CommandSender;

public class BooleanArgs implements Argument {

    @Override
    public List<String> tab(@Nonnull CommandSender sender, @Nonnull String arg) {
        List<String> argList = new ArrayList<>();
        if (arg.startsWith("true")) {
            argList.add("true");
        }
        if (arg.startsWith("false")) {
            argList.add("false");
        }
        return argList;
    }
    
}
