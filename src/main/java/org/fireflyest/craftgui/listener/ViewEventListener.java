package org.fireflyest.craftgui.listener;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.fireflyest.craftgui.CraftGUI;
import org.fireflyest.craftgui.api.ViewGuide;
import org.fireflyest.craftgui.api.ViewPage;
import org.fireflyest.craftgui.core.ViewGuideImpl;
import org.fireflyest.craftgui.event.ViewClickEvent;
import org.fireflyest.craftgui.event.ViewHotbarEvent;
import org.fireflyest.craftgui.event.ViewPlaceEvent;
import org.fireflyest.craftgui.protocol.ViewProtocol;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 玩家容器事件监听
 * @author Fireflyest
 * 2022/2/17 20:25
 */

public class ViewEventListener implements Listener {

    private final ViewGuide guide;

    public ViewEventListener(){
        this.guide = CraftGUI.getViewGuide();
    }

    /**
     * 监听玩家点击容器时的事件
     * @param event 容器点击事件
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClick(InventoryClickEvent event){
        // 判断是否浏览者
        HumanEntity human = event.getWhoClicked();
        String playerName = human.getName();
        if (guide.unUsed(playerName)) return;

        // 获取点击页面
        ViewPage page = guide.getUsingPage(playerName);
        // 获取点击信息
        Inventory inventory = page.getInventory();
        InventoryAction action = event.getAction();
        ClickType type = event.getClick();

        // 是否点击容器内
        if (event.getRawSlot() >= 0 && event.getRawSlot() < inventory.getSize()){
            // 获取点击的物品
            ItemStack clickItem = page.getItem(event.getRawSlot());
            ItemStack cursor = event.getCursor();
            // 判断操作行为
            if (InventoryAction.PLACE_ALL == action || InventoryAction.PLACE_ONE == action || InventoryAction.PLACE_SOME == action){
                // 在容器内放置东西
                if (cursor == null) return;
                ViewPlaceEvent placeEvent = new ViewPlaceEvent(event.getView(), event.getClick(), event.getSlot(), clickItem, cursor.clone());
                cursor.setAmount(0);
                Bukkit.getPluginManager().callEvent(placeEvent);
                // 还给玩家
                if (placeEvent.handBack()){
                    human.getInventory().addItem(placeEvent.getCursorItem());
                }
                // 刷新页面
                guide.refreshPage(playerName);
            }else if(ClickType.NUMBER_KEY == type || ClickType.SWAP_OFFHAND == type) {
                // 试图和页面中的物品交换，给出一个事件
                ViewHotbarEvent hotbarEvent = null;
                if (clickItem != null && clickItem.getType() != Material.AIR) {
                    hotbarEvent = new ViewHotbarEvent(event.getView(), event.getClick(), event.getSlot(), clickItem, event.getHotbarButton());
                }
                // 防止卡物品出来，把物品放进去的一瞬间按数字键或副手有几率卡物品出来
                if (event.getCurrentItem() != null) event.getCurrentItem().setAmount(0);
                // call 事件
                if (hotbarEvent != null) Bukkit.getPluginManager().callEvent(hotbarEvent);
                // 获取被玩家放上去的物品
                ItemStack swap;
                if (event.getHotbarButton() == -1){
                    swap = human.getInventory().getItemInOffHand();
                }else {
                    int itemIndex = event.getHotbarButton();
                    swap = human.getInventory().getItem(itemIndex);
                }
                // 如果没有物品被放上去，直接刷新
                if (swap == null || swap.getType() == Material.AIR) {
                    guide.refreshPage(playerName);
                    return;
                }
                // 要归还物品
                ItemStack itemBack = swap.clone();
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        inventory.remove(swap);
                    }
                }.runTask(CraftGUI.getPlugin());
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        human.getInventory().addItem(itemBack);
                    }
                }.runTaskLater(CraftGUI.getPlugin(), 5);
                // 刷新页面
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        if (clickItem != null && clickItem.getType() != Material.AIR) guide.refreshPage(playerName);
                    }
                }.runTaskLater(CraftGUI.getPlugin(), 2);
            }else {
                // 点空格不起作用
                if (clickItem == null || clickItem.getType() == Material.AIR) return;

                // 发布事件
                ViewClickEvent clickEvent = new ViewClickEvent(event.getView(), event.getClick(), event.getSlot(), clickItem, true);
                Bukkit.getPluginManager().callEvent(clickEvent);

                // 刷新页面
                if (clickEvent.needRefresh()) guide.refreshPage(playerName);
            }
        }else {
            // 防止移动东西进入
            if (InventoryAction.MOVE_TO_OTHER_INVENTORY == action) {
                // 取消并刷新页面
                event.setCancelled(true);
                guide.refreshPage(playerName);
            }
        }

    }

    /**
     * 监听玩家在容器中拖动物品，将物品分散到各个格的时候的事件
     * @param event 物品拖动事件
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryDrag(InventoryDragEvent event){
        // 判断是否浏览者
        HumanEntity human = event.getWhoClicked();
        String playerName = human.getName();
        if (guide.unUsed(playerName)) return;

        // 获取点击页面
        ViewPage page = guide.getUsingPage(playerName);
        // 获取点击信息
        final Inventory inventory = page.getInventory();
        // 是否有拖到容器里
        Set<Map.Entry<Integer, ItemStack>> giveBackList =  event.getNewItems().entrySet().stream().filter(itemEntry -> {
            int slot = itemEntry.getKey();
            return (slot >= 0 && slot < inventory.getSize());
        }).collect(Collectors.toSet());

        if (giveBackList.size() > 0){
            // 清空
            new BukkitRunnable(){
                @Override
                public void run() {
                    inventory.clear();
                }
            }.runTask(CraftGUI.getPlugin());
            // 还给玩家
            for (Map.Entry<Integer, ItemStack> integerItemStackEntry : giveBackList) {
                human.getInventory().addItem(integerItemStackEntry.getValue());
            }
        }
    }

    /**
     * 玩家关闭容器，说明一次浏览结束
     * @param event 容器关闭事件
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClose(InventoryCloseEvent event){
        String playerName = event.getPlayer().getName();
        // 判断是否在浏览
        if (guide.unUsed(playerName)) return;

        // 关闭界面
        if(guide.getUsingPage(playerName) != null) {
            if (ViewGuideImpl.DEBUG) CraftGUI.getPlugin().getLogger().info("onInventoryClose");
            guide.closeView(playerName);
        }
        // 删除数据包
        ViewProtocol.removePacket(playerName);
    }

}
