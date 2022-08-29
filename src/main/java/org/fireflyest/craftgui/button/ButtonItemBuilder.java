package org.fireflyest.craftgui.button;

import org.bukkit.Material;
import org.fireflyest.craftitem.builder.ItemBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Fireflyest
 * @since 2022/8/29
 */
public class ButtonItemBuilder extends ItemBuilder {

    public ButtonItemBuilder(@Nullable Material material) {
        super(material);
        nbt.put(ButtonAction.NBT_ACTION_KEY, ButtonAction.ACTION_NONE);
        nbt.put(ButtonAction.NBT_VALUE_KEY, "");
    }

    public ButtonItemBuilder(@Nonnull String material) {
        super(material);
        nbt.put(ButtonAction.NBT_ACTION_KEY, ButtonAction.ACTION_NONE);
        nbt.put(ButtonAction.NBT_VALUE_KEY, "");
    }

    public ButtonItemBuilder actionClose(){
        nbt.put(ButtonAction.NBT_ACTION_KEY, ButtonAction.ACTION_CLOSE);
        return this;
    }

}
