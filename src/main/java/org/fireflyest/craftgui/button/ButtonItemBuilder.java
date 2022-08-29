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

    public ButtonItemBuilder actionPageNext(){
        nbt.put(ButtonAction.NBT_ACTION_KEY, ButtonAction.ACTION_PAGE_NEXT);
        return this;
    }

    public ButtonItemBuilder actionPagePre(){
        nbt.put(ButtonAction.NBT_ACTION_KEY, ButtonAction.ACTION_PAGE_PRE);
        return this;
    }

    public ButtonItemBuilder actionPageJump(int num){
        nbt.put(ButtonAction.NBT_ACTION_KEY, ButtonAction.ACTION_PAGE_JUMP);
        nbt.put(ButtonAction.NBT_VALUE_KEY, num);
        return this;
    }

    public ButtonItemBuilder actionBack(){
        nbt.put(ButtonAction.NBT_ACTION_KEY, ButtonAction.ACTION_BACK);
        return this;
    }

    public ButtonItemBuilder actionEdit(){
        nbt.put(ButtonAction.NBT_ACTION_KEY, ButtonAction.ACTION_EDIT);
        return this;
    }

    public ButtonItemBuilder actionOpen(){
        nbt.put(ButtonAction.NBT_ACTION_KEY, ButtonAction.ACTION_OPEN);
        return this;
    }

    public ButtonItemBuilder actionPlayerCommand(String command){
        nbt.put(ButtonAction.NBT_ACTION_KEY, ButtonAction.ACTION_PLAYER_COMMAND);
        nbt.put(ButtonAction.NBT_VALUE_KEY, command);
        return this;
    }

    public ButtonItemBuilder actionConsoleCommand(String command){
        nbt.put(ButtonAction.NBT_ACTION_KEY, ButtonAction.ACTION_CONSOLE_COMMAND);
        nbt.put(ButtonAction.NBT_VALUE_KEY, command);
        return this;
    }

    public ButtonItemBuilder actionConsoleCommand(int page){
        nbt.put(ButtonAction.NBT_ACTION_KEY, ButtonAction.ACTION_PAGE);
        nbt.put(ButtonAction.NBT_VALUE_KEY, page);
        return this;
    }

    public ButtonItemBuilder actionPlugin(String value){
        nbt.put(ButtonAction.NBT_ACTION_KEY, ButtonAction.ACTION_PLUGIN);
        nbt.put(ButtonAction.NBT_VALUE_KEY, value);
        return this;
    }

}
