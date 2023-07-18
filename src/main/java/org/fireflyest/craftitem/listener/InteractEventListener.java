package org.fireflyest.craftitem.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.fireflyest.craftitem.interact.InteractAction;
import org.fireflyest.util.ItemUtils;

public class InteractEventListener implements Listener {
    
    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        
    }

    @EventHandler
    public void onPlayerItemDamage(PlayerItemDamageEvent event) {
        
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        
    }
    
    @EventHandler
    public void onPlayerItemBreak(PlayerItemBreakEvent event) {
        
    }
    
    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item == null) {
            return;
        }

        switch (event.getAction()) {
            case LEFT_CLICK_BLOCK:
                
                break;
            case LEFT_CLICK_AIR:
            
                break;
            case RIGHT_CLICK_BLOCK:
            case RIGHT_CLICK_AIR:
                String useAction = ItemUtils.getItemNbt(item, InteractAction.TRIGGER_USE);
                String blockAction = ItemUtils.getItemNbt(item, InteractAction.TRIGGER_BLOCK);
                if (useAction != null) {
                    this.trigger(event.getPlayer(), item, useAction);
                }
                if (blockAction != null && event.getClickedBlock() != null) {
                    this.trigger(event.getPlayer(), item, blockAction);
                }
                break;
            case PHYSICAL:
                
                break;
            default:
                break;
        }
        String action = ItemUtils.getItemNbt(item, InteractAction.TRIGGER_USE);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        
    }

    private void trigger(Player player, ItemStack item, String action) {
        String[] actionValue = action.split(":");
        String value = actionValue[1];
        switch (actionValue[0]) {
            case InteractAction.ACTION_CUSTOM:
                
                break;
            case InteractAction.ACTION_CUSTOM_DISPOSABLE:
                
                break;
            case InteractAction.ACTION_POTION:
                
                break;
            case InteractAction.ACTION_COMMAND:
                player.performCommand(value);
                break;
            case InteractAction.ACTION_CONSOLE:
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), value);
                break;
            case InteractAction.ACTION_CONSOLE_DISPOSABLE:
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), value);
                item.setAmount(item.getAmount() - 1);
                break;
            default:
                break;
        }
    }

}
