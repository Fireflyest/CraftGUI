package org.fireflyest.craftgui.item;

import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于构建一个物品用来做按钮
 */
public class ViewItemBuilder implements Listener {

    // 材料
    private Material material;

    // 名称
    private String displayName;

    // 本地名称
    private String localName;
    // 执行指令
    private String command;

    // 物品标志
    private ItemFlag[] itemFlags;

    // 注释
    private final List<String> lore = new ArrayList<>();

    private int amount = 1;

    public ViewItemBuilder() {
    }

    public ViewItemBuilder(@Nullable Material material) {
        this.material = material;
    }

    public ViewItemBuilder material(@Nullable Material material){
        this.material = material;
        return this;
    }

    public ViewItemBuilder name(@NotNull String displayName){
        this.displayName = displayName.replace("&", "§");
        return this;
    }

    public ViewItemBuilder localName(@NotNull String localName){
        this.localName = localName;
        return this;
    }

    public ViewItemBuilder command(@NotNull String command){
        this.command = command;
        return this;
    }

    public ViewItemBuilder lore(@NotNull String lore){
        this.lore.add(lore.replace("&", "§"));
        return this;
    }

    public ViewItemBuilder flags(@NotNull ItemFlag... itemFlags){
        this.itemFlags = itemFlags;
        return this;
    }

    public ViewItemBuilder amount(int amount){
        this.amount = amount;
        return this;
    }

    public ItemStack build(){
        ItemStack item = new ItemStack(material == null ? Material.STONE : material);

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item ;

        if (displayName != null) meta.setDisplayName(displayName);
        if (localName != null) meta.setLocalizedName(localName);
        // TODO: 2022/7/17 command的nbt实现
        if (command != null) meta.setLocalizedName(command);
        if (lore.size() > 0) meta.setLore(lore);
        if (itemFlags != null && itemFlags.length > 0) meta.addItemFlags(itemFlags);

        item.setItemMeta(meta);
        item.setAmount(amount);

        return item;
    }

}
