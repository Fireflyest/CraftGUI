package org.fireflyest.craftgui.api;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * 页面，用target作为标签，每个标签下是一个ViewPage链表
 */
public interface ViewPage {

    /**
     * 获取物品按钮，包括固定按钮和动态按钮
     * @return 动态按钮
     */
    @NotNull
    Map<Integer, ItemStack> getItemMap();

    /**
     * 获取固定物品按钮，展示速度较动态按钮快
     * @return 固定按钮
     */
    @NotNull
    Map<Integer, ItemStack> getButtonMap();

    /**
     * 获取页面容器，用于玩家打开
     * @return 页面容器
     */
    @NotNull
    Inventory getInventory();

    /**
     * 获取该页面的标签
     * @return 页面标签
     */
    @Nullable
    String getTarget();

    /**
     * 获取该页面的页码
     * @return 页码
     */
    int getPage();

    /**
     * 获取页面链表的下一个对象
     * @return 下一页
     */
    @Nullable
    ViewPage getNext();

    /**
     * 获取页面链表的上一个对象
     * @return 上一页
     */
    @Nullable
    ViewPage getPre();

    /**
     * 设置下一页
     * @param next 下一页
     */
    void setNext(@Nullable ViewPage next);

    /**
     * 设置上一页
     * @param pre 上一页
     */
    void setPre(@Nullable ViewPage pre);

    /**
     * 刷新
     */
    void refreshPage();

}
