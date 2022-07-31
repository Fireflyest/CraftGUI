package org.fireflyest.craftgui.core;

import org.fireflyest.craftgui.api.View;
import org.fireflyest.craftgui.api.ViewGuide;
import org.fireflyest.craftgui.api.ViewPage;
import org.fireflyest.craftgui.protocol.ViewProtocol;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Fireflyest
 * 2022/2/15 12:04
 */

public class ViewGuideImpl implements ViewGuide {

    // 所有界面
    public static final Map<String, View<? extends ViewPage>> viewMap = new ConcurrentHashMap<>();

    // 玩家正在浏览的页面
    public static final Map<String, ViewPage> viewUsing = new ConcurrentHashMap<>();

    public ViewGuideImpl() {
    }

    @Override
    public void addView(@NotNull String viewName, @NotNull View<? extends ViewPage> view) {
        viewMap.put(viewName, view);
    }

    @Override
    public void closeView(@NotNull String playerName) {
        viewUsing.remove(playerName);
    }

    @Override
    public ViewPage getUsingPage(@NotNull String playerName) {
        return viewUsing.get(playerName);
    }

    @Override
    public void nextPage(@NotNull Player player) {
        String playerName = player.getName();
        if (this.unUsed(playerName)) return;
        ViewPage next = this.getUsingPage(playerName).getNext();
        if (next != null) {
            player.closeInventory();
            viewUsing.put(playerName, next);
            player.openInventory(next.getInventory());
        }
    }

    @Override
    public void prePage(@NotNull Player player) {
        String playerName = player.getName();
        if (this.unUsed(playerName)) return;
        ViewPage pre = this.getUsingPage(playerName).getPre();
        if (pre != null) {
            player.closeInventory();
            viewUsing.put(playerName, pre);
            player.openInventory(pre.getInventory());
        }
    }

    @Override
    public void back(@NotNull Player player) {
        // TODO: 2022/7/31
    }

    @Override
    public void jump(@NotNull Player player, int page) {
        // TODO: 2022/7/31
    }

    @Override
    public void openView(@NotNull Player player, @NotNull String viewName, String target) {
        // 关闭正在浏览的界面
        player.closeInventory();
        String playerName = player.getName();
        // 判断视图是否存在
        if(! viewMap.containsKey(viewName)){
            Bukkit.getLogger().warning(String.format("View '%s' does not exist.", viewName));
            return;
        }
        // 获取视图的首页
        ViewPage page = viewMap.get(viewName).getFirstPage(target);
        // 首页是否存在
        if (page == null) {
            Bukkit.getLogger().warning(String.format("The first page of the '%s' does not exist.", viewName));
            return;
        }
        // 设置玩家正在浏览的视图
        viewUsing.put(playerName, page);
        // 打开容器
        player.openInventory(page.getInventory());
    }

    @Override
    public void refreshPage(String... playerNames) {
        for (String playerName : playerNames) {
            // 获取刷新页面
            ViewPage page = this.getUsingPage(playerName);
            // 是否浏览者
            if (page == null) continue;
            // 刷新物品
            page.refreshPage();
            // 发包
            ViewProtocol.sendItemsPacketAsynchronously(playerName);
        }
    }

    @Override
    public Set<String> getViewers() {
        return viewUsing.keySet();
    }

    @Override
    public boolean unUsed(@NotNull String playerName) {
        return !viewUsing.containsKey(playerName);
    }
}
