package org.fireflyest.craftgui.api;

import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * 视图导航
 */
public interface ViewGuide {

    /**
     * 添加一个视图到索引
     * @param viewName 视图名称
     * @param view 视图对象
     */
    void addView(@Nonnull String viewName, @Nonnull View<? extends ViewPage> view);

    /**
     * 关闭玩家正在浏览的界面
     * @param playerName 玩家名称
     */
    void closeView(@Nonnull String playerName);

    /**
     * 让玩家打开界面的首页
     * @param player 玩家
     * @param viewName 视图名称
     * @param target 页面标签
     */
    void openView(@Nonnull Player player, @Nonnull String viewName, @Nullable String target);

    /**
     * 获取某个玩家正在浏览的页面
     * @param playerName 玩家名称
     * @return 页面
     */
    @Nullable
    ViewPage getUsingPage(@Nonnull String playerName);

    /**
     * 获取某个玩家正在浏览的界面
     * @param playerName 玩家名称
     * @return 界面
     */
    @Nullable
    String getUsingView(@Nonnull String playerName);

    /**
     * 玩家切换到下一页
     * @param player 玩家
     */
    void nextPage(@Nonnull Player player);

    /**
     * 玩家切换到上一页
     * @param player 玩家
     */
    void prePage(@Nonnull Player player);

    /**
     * 返回到上一个界面，在打开不同的View时记录
     * @param player 玩家
     */
    void back(@Nonnull Player player);

    /**
     * 跳转页面
     * @param player 玩家
     * @param page 到达的页面
     */
    void jump(@Nonnull Player player, int page);

    /**
     * 刷新玩家的页面，可能异步调用
     * @param playerNames 玩家名称
     */
    void refreshPage(@Nonnull String... playerNames);

    /**
     * 刷新指定的界面，所有正在浏览的玩家都会收到刷新
     * @param viewName 界面名称
     * @param target 页面
     */
    void refreshPages(@Nonnull String viewName, @Nonnull String target);

    /**
     * 更新玩家浏览界面中的某个按钮
     * @param player 玩家
     * @param slot 插槽
     * @param buttonItem 按钮物品
     */
    void updateButton(@Nonnull Player player, int slot, @Nonnull ItemStack buttonItem);

    /**
     * 获取所有正在浏览的玩家
     * @return 玩家集
     */
    Set<String> getViewers();

    /**
     * 获取玩家浏览状态
     * @param playerName 玩家名称
     * @return 是否浏览者
     */
    boolean unUsed(@Nonnull String playerName);

}
