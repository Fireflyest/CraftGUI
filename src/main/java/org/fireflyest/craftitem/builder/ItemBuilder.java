package org.fireflyest.craftitem.builder;

import com.cryptomorin.xseries.XMaterial;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * 构建自定义物品
 * @author Fireflyest
 * @since 2022/8/29
 */
public class ItemBuilder {
    protected Material material;
    protected String displayName;
    protected String localName;
    protected ItemFlag[] itemFlags;
    protected int model = -1;
    protected final List<String> lore = new ArrayList<>();
    protected final Map<String, Object> nbt = new HashMap<>();

    protected int amount = 1;

    public ItemBuilder(@Nullable Material material) {
        this.material = material;
    }

    public ItemBuilder(@Nonnull String material) {
        Optional<XMaterial> xMaterial = XMaterial.matchXMaterial(material);
        xMaterial.ifPresent(value -> this.material = value.parseMaterial());
    }
    public ItemBuilder material(@Nullable Material material){
        this.material = material;
        return this;
    }

    public ItemBuilder name(@Nonnull String displayName){
        this.displayName = displayName.replace("&", "§");
        return this;
    }

    public ItemBuilder localName(@Nonnull String localName){
        this.localName = localName;
        return this;
    }

    public ItemBuilder lore(@Nonnull String lore){
        this.lore.add(lore.replace("&", "§"));
        return this;
    }

    public ItemBuilder nbt(@Nonnull String key, Object value){
        nbt.put(key, value);
        return this;
    }

    public ItemBuilder flags(@Nonnull ItemFlag... itemFlags){
        this.itemFlags = itemFlags;
        return this;
    }

    public ItemBuilder amount(int amount){
        this.amount = amount;
        return this;
    }

    public ItemBuilder model(int model){
        this.model = model;
        return this;
    }

    public ItemStack build(){
        // 堆
        ItemStack item = new ItemStack(material == null ? Material.STONE : material);
        item.setAmount(amount);
        // 元数据
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item ;
        if (displayName != null) meta.setDisplayName(displayName);
        if (localName != null) meta.setLocalizedName(localName);
        if (lore.size() > 0) meta.setLore(lore);
        if (itemFlags != null && itemFlags.length > 0) meta.addItemFlags(itemFlags);
        if (model != -1) meta.setCustomModelData(model);
        item.setItemMeta(meta);
        // 特殊数据
        if (nbt.size() > 0){
            NBTItem nbtItem = new NBTItem(item, true);
            for (Map.Entry<String, Object> entry : nbt.entrySet()) {
                nbtItem.setObject(entry.getKey(), entry.getValue());
            }
        }
        return item;
    }

}