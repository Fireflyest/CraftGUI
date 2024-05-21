package org.fireflyest.craftcommand.argument;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.command.CommandSender;

public class StringArgs implements Argument {

    private String[] strings;

    public StringArgs(String... strings) {
        this.strings = strings;
    }

    @Override
    public List<String> tab(@Nonnull CommandSender sender, @Nonnull String arg) {
        List<String> ret = new ArrayList<>();
        for (String string : Arrays.asList(strings)) {
            if (string.startsWith(arg)) {
                ret.add(string);
            }
        }
        return ret;
    }
    
}
