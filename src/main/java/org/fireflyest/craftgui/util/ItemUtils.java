package org.fireflyest.craftgui.util;

import com.cryptomorin.xseries.XMaterial;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.fireflyest.craftgui.item.ViewItem;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Fireflyest
 */
public class ItemUtils {

    private ItemUtils(){
    }

    public static void setItemValue(@NotNull ItemStack item, String value) {
        if (item.getType() == XMaterial.AIR.parseMaterial()) return;
        NBTItem nbtItem = new NBTItem(item, true);
        nbtItem.setString(ViewItem.NBT_VALUE_KEY, value);
    }

    public static String getItemValue(@NotNull ItemStack item) {
        if (item.getType() == XMaterial.AIR.parseMaterial()) return null;
        NBTItem nbtItem = new NBTItem(item);
        return nbtItem.getString(ViewItem.NBT_VALUE_KEY);
    }

    public static void setItemAction(@NotNull ItemStack item, int action) {
        if (item.getType() == XMaterial.AIR.parseMaterial()) return;
        NBTItem nbtItem = new NBTItem(item, true);
        nbtItem.setInteger(ViewItem.NBT_ACTION_KEY, action);
    }

    public static int getItemAction(@NotNull ItemStack item) {
        if (item.getType() == XMaterial.AIR.parseMaterial()) return 0;
        NBTItem nbtItem = new NBTItem(item);
        return nbtItem.getInteger(ViewItem.NBT_ACTION_KEY);
    }

    public static boolean hasCustomNBT(@NotNull ItemStack item){
        if (item.getType() == XMaterial.AIR.parseMaterial()) return false;
        NBTItem nbtItem = new NBTItem(item);
        return nbtItem.hasCustomNbtData();
    }

    /**
     * 设置物品名称
     * @param item 物品
     * @param name 名称
     */
    public static void setDisplayName(@NotNull ItemStack item, String name) {
        if (item.getType() == XMaterial.AIR.parseMaterial()) return;
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
    public static void addLore(@NotNull ItemStack item, String lore){
        if (item.getType() == XMaterial.AIR.parseMaterial()) return;
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

    /**
     * 设置物品注释
     * @param item 物品
     * @param lore 注释
     * @param line 行
     */
    public static void setLore(@NotNull ItemStack item, String lore, int line){
        if (item.getType() == XMaterial.AIR.parseMaterial()) return;
        ItemMeta meta = item.getItemMeta();
        if(meta != null){
            List<String> lores = item.getItemMeta().getLore();
            if (lores == null) {
                lores = new ArrayList<>();
            }
            while (lores.size() <= line){
                lores.add("");
            }
            lores.set(line, lore);
            meta.setLore(lores);
            item.setItemMeta(meta);
        }
    }

    /**
     * 设置玩家头颅
     * @param item 物品
     * @param player 头颅
     */
    public static void setSkullOwner(@NotNull ItemStack item, OfflinePlayer player){
        if (item.getType() != XMaterial.PLAYER_HEAD.parseMaterial()) return;
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        if (meta != null) {
            meta.setOwningPlayer(player);
            item.setItemMeta(meta);
        }
    }

}
