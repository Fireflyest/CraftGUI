package org.fireflyest.craftgui.item;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

/**
 * data of view item
 *
 * @author Fireflyest
 * @since 2022/7/30
 */
public abstract class ViewItem {

    // nbt数据键，存储的值是String类型
    public static final String NBT_VALUE_KEY = "craft-gui-value";
    // nbt行为键，存储int类型值，让插件开发者在监听点击事件的时候判断按钮是用来干嘛
    public static final String NBT_ACTION_KEY = "craft-gui-action";

    // 无
    public static final int ACTION_NONE = 0;
    // 返回
    public static final int ACTION_BACK = 1;
    // 关闭
    public static final int ACTION_CLOSE = 2;
    // 执行指令，值一般是要执行的指令
    public static final int ACTION_PLAYER_COMMAND = 3;
    // 控制台指令
    public static final int ACTION_CONSOLE_COMMAND = 4;
    // 翻页，值一般是pre或next
    public static final int ACTION_PAGE = 5;
    // 界面编辑，不需要bukkit的事件监听处理，由Page自己处理
    public static final int ACTION_EDIT = 6;

    protected int action = ACTION_NONE;
    protected String value = "";

    public ViewItem(){
    }

    public ViewItem action(int action){
        this.action = action;
        return this;
    }

    public ViewItem value(String value){
        this.value = value;
        return this;
    }

    public abstract ItemStack build();

}
