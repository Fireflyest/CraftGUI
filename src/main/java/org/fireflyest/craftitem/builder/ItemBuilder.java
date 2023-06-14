package org.fireflyest.craftitem.builder;

import com.cryptomorin.xseries.XMaterial;
import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.changeme.nbtapi.NBTList;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.fireflyest.CraftGUI;
import org.fireflyest.crafttext.formal.TextColorFormal;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * 构建自定义物品
 * @author Fireflyest
 * @since 2022/8/29
 */
public class ItemBuilder {

    protected boolean update = true;

    protected Material material;
    protected String displayName;
    protected ItemFlag[] itemFlags;
    protected final List<String> lore = new ArrayList<>();
    protected final Map<String, Object> nbt = new HashMap<>();
    protected boolean colorful = false;
    protected int amount = 1;
    protected int model = -1;

    protected ItemStack cache;

    /**
     * 使用材料快速物品构建
     * @param material 材料
     */
    public ItemBuilder(@Nullable Material material) {
        this.material = material;
    }

    /**
     * 使用材料名称快速物品构建
     * @param material 材料名称
     */
    public ItemBuilder(@Nonnull String material) {
        Optional<XMaterial> xMaterial = XMaterial.matchXMaterial(material);
        xMaterial.ifPresent(value -> this.material = value.parseMaterial());
    }

    /**
     * 命名
     * @param displayName 名称
     * @return 构建对象
     */
    public ItemBuilder name(@Nonnull String displayName) {
        update = true;
        this.displayName = displayName.replace("&", "§");
        return this;
    }

    public ItemBuilder lore(@Nonnull String lore) {
        update = true;
        this.lore.add(lore.replace("&", "§"));
        return this;
    }

    public ItemBuilder nbt(@Nonnull String key, Object value) {
        update = true;
        nbt.put(key, value);
        return this;
    }

    public ItemBuilder flags(@Nonnull ItemFlag... itemFlags) {
        update = true;
        this.itemFlags = itemFlags;
        return this;
    }

    public ItemBuilder colorful() {
        update = true;
        this.colorful = CraftGUI.BUKKIT_VERSION >= 16;
        return this;
    }

    public ItemBuilder amount(int amount) {
        update = true;
        this.amount = amount;
        return this;
    }

    public ItemBuilder model(int model) {
        update = true;
        this.model = model;
        return this;
    }

    /**
     * 完成构建
     * @return 物品
     */
    public ItemStack build() {
        // 缓存
        if (!update) {
            return cache.clone();
        }

        // 堆
        ItemStack item = new ItemStack(material == null ? Material.STONE : material);
        item.setAmount(amount);
        // 元数据
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return item;
        }
        if (displayName != null) {
            meta.setDisplayName(displayName);
        }
        if (!lore.isEmpty()) {
            meta.setLore(lore);
        }
        if (itemFlags != null && itemFlags.length > 0) {
            meta.addItemFlags(itemFlags);
        }
        if (model != -1) {
            meta.setCustomModelData(model);
        }
        item.setItemMeta(meta);

        // 特殊数据
        NBTItem nbtItem = new NBTItem(item);
        // 颜色
        if (colorful){
            this.colorData(nbtItem);
        }
        if (nbt.size() > 0){
            for (Map.Entry<String, Object> entry : nbt.entrySet()) {
                nbtItem.setObject(entry.getKey(), entry.getValue());
            }
        }
        item = nbtItem.getItem();

        cache = item.clone();
        update = false;

        return item;
    }

    /**
     * 颜色数据
     * @param nbtItem 物品
     */
    private void colorData(NBTItem nbtItem) {
        NBTCompound display = nbtItem.getCompound("display");
        if (display != null) {
            // name
            display.setString("Name", new TextColorFormal(display.getString("Name")).toString());
            // lore
            NBTList<String> loreList = display.getStringList("Lore");
            if (loreList != null) {
                int lorePos = 0;
                for (String loreString : loreList) {
                    loreList.set(lorePos++, new TextColorFormal(loreString).toString());
                }
            }
        }
    }

}
