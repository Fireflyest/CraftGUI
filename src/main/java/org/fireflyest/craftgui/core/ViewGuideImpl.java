package org.fireflyest.craftgui.core;

import org.fireflyest.craftgui.CraftGUI;
import org.fireflyest.craftgui.api.View;
import org.fireflyest.craftgui.api.ViewGuide;
import org.fireflyest.craftgui.api.ViewPage;
import org.fireflyest.craftgui.protocol.ViewProtocol;
import org.fireflyest.craftgui.view.ErrorView;
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

    public static final ErrorView errorView = new ErrorView();

    public ViewGuideImpl() {
        // 错误界面
        viewMap.put(CraftGUI.ERROR_VIEW, errorView);
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
        return viewUsing.getOrDefault(playerName, errorView.getFirstPage(ErrorView.NOT_FOUND));
    }

    @Override
    public void nextPage(@NotNull Player player) {
        String playerName = player.getName();
        ViewPage page = this.getUsingPage(playerName).getNext();
        if (page != null) {
            player.closeInventory();
            viewUsing.put(playerName, page);
            player.openInventory(page.getInventory());
        }
    }

    @Override
    public void prePage(@NotNull Player player) {
        String playerName = player.getName();
        ViewPage page = this.getUsingPage(playerName).getPre();
        if (page != null) {
            player.closeInventory();
            viewUsing.put(playerName, page);
            player.openInventory(page.getInventory());
        }
    }

    @Override
    public void openView(@NotNull Player player, @NotNull String viewName, String target) {
        // 关闭正在浏览的界面
        player.closeInventory();
        String playerName = player.getName();
        // 判断视图是否存在
        if(! viewMap.containsKey(viewName)){
            Bukkit.getLogger().warning(String.format("[Gui]View '%s' does not exist.", viewName));
            return;
        }
        // 获取视图
        View<? extends ViewPage> view = viewMap.get(viewName);
        // 获取视图的首页
        ViewPage page = view.getFirstPage(target);
        // 首页是否存在
        if (page != null) {
            // 设置玩家正在浏览的视图
            viewUsing.put(playerName, page);
        }else {
            Bukkit.getLogger().warning(String.format("[Gui]The first page of the '%s' does not exist.", viewName));
        }
        // 打开容器
        player.openInventory(viewUsing.get(playerName).getInventory());
    }

    @Override
    public void refreshPage(String... playerNames) {
        for (String playerName : playerNames) {
            // 获取刷新页面
            ViewPage page = this.getUsingPage(playerName);

            if (page != null) {
                // 刷新物品
                page.refreshPage();
                // 发包
                ViewProtocol.sendItemsPacketAsynchronously(playerName);
            }
        }
    }

    @Override
    public Set<String> getViewers() {
        return viewUsing.keySet();
    }

    @Override
    public boolean isViewer(String playerName) {
        return viewUsing.containsKey(playerName) && viewUsing.get(playerName) != errorView.getFirstPage(ErrorView.NOT_FOUND);
    }
}
