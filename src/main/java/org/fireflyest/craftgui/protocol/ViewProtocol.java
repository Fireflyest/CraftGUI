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

import java.lang.reflect.InvocationTargetException;
import java.util.*;

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

    private static final ItemStack AIR = new ItemStack(Material.AIR);
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
//                        System.out.println("**************************************************************************************");
//                        List<ItemStack> iss = packet.getItemListModifier().read(0);
//                        System.out.println("isAsync = " + event.isAsync());
//                        ItemStack itr = packet.getItemModifier().read(0);
//                        System.out.println("Item = " + itr);
//                        StringBuilder sb = new StringBuilder();
//                        for (int i = 0; i < iss.size(); i++) {
//                            ItemStack is = iss.get(i);
//                            if (is == null) {
//                                continue;
//                            }
//                            sb.append(i)
//                                    .append("{")
//                                    .append(is.getType())
//                                    .append("x")
//                                    .append(is.getAmount())
//                                    .append("} ");
//                            if (i % 8 == 0){
//                                System.out.println(sb);
//                                sb = new StringBuilder();
//                            }
//                        }
//                        packet.getItemListModifier().write(0, iss);
//                        packet.getItemModifier().write(0, itr);


                        // 非浏览者不响应
                        // 已经存包，说明已经发过，这个时候的异步包可能是动态按钮，不响应
                        if (viewGuide.unUsed(playerName) || (packets.containsKey(playerName) && event.isAsync())) return;

                        ViewPage page = viewGuide.getUsingPage(playerName);
                        // 是否页面的浏览者浏览者
                        if (! page.getInventory().getViewers().contains(event.getPlayer())){
                            viewGuide.closeView(playerName);
                            return;
                        }
                        // 放置固定按钮
                        List<ItemStack> itemStacks = packet.getItemListModifier().read(0);
                        int invSize = page.getInventory().getSize();
                        while (itemStacks.size() < invSize) itemStacks.add(AIR);
                        for (Map.Entry<Integer, ItemStack> entry : page.getButtonMap().entrySet()) {
                            itemStacks.set(entry.getKey(), entry.getValue());
                        }
                        // 如果还没存包，说明第一次打开，只发送容器内物品，所以删除容器的物品
                        if (! packets.containsKey(playerName)){
                            int size = page.getInventory().getSize();
                            Iterator<ItemStack> iterator = itemStacks.listIterator(size);
                            while (iterator.hasNext()) {
                                iterator.next();
                                iterator.remove();
                            }
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

                // 获取页面所有按钮并放置到容器中，如果空的，填充空气
                List<ItemStack> itemStacks = packet.getItemListModifier().read(0);
                Map<Integer, ItemStack> viewItemMap = page.getItemMap();
                int invSize = page.getInventory().getSize();
                while (itemStacks.size() < invSize) itemStacks.add(AIR);
                for (int i = 0; i < invSize; i++) {
                    ItemStack item = viewItemMap.get(i);
                    if (item == null) {
                        itemStacks.set(i, AIR);
                    }else {
                        itemStacks.set(i, item);
                    }
                }

                // 写入
                packet.getItemListModifier().write(0, itemStacks);

                // 发送数据包
                Player player = Bukkit.getPlayerExact(playerName);
                if (player == null) return;
                try {
                    protocolManager.sendServerPacket(player, packet);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

                PacketContainer packetContainer = packet.shallowClone();
                // 删掉多余格
                Iterator<ItemStack> iterator = itemStacks.listIterator(invSize);
                while (iterator.hasNext()){
                    iterator.next();
                    iterator.remove();
                }
                // 写入
                packetContainer.getItemListModifier().write(0, itemStacks);
                // 1.17开始才会更新鼠标上的物品
                if (CraftGUI.BUKKIT_VERSION > 16) packetContainer.getItemModifier().write(0, null);
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
}
