package org.fireflyest.craftgui.listener;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.fireflyest.craftgui.CraftGUI;
import org.fireflyest.craftgui.api.ViewGuide;
import org.fireflyest.craftgui.core.ViewGuideImpl;
import org.fireflyest.craftgui.event.ViewClickEvent;
import org.fireflyest.craftgui.item.ViewItem;
import org.fireflyest.craftgui.util.ItemUtils;

/**
 * test
 *
 * @author Fireflyest
 * @since 2022/8/4
 */
public class SimpleEventListener implements Listener {

    private final ViewGuide guide;

    public SimpleEventListener(){
        this.guide = CraftGUI.getViewGuide();
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        // 测试用的，不用管
        Player player = event.getPlayer();
        String playerName = player.getName();
        if (ViewGuideImpl.DEBUG) {
            guide.openView(player, CraftGUI.SIMPLE_VIEW, playerName);
            new BukkitRunnable(){
                @Override
                public void run() {
                    guide.openView(player, CraftGUI.SIMPLE_VIEW, "playerName");
                }
            }.runTaskLater(CraftGUI.getPlugin(), 40);
        }
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        // 测试用的，不用管
        Player player = event.getPlayer();
        if (player.isOp() && event.getMessage().contains("gui debug")){
            ViewGuideImpl.DEBUG = !ViewGuideImpl.DEBUG;
            player.sendMessage(String.format("[CraftGUI] debug -> %s", ViewGuideImpl.DEBUG));
        }
    }

    @EventHandler
    public void onViewClick(ViewClickEvent event) {
        // 判断是否本插件相关的事件
        if(!event.getView().getTitle().contains("[CraftGUI]")) return;
        // 是否点击到物品，一般来说是有物品
        ItemStack item = event.getCurrentItem();
        if(item == null) return;
        // 获取点击的玩家
        Player player = (Player)event.getWhoClicked();
        // 获取行为和值
        int action = ItemUtils.getItemAction(item);
        String value = ItemUtils.getItemValue(item);
        // 根据行为做反应，如果是页面跳转，请取消刷新，防止不必要算力消耗
        switch (action){
            case ViewItem.ACTION_CLOSE: // 关闭页面
                event.setRefresh(false);
                player.closeInventory();
                break;
            case ViewItem.ACTION_PAGE: // 页面跳转
                event.setRefresh(false);
                if ("pre".equals(value)){
                    guide.prePage(player);
                } else if ("next".equals(value)) {
                    guide.nextPage(player);
                } else {
                    guide.jump(player, NumberUtils.toInt(value));
                }
                break;
            case ViewItem.ACTION_BACK: // 返回上一个界面
                event.setRefresh(false);
                guide.back(player);
                break;
            case ViewItem.ACTION_OPEN: // 打开一个界面
                event.setRefresh(false);
                guide.openView(player, CraftGUI.SIMPLE_VIEW, value);
                break;
            case ViewItem.ACTION_PLAYER_COMMAND: // 玩家指令
                if (value != null) player.performCommand(value);
                break;
            case ViewItem.ACTION_CONSOLE_COMMAND: // 控制台指令
                if (value != null) {
                    Bukkit.getServer().dispatchCommand(
                            Bukkit.getConsoleSender(), value.replace("%player%", player.getName()));
                }
                break;
            case ViewItem.ACTION_PLUGIN:
                // do something
                break;
            case ViewItem.ACTION_UNKNOWN:
            case ViewItem.ACTION_EDIT:
            case ViewItem.ACTION_NONE:
            default:
        }
    }

}
