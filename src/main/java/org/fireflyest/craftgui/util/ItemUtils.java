package org.fireflyest.craftgui.util;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fireflyest
 */
public class ItemUtils {

    private ItemUtils(){
    }

    /**
     * 设置物品名称
     * @param item 物品
     * @param name 名称
     */
    public static void setDisplayName(ItemStack item, String name) {
        ItemMeta meta = item.getItemMeta();
        if(meta != null){
            meta.setDisplayName(name.replace("&", "§"));
            item.setItemMeta(meta);
        }
    }

    /**
     * 添加注释
     * @param item 物品
     * @param lore 注释
     */
    public static void addLore(ItemStack item, String lore){
        ItemMeta meta = item.getItemMeta();
        if(meta != null){
            List<String> lores = item.getItemMeta().getLore();
            if (lores == null) {
                lores = new ArrayList<>();
            }
            lores.add(lore);
            meta.setLore(lores);
            item.setItemMeta(meta);
        }
    }

}
