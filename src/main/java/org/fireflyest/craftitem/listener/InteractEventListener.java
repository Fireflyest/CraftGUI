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
import org.bukkit.util.NumberConversions;
import org.fireflyest.craftitem.interact.InteractAction;
import org.fireflyest.util.ItemUtils;

public class InteractEventListener implements Listener {
    
    public InteractEventListener() {

    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        ItemStack item = event.getItem();
        if (item == null) {
            return;
        }
        int cooldown = NumberConversions.toInt(ItemUtils.getItemNbt(item, InteractAction.INTERACT_COOLDOWN));

        String consumeAction = ItemUtils.getItemNbt(item, InteractAction.TRIGGER_CONSUME);
        if (consumeAction != null && !"".equals(consumeAction)) {
            this.trigger(event.getPlayer(), item, consumeAction, cooldown);
        }
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
        int cooldown = NumberConversions.toInt(ItemUtils.getItemNbt(item, InteractAction.INTERACT_COOLDOWN));

        switch (event.getAction()) {
            case LEFT_CLICK_BLOCK:
                
                break;
            case LEFT_CLICK_AIR:
            
                break;
            case RIGHT_CLICK_BLOCK:
            case RIGHT_CLICK_AIR:
                String useAction = ItemUtils.getItemNbt(item, InteractAction.TRIGGER_USE);
                String blockAction = ItemUtils.getItemNbt(item, InteractAction.TRIGGER_BLOCK);
                if (useAction != null && !"".equals(useAction)) {
                    this.trigger(event.getPlayer(), item, useAction, cooldown);
                } else if (blockAction != null && !"".equals(blockAction) && event.getClickedBlock() != null) {
                    this.trigger(event.getPlayer(), item, blockAction, cooldown);
                }
                break;
            case PHYSICAL:
                break;
            default:
                break;
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        
    }

    private void trigger(Player player, ItemStack item, String action, int cooldown) {
        if (player.getCooldown(item.getType()) > 0) {
            return;
        }
        System.out.println("action = " + action);
        String[] actionValue = action.replace("\"", "").split(":");
        String value = actionValue[1].replace("%player%", player.getName());
        switch (actionValue[0]) {
            case InteractAction.ACTION_CUSTOM:
                
                break;
            case InteractAction.ACTION_CUSTOM_DISPOSABLE:
                
                break;
            case InteractAction.ACTION_POTION:
                
                break;
            case InteractAction.ACTION_COMMAND:
                System.out.println("performCommand = " + value);
                player.performCommand(value);
                break;
            case InteractAction.ACTION_CONSOLE:
                System.out.println("dispatchCommand = " + value);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), value);
                break;
            case InteractAction.ACTION_CONSOLE_DISPOSABLE:
                System.out.println("dispatchCommand disposable = " + value);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), value);
                item.setAmount(item.getAmount() - 1);
                break;
            default:
                break;
        }
        player.setCooldown(item.getType(), cooldown);
    }

}
