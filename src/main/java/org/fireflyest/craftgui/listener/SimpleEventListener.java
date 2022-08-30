package org.fireflyest.craftgui.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.fireflyest.CraftGUI;
import org.fireflyest.craftgui.api.ViewGuide;
import org.fireflyest.craftgui.button.ButtonAction;
import org.fireflyest.craftgui.core.ViewGuideImpl;
import org.fireflyest.craftgui.event.ViewClickEvent;

/**
 * test
 *
 * @author Fireflyest
 * @since 2022/8/4
 */
public class SimpleEventListener implements Listener {

    public SimpleEventListener(){
    }

    /**
     * 用于控制调试
     * @param event 聊天事件
     */
    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (player.isOp() && event.getMessage().contains("gui debug")){
            ViewGuideImpl.DEBUG = !ViewGuideImpl.DEBUG;
        }
    }

    @EventHandler
    public void onViewClick(ViewClickEvent event) {
        int action = event.getAction();
        String view = event.getViewName(), value = event.getValue();

        // 判断是否本插件相关的事件
        if(view == null || !view.startsWith("craftgui")) return;

        // 获取点击到的物品，一般来说是有物品
        ItemStack item = event.getCurrentItem();
        if(item == null) return;

        // 获取点击的玩家
        Player player = (Player)event.getWhoClicked();

        // 根据行为做反应
        if (action == ButtonAction.ACTION_PLUGIN) {
            // do something
            player.sendMessage("is " + value);
        }
    }

}
