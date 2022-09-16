package org.fireflyest.craftgui.core;

import org.fireflyest.CraftGUI;
import org.fireflyest.craftgui.api.View;
import org.fireflyest.craftgui.api.ViewGuide;
import org.fireflyest.craftgui.api.ViewPage;
import org.fireflyest.craftgui.protocol.ViewProtocol;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * 导航实现类，切勿在主线程之外的线程打开容器
 * @author Fireflyest
 * 2022/2/15 12:04
 */
public class ViewGuideImpl implements ViewGuide {

    // 所有界面
    public final Map<String, View<? extends ViewPage>> viewMap = new HashMap<>();

    // 玩家正在浏览的页面
    public final Map<String, ViewPage> viewPageUsing = new HashMap<>();

    // 玩家正在浏览的界面名称
    public final Map<String, String> viewUsing = new HashMap<>();

    // 记录玩家浏览过的界面，方便返回
    public final Map<String, Stack<ViewPage>> viewUsd = new HashMap<>();

    // 跳转页面，玩家会先打开页面，再关闭原有页面，为了防止取消使用记录，这里记录重定向
    public final Set<String> viewRedirect = new HashSet<>();
    public static final boolean DEBUG = true;

    public ViewGuideImpl() {
    }

    @Override
    public void addView(@Nonnull String viewName, @Nonnull View<? extends ViewPage> view) {
        viewMap.put(viewName, view);
    }

    @Override
    public void closeView(@Nonnull String playerName) {
        if (viewRedirect.contains(playerName)){
            if (DEBUG) CraftGUI.getPlugin().getLogger().info(String.format("%s redirect", playerName));
            viewRedirect.remove(playerName);
        }else {
            if (DEBUG) CraftGUI.getPlugin().getLogger().info(String.format("%s close", playerName));
            viewUsing.remove(playerName);
            viewPageUsing.remove(playerName);
        }
    }

    @Override
    public ViewPage getUsingPage(@Nonnull String playerName) {
        return viewPageUsing.get(playerName);
    }

    @Override
    public String getUsingView(@Nonnull String playerName) {
        return viewUsing.get(playerName);
    }

    @Override
    public void nextPage(@Nonnull Player player) {
        if (DEBUG) CraftGUI.getPlugin().getLogger().info(String.format("%s next", player.getName()));
        String playerName = player.getName();
        if (this.unUsed(playerName)) return;
        ViewPage next, page = this.getUsingPage(playerName);
        if (page != null) {
            next = page.getNext();
            if (next == null) return;
            viewRedirect.add(playerName);
            viewPageUsing.put(playerName, next);
            player.openInventory(next.getInventory());
        }
    }

    @Override
    public void prePage(@Nonnull Player player) {
        if (DEBUG) CraftGUI.getPlugin().getLogger().info(String.format("%s pre", player.getName()));
        String playerName = player.getName();
        if (this.unUsed(playerName)) return;
        ViewPage pre, page = this.getUsingPage(playerName);
        if (page != null) {
            pre = page.getPre();
            if (pre == null) return;
            viewRedirect.add(playerName);
            viewPageUsing.put(playerName, pre);
            player.openInventory(pre.getInventory());
        }
    }

    @Override
    public void back(@Nonnull Player player) {
        String playerName = player.getName();
        if (this.unUsed(playerName)) return;
        // 上一页是否存在
        Stack<ViewPage> backStack = viewUsd.getOrDefault(playerName, new Stack<>());
        if (backStack.empty()) {
            player.closeInventory();
            return;
        }
        // 取出
        ViewPage page = backStack.pop();
        if (page == null) {
            CraftGUI.getPlugin().getLogger().warning("The page player last used does not exist.");
            return;
        }
        // 设置玩家正在浏览的视图
        viewRedirect.add(playerName);
        viewPageUsing.put(playerName, page);
        // 打开容器
        if (DEBUG) CraftGUI.getPlugin().getLogger().info(String.format("%s back to %s", player.getName(), page.getTarget()));
        player.openInventory(page.getInventory());
    }

    @Override
    public void jump(@Nonnull Player player, int page) {
        String playerName = player.getName();
        if (this.unUsed(playerName)) return;
        ViewPage viewPage = this.getUsingPage(playerName), targetPage = viewPage;
        if (viewPage == null) return;
        // 找到目标页面
        int delta = page - viewPage.getPage();
        if (delta > 0){
            for (int i = 0; i < delta; i++) {
                ViewPage tempPage = targetPage.getNext();
                if (tempPage == null) break;
                targetPage = tempPage;
            }
        }else {
            for (int i = 0; i > delta; i--) {
                ViewPage tempPage = targetPage.getPre();
                if (tempPage == null) break;
                targetPage = tempPage;
            }
        }
        // 打开目标页面
        viewRedirect.add(playerName);
        viewPageUsing.put(playerName, targetPage);
        if (DEBUG) CraftGUI.getPlugin().getLogger().info(String.format("%s jump", playerName));
        player.openInventory(targetPage.getInventory());
    }

    @Override
    public void openView(@Nonnull Player player, @Nonnull String viewName, String target) {
        String playerName = player.getName();
        // 记录玩家返回界面
        if (!viewUsd.containsKey(playerName)) viewUsd.put(playerName, new Stack<>());
        ViewPage usingPage = null;
        if (this.unUsed(playerName)){
          viewUsd.get(playerName).clear();
        } else {
            // 转存可以返回
            usingPage = this.getUsingPage(playerName);
            if (DEBUG) CraftGUI.getPlugin().getLogger().info(String.format("%s store back", playerName));
        }
        // 添加返回
        if (usingPage != null) {
            viewRedirect.add(playerName);
            viewUsd.get(playerName).push(usingPage);
        }
        // 判断视图是否存在
        if(! viewMap.containsKey(viewName)){
            CraftGUI.getPlugin().getLogger().warning(String.format("View '%s' does not exist.", viewName));
            return;
        }
        // 获取视图的首页
        ViewPage page = viewMap.get(viewName).getFirstPage(target);
        // 首页是否存在
        if (page == null) {
            CraftGUI.getPlugin().getLogger().warning(String.format("The first page of the '%s' does not exist.", viewName));
            return;
        }
        // 设置玩家正在浏览的视图
        viewUsing.put(playerName, viewName);
        viewPageUsing.put(playerName, page);
        // 打开容器
        if (DEBUG) CraftGUI.getPlugin().getLogger().info(String.format("%s open", playerName));
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
            if (DEBUG) CraftGUI.getPlugin().getLogger().info(String.format("%s refresh", playerName));
            ViewProtocol.sendItemsPacketAsynchronously(playerName);
        }
    }

    @Override
    public Set<String> getViewers() {
        return viewPageUsing.keySet();
    }

    @Override
    public boolean unUsed(@Nonnull String playerName) {
        return !viewPageUsing.containsKey(playerName);
    }
}
