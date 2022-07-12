package org.fireflyest.craftgui.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.fireflyest.craftgui.CraftGUI;
import org.fireflyest.craftgui.api.ViewGuide;
import org.fireflyest.craftgui.api.ViewPage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 玩家打开容器时，服务端发送物品数据包。如果玩家打开的是视图，本类先将数据包内物品更改为固定按钮，
 * 同时保存数据包，然后再发送一个包更新动态按钮，并删除保存的包
 * @author Fireflyest
 * 2022/1/16 18:45
 */
public class ViewProtocol {

    private static ProtocolManager protocolManager;
    private static JavaPlugin plugin;
    private static ViewGuide viewGuide;

    private static final HashMap<String, PacketContainer> packets = new HashMap<>();

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
     * 创建一个数据包监听
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
                        // 非浏览者不响应
                        // 已经存包，说明已经发过，这个时候的异步包可能是动态按钮，不响应
                        if (!viewGuide.isViewer(playerName) || (packets.containsKey(playerName) && event.isAsync())) return;
                        // 非异步包不再发送，只能异步更新界面
                        if (packets.containsKey(playerName) && ! event.isAsync()) {
                            event.setCancelled(true);
                            return;
                        }

                        ViewPage page = viewGuide.getUsingPage(playerName);
                        // 判断玩家是否正在浏览界面
                        if (page == null) return;
                        int size = page.getInventory().getSize();

                        // 放置固定按钮
                        List<ItemStack> itemStacks = packet.getItemListModifier().read(0);
                        for (Map.Entry<Integer, ItemStack> entry : page.getButtonMap().entrySet()) {
                            itemStacks.set(entry.getKey(), entry.getValue());
                        }
                        // 删除容器的物品
                        Iterator<ItemStack> iterator = itemStacks.listIterator(size);
                        while (iterator.hasNext()) {
                            iterator.next();
                            iterator.remove();
                        }

                        packet.getItemListModifier().write(0, itemStacks);

                        // 存包
                        packets.put(playerName, packet);

                        // 更新动态按钮
                        sendItemsPacketAsynchronously(playerName);
                    }
                });
    }

    /**
     * 刷新页面的时候，异步发包，发送的是所有按钮
     * @param playerName 用户名
     */
    public static void sendItemsPacketAsynchronously(String playerName){
        new BukkitRunnable(){
            @Override
            public void run() {
                // 获取正在浏览的页面
                ViewPage page = viewGuide.getUsingPage(playerName);
                // 获取数据包
                PacketContainer packet = packets.get(playerName);

                if (packet == null || page == null) return;

                // 获取页面所有按钮并放置到容器中
                List<ItemStack> itemStacks = packet.getItemListModifier().read(0);
                for (Map.Entry<Integer, ItemStack> entry : page.getItemMap().entrySet()) {
                    itemStacks.set(entry.getKey(), entry.getValue());
                }

                // 写入
                packet.getItemListModifier().write(0, itemStacks);

                // 发送数据包
                Player player = Bukkit.getPlayer(playerName);
                if (player != null) {
                    try {
                        protocolManager.sendServerPacket(player, packet);
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.runTaskLaterAsynchronously(CraftGUI.getPlugin(), 1);
    }

    /**
     * 玩家关闭页面，删除玩家存储的数据包
     * @param playerName 游戏名
     */
    public static void removePacket(String playerName) {
        packets.remove(playerName);
    }
}
