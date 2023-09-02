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

    /**
     * 关闭，由导航直接处理
     * @return this
     */
    public ButtonItemBuilder actionClose() {
        update = true;
        nbt.put(ButtonAction.NBT_ACTION_KEY, ButtonAction.ACTION_CLOSE);
        return this;
    }

    /**
     * 向后翻页，由导航直接处理
     * @return this
     */
    public ButtonItemBuilder actionPageNext() {
        update = true;
        nbt.put(ButtonAction.NBT_ACTION_KEY, ButtonAction.ACTION_PAGE_NEXT);
        return this;
    }

    /**
     * 向前翻页，由导航直接处理
     * @return this
     */
    public ButtonItemBuilder actionPagePre() {
        update = true;
        nbt.put(ButtonAction.NBT_ACTION_KEY, ButtonAction.ACTION_PAGE_PRE);
        return this;
    }

    /**
     * 跳转页面，由导航直接处理
     * @param num 页码
     * @return this
     */
    public ButtonItemBuilder actionPageJump(int num) {
        update = true;
        nbt.put(ButtonAction.NBT_ACTION_KEY, ButtonAction.ACTION_PAGE_JUMP);
        nbt.put(ButtonAction.NBT_VALUE_KEY, num);
        return this;
    }

    /**
     * 返回上个界面
     * @return this
     */
    public ButtonItemBuilder actionBack() {
        update = true;
        nbt.put(ButtonAction.NBT_ACTION_KEY, ButtonAction.ACTION_BACK);
        return this;
    }

    /**
     * 修改页面内容，由页面处理
     * @return this
     */
    public ButtonItemBuilder actionEdit() {
        update = true;
        nbt.put(ButtonAction.NBT_ACTION_KEY, ButtonAction.ACTION_EDIT);
        return this;
    }

    /**
     * 打开页面，由导航直接处理
     * @param page 页面格式为viewName.pageTarget
     * @return this
     */
    public ButtonItemBuilder actionOpenPage(String page) {
        update = true;
        nbt.put(ButtonAction.NBT_ACTION_KEY, ButtonAction.ACTION_PAGE_OPEN);
        nbt.put(ButtonAction.NBT_VALUE_KEY, page);
        return this;
    }

    /**
     * 玩家指令，由导航直接处理
     * @param command 指令
     * @return this
     */
    public ButtonItemBuilder actionPlayerCommand(String command) {
        update = true;
        nbt.put(ButtonAction.NBT_ACTION_KEY, ButtonAction.ACTION_PLAYER_COMMAND_SEND);
        nbt.put(ButtonAction.NBT_VALUE_KEY, command);
        return this;
    }

    /**
     * 控制台指令，由导航直接处理
     * @param command 指令
     * @return this
     */
    public ButtonItemBuilder actionConsoleCommand(String command) {
        update = true;
        nbt.put(ButtonAction.NBT_ACTION_KEY, ButtonAction.ACTION_CONSOLE_COMMAND_SEND);
        nbt.put(ButtonAction.NBT_VALUE_KEY, command);
        return this;
    }

    /**
     * Shift点击指令
     * @param command 指令
     * @param shiftCommand 指令
     * @return this
     */
    public ButtonItemBuilder actionShiftCommand(String command, String shiftCommand) {
        update = true;
        nbt.put(ButtonAction.NBT_ACTION_KEY, ButtonAction.ACTION_SHIFT_COMMAND_SEND);
        nbt.put(ButtonAction.NBT_VALUE_KEY, command + ";" + shiftCommand);
        return this;
    }

    /**
     * 插件自定义行为，给插件处理
     * @param value 行为值
     * @return this
     */
    public ButtonItemBuilder actionPlugin(String value) {
        update = true;
        nbt.put(ButtonAction.NBT_ACTION_KEY, ButtonAction.ACTION_PLUGIN);
        nbt.put(ButtonAction.NBT_VALUE_KEY, value);
        return this;
    }

    /**
     * 按钮行为
     * @param action 行为
     * @param value 值
     * @return this
     */
    public ButtonItemBuilder action(int action, String value) {
        update = true;
        nbt.put(ButtonAction.NBT_ACTION_KEY, action);
        nbt.put(ButtonAction.NBT_VALUE_KEY, value);
        return this;
    }

}
