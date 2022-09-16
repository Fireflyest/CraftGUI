package org.fireflyest.craftgui.listener;

import com.cryptomorin.xseries.XSound;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.NumberConversions;
import org.fireflyest.CraftGUI;
import org.fireflyest.craftgui.api.ViewGuide;
import org.fireflyest.craftgui.api.ViewPage;
import org.fireflyest.craftgui.button.ButtonAction;
import org.fireflyest.craftgui.core.ViewGuideImpl;
import org.fireflyest.craftgui.event.ViewClickEvent;
import org.fireflyest.craftgui.event.ViewHotbarEvent;
import org.fireflyest.craftgui.event.ViewPlaceEvent;
import org.fireflyest.craftgui.protocol.ViewProtocol;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.fireflyest.craftgui.util.ViewItemUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 玩家容器事件监听
 * @author Fireflyest
 * 2022/2/17 20:25
 */

public class ViewEventListener implements Listener {

    private final ViewGuide guide;
    private final Sound clickSound;
    private final Sound pageSound;

    public ViewEventListener(){
        this.guide = CraftGUI.getViewGuide();
        this.clickSound = XSound.BLOCK_STONE_BUTTON_CLICK_OFF.parseSound();
        this.pageSound = XSound.ITEM_BOOK_PAGE_TURN.parseSound();
    }

    /**
     * 监听玩家点击容器时的事件
     * @param event 容器点击事件
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClick(InventoryClickEvent event){
        // 判断是否浏览者
        Player human = ((Player) event.getWhoClicked());
        String playerName = human.getName();
        if (guide.unUsed(playerName)) return;

        // 获取点击页面
        ViewPage page = guide.getUsingPage(playerName);
        if (page == null) return;
        // 获取点击信息
        Inventory inventory = page.getInventory();
        InventoryAction action = event.getAction();
        ClickType type = event.getClick();

        // 是否点击容器内
        if (event.getRawSlot() >= 0 && event.getRawSlot() < inventory.getSize()){
            // 获取点击的物品
            ItemStack clickItem = page.getItem(event.getRawSlot());
            ItemStack cursor = event.getCursor();
            // 按钮行为
            int buttonAction = ButtonAction.ACTION_NONE;
            String buttonValue = "";
            // 判断操作行为
            if (InventoryAction.PLACE_ALL == action || InventoryAction.PLACE_ONE == action || InventoryAction.PLACE_SOME == action){

                // 在容器内放置东西
                if (cursor == null) return;
                if (clickItem != null) {
                    buttonAction = ViewItemUtils.getItemAction(clickItem);
                    buttonValue = ViewItemUtils.getItemValue(clickItem);
                }
                ViewPlaceEvent placeEvent = null;
                switch (buttonAction){
                    case ButtonAction.ACTION_NONE:
                    case ButtonAction.ACTION_OPEN:
                    case ButtonAction.ACTION_CONSOLE_COMMAND:
                    case ButtonAction.ACTION_PAGE:
                    case ButtonAction.ACTION_PLAYER_COMMAND:
                    case ButtonAction.ACTION_PLUGIN:
                    case ButtonAction.ACTION_UNKNOWN:
                        placeEvent = new ViewPlaceEvent(event.getView(),
                                event.getClick(),
                                event.getSlot(),
                                clickItem,
                                cursor.clone(),
                                guide.getUsingView(playerName),
                                buttonValue,
                                buttonAction);
                        break;
                    default:
                }
                cursor.setAmount(0);
                if (placeEvent != null) {
                    Bukkit.getPluginManager().callEvent(placeEvent);
                    // 还给玩家
                    if (placeEvent.handBack()){
                        human.getInventory().addItem(placeEvent.getCursorItem());
                    }
                }
                // 刷新页面
                guide.refreshPage(playerName);

            }else if(ClickType.NUMBER_KEY == type || (CraftGUI.BUKKIT_VERSION > 15 && ClickType.SWAP_OFFHAND == type)) {
                // 试图和页面中的物品交换，给出一个事件
                ViewHotbarEvent hotbarEvent = null;
                if (clickItem != null && clickItem.getType() != Material.AIR) {
                    buttonAction = ViewItemUtils.getItemAction(clickItem);
                    buttonValue = ViewItemUtils.getItemValue(clickItem);
                    switch (buttonAction){
                        case ButtonAction.ACTION_NONE:
                        case ButtonAction.ACTION_PAGE:
                        case ButtonAction.ACTION_PLUGIN:
                        case ButtonAction.ACTION_UNKNOWN:
                            hotbarEvent = new ViewHotbarEvent(event.getView(),
                                    event.getClick(),
                                    event.getSlot(),
                                    clickItem,
                                    event.getHotbarButton(),
                                    guide.getUsingView(playerName),
                                    buttonValue,
                                    buttonAction);
                            break;
                        default:
                    }
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
                buttonAction = ViewItemUtils.getItemAction(clickItem);
                buttonValue = ViewItemUtils.getItemValue(clickItem);
                switch (buttonAction){
                    case ButtonAction.ACTION_NONE:
                    case ButtonAction.ACTION_OPEN:
                    case ButtonAction.ACTION_PAGE:
                    case ButtonAction.ACTION_PLUGIN:
                    case ButtonAction.ACTION_UNKNOWN:
                    case ButtonAction.ACTION_PLAYER_COMMAND:
                    case ButtonAction.ACTION_CONSOLE_COMMAND:
                        // 发布事件
                        ViewClickEvent clickEvent = new ViewClickEvent(event.getView(),
                                event.getClick(),
                                event.getSlot(),
                                clickItem,
                                guide.getUsingView(playerName),
                                buttonValue,
                                buttonAction,
                                true);
                        Bukkit.getPluginManager().callEvent(clickEvent);
                        // 刷新页面
                        if (clickEvent.needRefresh()) guide.refreshPage(playerName);
                        break;
                    case ButtonAction.ACTION_BACK:
                        guide.back(human);
                        human.playSound(human.getLocation(), clickSound, 1F, 1F);
                        if (event.isShiftClick()) guide.refreshPage(playerName);
                        break;
                    case ButtonAction.ACTION_CLOSE:
                        human.closeInventory();
                        human.playSound(human.getLocation(), clickSound, 1F, 1F);
                        if (event.isShiftClick()) guide.refreshPage(playerName);
                        break;
                    case ButtonAction.ACTION_PAGE_NEXT:
                        guide.nextPage(human);
                        human.playSound(human.getLocation(), pageSound, 1F, 1F);
                        if (event.isShiftClick()) guide.refreshPage(playerName);
                        break;
                    case ButtonAction.ACTION_PAGE_PRE:
                        guide.prePage(human);
                        human.playSound(human.getLocation(), pageSound, 1F, 1F);
                        if (event.isShiftClick()) guide.refreshPage(playerName);
                        break;
                    case ButtonAction.ACTION_PAGE_JUMP:
                        guide.jump(human, NumberConversions.toInt(buttonValue));
                        human.playSound(human.getLocation(), pageSound, 1F, 1F);
                        if (event.isShiftClick()) guide.refreshPage(playerName);
                        break;
                    case ButtonAction.ACTION_CONSOLE_COMMAND_SEND:
                        if (buttonValue == null || "".equals(buttonValue)) break;
                        String command = buttonValue.replace("%player%", playerName);
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
                        human.playSound(human.getLocation(), clickSound, 1F, 1F);
                        if (event.isShiftClick()) guide.refreshPage(playerName);
                        break;
                    case ButtonAction.ACTION_PLAYER_COMMAND_SEND:
                        if (buttonValue == null || "".equals(buttonValue)) break;
                        String playerCommand = buttonValue.replace("%player%", playerName);
                        human.performCommand(playerCommand);
                        human.playSound(human.getLocation(), clickSound, 1F, 1F);
                        if (event.isShiftClick()) guide.refreshPage(playerName);
                        break;
                    case ButtonAction.ACTION_PAGE_OPEN:
                        if (buttonValue == null || !buttonValue.contains(".")) break;
                        String view = buttonValue.substring(0, buttonValue.lastIndexOf("."));
                        String pageTarget = buttonValue.substring(buttonValue.lastIndexOf(".")+1);
                        human.playSound(human.getLocation(), clickSound, 1F, 1F);
                        guide.openView(human, view, pageTarget);
                        if (event.isShiftClick()) guide.refreshPage(playerName);
                        break;
                    default:
                }
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
        if (page == null) return;
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
