package org.fireflyest.craftcommand.argument;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.command.CommandSender;

public class EmptyArgs implements Argument {

    @Override
    public List<String> tab(@Nonnull CommandSender sender, @Nonnull String arg) {
        return new ArrayList<>();
    }
    
}
