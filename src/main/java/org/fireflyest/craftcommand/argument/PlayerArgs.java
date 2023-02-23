package org.fireflyest.craftcommand.argument;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.command.CommandSender;

public class PlayerArgs implements Argument {

    @Override
    @Nullable
    public List<String> tab(@Nonnull CommandSender sender, @Nonnull String arg) {
        return null;
    }
    
}
