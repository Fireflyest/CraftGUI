package org.fireflyest.craftgui.api;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * 视图导航
 */
public interface ViewGuide {

    /**
     * 添加一个视图到索引
     * @param viewName 视图名称
     * @param view 视图对象
     */
    void addView(@NotNull String viewName, @NotNull View<? extends ViewPage> view);

    /**
     * 关闭玩家正在浏览的界面
     * @param playerName 玩家名称
     */
    void closeView(@NotNull String playerName);

    /**
     * 让玩家打开界面的首页
     * @param player 玩家
     * @param viewName 视图名称
     * @param target 页面标签
     */
    void openView(@NotNull Player player, @NotNull String viewName, @Nullable String target);

    /**
     * 获取某个玩家正在浏览的界面
     * @param playerName 玩家名称
     * @return 页面
     */
    ViewPage getUsingPage(@NotNull String playerName);

    /**
     * 玩家切换到下一页
     * @param player 玩家
     */
    void nextPage(@NotNull Player player);

    /**
     * 玩家切换到上一页
     * @param player 玩家
     */
    void prePage(@NotNull Player player);

    /**
     * 返回到上一个界面，通常是不同的View
     * @param player 玩家
     */
    void back(@NotNull Player player);

    /**
     * 跳转页面
     * @param player 玩家
     * @param page 到达的页面
     */
    void jump(@NotNull Player player, int page);

    /**
     * 刷新玩家的页面
     * @param playerNames 玩家名称
     */
    void refreshPage(String... playerNames);

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
    boolean unUsed(@NotNull String playerName);

}
