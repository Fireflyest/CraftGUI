package org.fireflyest.craftgui.view;

import org.fireflyest.craftgui.api.ViewPage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.fireflyest.craftgui.item.ViewItemBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Fireflyest
 * 2022/2/16 17:24
 */

public class ErrorPage implements ViewPage {

    // 界面载体
    private Inventory inventory;

    // 固定按钮物品位置
    private final Map<Integer, ItemStack> itemMap = new HashMap<>();

    public ErrorPage(String pluginName) {
        // 新建容器
        this.updateTitle(pluginName);
        // 添加固定按钮
        this.refreshPage();
    }

    @Override
    public @NotNull Map<Integer, ItemStack> getItemMap() {
        Map<Integer, ItemStack> itemStackMap = new HashMap<>(itemMap);
        itemStackMap.put(0, new ItemStack(Material.STONE));
        return itemStackMap;
    }

    @Override
    public @NotNull Map<Integer, ItemStack> getButtonMap() {
        return new HashMap<>(itemMap);
    }

    @Override
    @NotNull
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public String getTarget() {
        return null;
    }

    @Override
    public int getPage() {
        return 0;
    }

    @Override
    public ViewPage getNext() {
        return null;
    }

    @Override
    public ViewPage getPre() {
        return null;
    }

    @Override
    public void setPre(ViewPage pre) {
    }

    @Override
    public void setNext(ViewPage pre) {
    }

    @Override
    public void refreshPage() {
        ItemStack close = new ViewItemBuilder(Material.REDSTONE)
                .name("§c关闭")
                .lore("§f按§3ESC§f关闭界面")
                .build();
        itemMap.put(8, close);
    }

    @Override
    public void updateTitle(String title) {
        inventory = Bukkit.createInventory(null, 9, String.format("%s > §cNON_FOUND", title));
    }
}
