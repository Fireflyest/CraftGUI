package org.fireflyest.craftgui.api;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

/**
 * 一个ViewPage相当于一个链表节点
 */
public interface ViewPage {

    /**
     * 获取全部按钮，异步展示
     * @return 动态按钮
     */
    @Nonnull
    Map<Integer, ItemStack> getItemMap();

    /**
     * 获取固定物品按钮，直接展示
     * @return 固定按钮
     */
    @Nonnull
    Map<Integer, ItemStack> getButtonMap();

    /**
     * 点击监听获取用户所点击物品
     * @param slot 格子
     * @return 物品
     */
    @Nullable
    ItemStack getItem(int slot);

    /**
     * 获取页面容器，用于玩家打开
     * @return 页面容器
     */
    @Nonnull
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
     * 刷新，可能异步
     */
    void refreshPage();

    /**
     * 更新容器标题
     * @param title 标题
     */
    void updateTitle(String title);
}
