package org.fireflyest.craftcommand.argument;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;

public class MaterialArgs implements Argument {

    @Override
    public List<String> tab(@Nonnull CommandSender sender, @Nonnull String arg) {
        List<String> argList = new ArrayList<>();
        this.putMaterial(argList, arg);
        return argList;
    }
    
    /**
     * 材料提示
     * @param argList 提示列表
     * @param arg 参数
     */
    private void putMaterial(List<String> argList, String arg) {
        for (Material material : Material.values()) {
            if (argList.size() > 20) {
                break;
            }
            String name = material.name().toLowerCase();
            if (name.startsWith(arg)) {
                argList.add(name);
            }
        }
    }

}
