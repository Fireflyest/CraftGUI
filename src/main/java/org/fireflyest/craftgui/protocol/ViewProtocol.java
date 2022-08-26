package org.fireflyest.craftgui.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Material;
import org.fireflyest.craftgui.CraftGUI;
import org.fireflyest.craftgui.api.ViewGuide;
import org.fireflyest.craftgui.api.ViewPage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.fireflyest.craftgui.core.ViewGuideImpl;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 玩家打开容器时，服务端发送物品数据包。如果玩家打开的是视图，本类先将数据包内物品更改为固定按钮，<br/>
 * 同时保存数据包，然后再发送一个包更新动态按钮，并删除保存的包<br/>
 * @author Fireflyest
 * 2022/1/16 18:45
 */
public class ViewProtocol {

    private static ProtocolManager protocolManager;
    private static JavaPlugin plugin;
    private static ViewGuide viewGuide;

    private static final ItemStack AIR = new ItemStack(Material.AIR);
    private static final Map<String, PacketContainer> packets = new ConcurrentHashMap<>();
    private ViewProtocol(){
    }

    /**
     * 一些初始化工作
     */
    public static void initViewProtocol(){
        protocolManager = ProtocolLibrary.getProtocolManager();
        plugin = CraftGUI.getPlugin();
        viewGuide = CraftGUI.getViewGuide();

        // 监听
        createPacketListener();
    }

    /**
     * 创建一个数据包监听<br/>
     */
    public static void createPacketListener(){
        // 打开界面监听
        protocolManager.addPacketListener(
                new PacketAdapter(plugin,
                        ListenerPriority.NORMAL,
                        PacketType.Play.Server.WINDOW_ITEMS) {

                    @Override
                    public void onPacketSending(PacketEvent event) {
                        if (event.getPacketType() != PacketType.Play.Server.WINDOW_ITEMS) return;

                        // 获取数据包
                        String playerName = event.getPlayer().getName();
                        PacketContainer packet = event.getPacket();

                        // 是否使用者
                        if (viewGuide.unUsed(playerName)) {
                            if (ViewGuideImpl.DEBUG) plugin.getLogger().info("viewGuide.unUsed(playerName) = " + viewGuide.unUsed(playerName));
                            return;
                        }

                        // 获取包里的物品列表和页面
                        List<ItemStack> itemStacks = packet.getItemListModifier().read(0);
                        ViewPage page = viewGuide.getUsingPage(playerName);
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
                        sendItemsPacketAsynchronously(playerName);
                    }
                });
    }

    /**
     * 刷新页面的时候，异步发包，发送的是所有按钮<br/>
     * <br/>
     * 首次打开的异步刷新(page)<br/>
     * 背包更新的异步刷新(page+inv)<br/>
     * 导航的异步刷新(page)<br/>
     *
     * @param playerName 用户名
     */
    public static void sendItemsPacketAsynchronously(final String playerName){
        new BukkitRunnable(){
            @Override
            public void run() {
                // 获取正在浏览的页面
                ViewPage page = viewGuide.getUsingPage(playerName);
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
                while (iterator.hasNext()){
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
     * 玩家关闭页面，删除玩家存储的数据包
     * @param playerName 游戏名
     */
    public static void removePacket(String playerName) {
        packets.remove(playerName);
    }


    public static void close(){
        protocolManager.removePacketListeners(CraftGUI.getPlugin());
    }

}
