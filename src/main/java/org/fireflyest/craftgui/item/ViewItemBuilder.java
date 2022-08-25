package org.fireflyest.craftgui.item;

import com.cryptomorin.xseries.XMaterial;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 用于构建一个物品用来做按钮
 */
public class ViewItemBuilder extends ViewItem implements Listener {

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

    private int model = -1;

    // 注释
    private final List<String> lore = new ArrayList<>();

    private int amount = 1;

    public ViewItemBuilder() {
    }

    public ViewItemBuilder(@Nullable Material material) {
        this.material = material;
    }

    public ViewItemBuilder(@Nonnull String material) {
        Optional<XMaterial> xMaterial = XMaterial.matchXMaterial(material);
        xMaterial.ifPresent(value -> this.material = value.parseMaterial());
    }
    public ViewItemBuilder material(@Nullable Material material){
        this.material = material;
        return this;
    }

    public ViewItemBuilder name(@Nonnull String displayName){
        this.displayName = displayName.replace("&", "§");
        return this;
    }

    public ViewItemBuilder localName(@Nonnull String localName){
        this.localName = localName;
        return this;
    }

    public ViewItemBuilder command(@Nonnull String command){
        this.command = command;
        return this;
    }

    public ViewItemBuilder lore(@Nonnull String lore){
        this.lore.add(lore.replace("&", "§"));
        return this;
    }

    public ViewItemBuilder flags(@Nonnull ItemFlag... itemFlags){
        this.itemFlags = itemFlags;
        return this;
    }

    public ViewItemBuilder amount(int amount){
        this.amount = amount;
        return this;
    }

    public ViewItemBuilder model(int model){
        this.model = model;
        return this;
    }

    @Override
    public ItemStack build(){
        ItemStack item = new ItemStack(material == null ? Material.STONE : material);

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item ;

        if (displayName != null) meta.setDisplayName(displayName);
        if (localName != null) meta.setLocalizedName(localName);

        if (lore.size() > 0) meta.setLore(lore);
        if (itemFlags != null && itemFlags.length > 0) meta.addItemFlags(itemFlags);
        if (model != -1){
            meta.setCustomModelData(model);
        }

        item.setItemMeta(meta);
        item.setAmount(amount);

        NBTItem nbtItem = new NBTItem(item, true);

        if (command != null) {
            nbtItem.setInteger(ViewItem.NBT_ACTION_KEY, ViewItem.ACTION_PLAYER_COMMAND);
            nbtItem.setString(ViewItem.NBT_VALUE_KEY, command);
        }else {
            nbtItem.setInteger(ViewItem.NBT_ACTION_KEY, action);
            nbtItem.setString(ViewItem.NBT_VALUE_KEY, value);
        }

        return item;
    }

}
