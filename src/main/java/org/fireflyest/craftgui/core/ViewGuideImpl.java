package org.fireflyest.craftgui.core;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.fireflyest.CraftGUI;
import org.fireflyest.craftgui.api.View;
import org.fireflyest.craftgui.api.ViewGuide;
import org.fireflyest.craftgui.api.ViewPage;

/**
 * 导航实现类，切勿在主线程之外的线程打开容器
 * @author Fireflyest
 */
public class ViewGuideImpl implements ViewGuide {

    private final ProtocolManager protocolManager;

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

    private final Map<String, PacketContainer> packets = new ConcurrentHashMap<>();

    public static final boolean DEBUG = true;

    public static final ItemStack AIR = new ItemStack(Material.AIR);

    /**
     * 导航实现类
     */
    public ViewGuideImpl() {
        // 数据包监听s
        this.protocolManager = ProtocolLibrary.getProtocolManager();
        this.createPacketListener();
    }

    @Override
    public void addView(@Nonnull String viewName, @Nonnull View<? extends ViewPage> view) {
        viewMap.put(viewName, view);
    }

    @Override
    public void closeView(@Nonnull String playerName) {
        if (viewRedirect.contains(playerName)) {
            if (DEBUG) {
                String info = String.format("%s redirect", playerName);
                CraftGUI.getPlugin().getLogger().info(info);
            }
            viewRedirect.remove(playerName);
        } else {
            if (DEBUG) {
                String info = String.format("%s close", playerName);
                CraftGUI.getPlugin().getLogger().info(info);
            }
            viewUsing.remove(playerName);
            viewPageUsing.remove(playerName);
        }
    }

    @Override
    public ViewPage getUsingPage(String playerName) {
        return viewPageUsing.get(playerName);
    }

    @Override
    public String getUsingView(String playerName) {
        return viewUsing.get(playerName);
    }

    @Override
    public void nextPage(@Nonnull Player player) {
        String playerName = player.getName();
        if (DEBUG) {
            String info = String.format("%s next", playerName);
            CraftGUI.getPlugin().getLogger().info(info);
        }
        if (this.unUsed(playerName)) return;
        ViewPage next;
        ViewPage page = this.getUsingPage(playerName);
        if (page != null) {
            next = page.getNext();
            if (next == null)
                return;
            viewRedirect.add(playerName);
            viewPageUsing.put(playerName, next);
            player.openInventory(next.getInventory());
        }
    }

    @Override
    public void prePage(@Nonnull Player player) {
        String playerName = player.getName();
        if (DEBUG) {
            String info = String.format("%s pre", playerName);
            CraftGUI.getPlugin().getLogger().info(info);
        }
        if (this.unUsed(playerName))
            return;
        ViewPage pre;
        ViewPage page = this.getUsingPage(playerName);
        if (page != null) {
            pre = page.getPre();
            if (pre == null)
                return;
            viewRedirect.add(playerName);
            viewPageUsing.put(playerName, pre);
            player.openInventory(pre.getInventory());
        }
    }

    @Override
    public void back(@Nonnull Player player) {
        String playerName = player.getName();
        if (this.unUsed(playerName))
            return;
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
        player.openInventory(page.getInventory());
        if (DEBUG) {
            String info = String.format("%s back to %s", player.getName(), page.getTarget());
            CraftGUI.getPlugin().getLogger().info(info);
        }
    }

    @Override
    public void jump(@Nonnull Player player, int page) {
        String playerName = player.getName();
        if (this.unUsed(playerName))
            return;
        ViewPage viewPage = this.getUsingPage(playerName);
        ViewPage targetPage = viewPage;
        if (viewPage == null)
            return;
        // 找到目标页面
        int delta = page - viewPage.getPage();
        if (delta > 0) {
            for (int i = 0; i < delta; i++) {
                ViewPage tempPage = targetPage.getNext();
                if (tempPage == null) break;
                targetPage = tempPage;
            }
        } else {
            for (int i = 0; i > delta; i--) {
                ViewPage tempPage = targetPage.getPre();
                if (tempPage == null) break;
                targetPage = tempPage;
            }
        }
        // 打开目标页面
        viewRedirect.add(playerName);
        viewPageUsing.put(playerName, targetPage);
        player.openInventory(targetPage.getInventory());
        if (DEBUG) {
            String info = String.format("%s jump", playerName);
            CraftGUI.getPlugin().getLogger().info(info);
        }
    }

    @Override
    public void openView(@Nonnull Player player, @Nonnull String viewName, @Nullable String target) {
        String playerName = player.getName();
        // 记录玩家返回界面
        viewUsd.computeIfAbsent(playerName, k -> new Stack<>());
        ViewPage usingPage = null;
        if (this.unUsed(playerName)) {
            viewUsd.get(playerName).clear();
        } else {
            // 转存可以返回
            usingPage = this.getUsingPage(playerName);
            if (DEBUG) {
                String info = String.format("%s store back", playerName);
                CraftGUI.getPlugin().getLogger().info(info);
            }
        }
        // 添加返回
        if (usingPage != null) {
            viewRedirect.add(playerName);
            viewUsd.get(playerName).push(usingPage);
        }
        // 判断视图是否存在
        if (! viewMap.containsKey(viewName)) {
            String warn = String.format("View '%s' does not exist.", viewName);
            CraftGUI.getPlugin().getLogger().warning(warn);
            return;
        }
        // 获取视图的首页
        ViewPage page = viewMap.get(viewName).getFirstPage(target);
        // 首页是否存在
        if (page == null) {
            String warn = String.format("The first page of the '%s' does not exist.", viewName);
            CraftGUI.getPlugin().getLogger().warning(warn);
            return;
        }
        // 设置玩家正在浏览的视图
        viewUsing.put(playerName, viewName);
        viewPageUsing.put(playerName, page);
        // 打开容器
        player.openInventory(page.getInventory());
        if (DEBUG) {
            String info = String.format("%s open", playerName);
            CraftGUI.getPlugin().getLogger().info(info);
        }
    }

    @Override
    public void refreshPage(String... playerNames) {
        for (String playerName : playerNames) {
            // 是否浏览者
            if (unUsed(playerName)) continue;
            // 发包
            if (DEBUG) {
                String info = String.format("%s refresh", playerName);
                CraftGUI.getPlugin().getLogger().info(info);
            }
            this.sendItemsPacketAsynchronously(playerName);
        }
    }

    @Override
    public void refreshPages(@Nonnull String viewName, @Nonnull String target) {
        if (DEBUG) {
            String info = String.format("view(%s) of target(%s) refresh", viewName, target);
            CraftGUI.getPlugin().getLogger().info(info);
        }
        for (Entry<String, String> usingViewEntry : viewUsing.entrySet()) {
            String playerName = usingViewEntry.getKey();
            String usingViewName = usingViewEntry.getValue();
            // 判断是否浏览要刷新的界面
            if (!viewName.equals(usingViewName)) continue;
            // 获取浏览的页面，如果目标符合就刷新
            ViewPage page = viewPageUsing.get(playerName);
            if (page != null && target.equals(page.getTarget())) {
                this.refreshPage(playerName);
            }
        }
    }

    @Override
    public void updateButton(@Nonnull Player player, int slot, @Nonnull ItemStack buttonItem) {
        int window = 0;
        if (slot == -1) {
            window = -1;
        } else if (packets.containsKey(player.getName())) {
            window = packets.get(player.getName()).getIntegers().read(0);
        }
        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.SET_SLOT);
        packet.getIntegers().write(0, window);
        packet.getIntegers().write(2, slot);
        packet.getItemModifier().write(0, buttonItem);
        try {
            if (DEBUG) {
                String info = String.format("button update %s on %s", buttonItem.getType().name(),  slot);
                CraftGUI.getPlugin().getLogger().info(info);
            }
            protocolManager.sendServerPacket(player, packet);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Set<String> getViewers() {
        return viewPageUsing.keySet();
    }

    @Override
    public boolean unUsed(String playerName) {
        return !viewPageUsing.containsKey(playerName);
    }

    /**
     * 刷新页面的时候，异步发包，发送的是所有按钮，
     * 首次打开的异步刷新(page)，
     * 背包更新的异步刷新(page+inv)，
     * 导航的异步刷新(page)，
     * @param playerName 用户名
     */
    public void sendItemsPacketAsynchronously(final String playerName) {
        new BukkitRunnable() {
            @Override
            public void run() {
                // 获取正在浏览的页面
                ViewPage page = ViewGuideImpl.this.getUsingPage(playerName);
                // 获取数据包
                PacketContainer packet = packets.get(playerName);

                if (packet == null || page == null) return;

                // 获取页面所有按钮并放置到容器中，如果空的，填充空气
                List<ItemStack> itemStacks = packet.getItemListModifier().read(0);
                Map<Integer, ItemStack> viewItemMap = page.getItemMap();
                int invSize = page.getInventory().getSize();
                while (itemStacks.size() < invSize) itemStacks.add(AIR);
                for (int i = 0; i < invSize; i++) {
                    ItemStack item = viewItemMap.get(i);
                    itemStacks.set(i, Objects.requireNonNullElse(item, AIR));
                }

                // 写入
                packet.getItemListModifier().write(0, itemStacks);

                // 发送数据包
                Player player = Bukkit.getPlayerExact(playerName);
                if (player == null) return;
                try {
                    protocolManager.sendServerPacket(player, packet, false);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

                PacketContainer packetContainer = packet.shallowClone();
                // 删掉多余格，只有背包的异步更新需要删减
                Iterator<ItemStack> iterator = itemStacks.listIterator(invSize);
                while (iterator.hasNext()) {
                    iterator.next();
                    iterator.remove();
                }
                // 写入
                packetContainer.getItemListModifier().write(0, itemStacks);
                // 1.17开始才会更新鼠标上的物品
                if (CraftGUI.BUKKIT_VERSION > 16) packetContainer.getItemModifier().write(0, AIR);
                packets.put(playerName, packetContainer);

            }
        }.runTaskAsynchronously(CraftGUI.getPlugin());
    }

    /**
     * 删除玩家存包
     * @param playerName 玩家名称
     */
    public void removePacket(String playerName) {
        packets.remove(playerName);
    }

    /**
     * 创建一个数据包监听
     */
    private void createPacketListener() {
        // 打开界面监听
        protocolManager.addPacketListener(
                new PacketAdapter(CraftGUI.getPlugin(),
                        ListenerPriority.NORMAL,
                        PacketType.Play.Server.WINDOW_ITEMS) {

                    @Override
                    public void onPacketSending(PacketEvent event) {
                        if (event.getPacketType() != PacketType.Play.Server.WINDOW_ITEMS) return;

                        // 获取数据包
                        String playerName = event.getPlayer().getName();
                        PacketContainer packet = event.getPacket();

                        // 是否使用者
                        if (ViewGuideImpl.this.unUsed(playerName)) return;

                        // 获取包里的物品列表和页面
                        List<ItemStack> itemStacks = packet.getItemListModifier().read(0);
                        ViewPage page = ViewGuideImpl.this.getUsingPage(playerName);
                        if (page == null) return;
                        int invSize = page.getInventory().getSize();
                        // 没有存包，是第一个打开而非更新背包，删掉背包物品
                        if (! packets.containsKey(playerName)) {
                            // 删掉背包物品
                            int size = page.getInventory().getSize();
                            Iterator<ItemStack> iterator = itemStacks.listIterator(size);
                            while (iterator.hasNext()) {
                                iterator.next();
                                iterator.remove();
                            }
                        }

                        // 填充空气，防止在设置按钮时越界
                        while (itemStacks.size() < invSize) itemStacks.add(AIR);

                        // 放置固定按钮
                        for (Map.Entry<Integer, ItemStack> entry : page.getButtonMap().entrySet()) {
                            itemStacks.set(entry.getKey(), entry.getValue());
                        }

                        // 存包
                        packets.put(playerName, packet);

                        // 更新动态按钮
                        ViewGuideImpl.this.sendItemsPacketAsynchronously(playerName);
                    }
                });
    }

}
