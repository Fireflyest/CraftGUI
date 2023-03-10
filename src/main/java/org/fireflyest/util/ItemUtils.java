package org.fireflyest.util;

import com.cryptomorin.xseries.XMaterial;

import de.tr7zw.changeme.nbtapi.NBTItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * 物品工具类
 * @author Fireflyest
 */
public class ItemUtils {

    private ItemUtils(){
    }

    /**
     * 设置物品NBT
     * @param item 物品
     * @param key 键
     * @param value 值
     */
    public static void setItemNbt(@Nonnull ItemStack item, String key, Object value) {
        if (item.getType() == XMaterial.AIR.parseMaterial()) return;
        NBTItem nbtItem = new NBTItem(item, true);
        nbtItem.setObject(key, value);
    }

    /**
     * 获取物品NBT
     * @param item 物品
     * @param key 键
     * @return 值
     */
    public static String getItemNbt(@Nonnull ItemStack item, String key) {
        if (item.getType() == XMaterial.AIR.parseMaterial()) return null;
        NBTItem nbtItem = new NBTItem(item);
        return nbtItem.getString(key);
    }

    /**
     * 设置物品自定义模型
     * @param item 物品
     * @param model 模型
     */
    public static void setItemModel(@Nonnull ItemStack item, int model) {
        if (item.getType() == XMaterial.AIR.parseMaterial()) return;
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setCustomModelData(model);
            item.setItemMeta(meta);
        }
    }

    /**
     * 物品是否有自定义NBT
     * @param item 物品
     * @return 是否有自定义NBT
     */
    public static boolean hasCustomNBT(@Nonnull ItemStack item) {
        if (item.getType() == XMaterial.AIR.parseMaterial()) return false;
        NBTItem nbtItem = new NBTItem(item);
        return nbtItem.hasCustomNbtData();
    }

    /**
     * 设置物品名称
     * @param item 物品
     * @param name 名称
     */
    public static void setDisplayName(@Nonnull ItemStack item, String name) {
        if (item.getType() == XMaterial.AIR.parseMaterial()) return;
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name.replace("&", "§"));
            item.setItemMeta(meta);
        }
    }

    /**
     * 添加注释
     * @param item 物品
     * @param lore 注释
     */
    public static void addLore(@Nonnull ItemStack item, String... lore) {
        if (item.getType() == XMaterial.AIR.parseMaterial()) return;
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            List<String> lores = meta.getLore();
            if (lores == null) {
                lores = new ArrayList<>();
            }
            lores.addAll(Arrays.asList(lore));
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
    public static void setLore(@Nonnull ItemStack item, String lore, int line) {
        if (item.getType() == XMaterial.AIR.parseMaterial()) return;
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            List<String> lores = item.getItemMeta().getLore();
            if (lores == null) {
                lores = new ArrayList<>();
            }
            if (line > 31) {
                line = 31;
            }
            while (lores.size() <= line) {
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
    public static void setSkullOwner(@Nonnull ItemStack item, OfflinePlayer player) {
        if (item.getType() != XMaterial.PLAYER_HEAD.parseMaterial()) return;
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        if (meta != null) {
            meta.setOwningPlayer(player);
            item.setItemMeta(meta);
        }
    }

    /**
     * 转化为nbt文本
     * @param item 物品
     * @return nbt文本
     */
    public static String toNbtString(@Nonnull ItemStack item) {
        return new NBTItem(item).toString();
    }

}
