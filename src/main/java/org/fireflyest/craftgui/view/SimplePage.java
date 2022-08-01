package org.fireflyest.craftgui.view;

import org.fireflyest.craftgui.api.ViewPage;
import org.fireflyest.craftgui.item.ViewItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Fireflyest
 * 2022/2/15 0:00
 */
public class SimplePage implements ViewPage {

    protected final Map<Integer, ItemStack> itemMap = new HashMap<>();
    protected Map<Integer, ItemStack> crashMap = new HashMap<>();

    protected Inventory inventory;
    protected final String pluginName;
    protected final String target;
    protected final int page;
    protected final int size;

    protected ViewPage next = null;
    protected ViewPage pre = null;

    public SimplePage(String pluginName, String target, int page, int size) {
        this.pluginName = pluginName;
        this.target = target;
        this.page = page;
        this.size = size;

        // 标题
        String guiTitle = pluginName;
        if (target != null)  guiTitle += ("§9" + target);
        if (page != 0) guiTitle += (" §7#§8" + page);

        // 界面容器
        this.updateTitle(guiTitle);
        // 界面固定按钮
        this.refreshPage();
    }

    @NotNull
    @Override
    public Inventory getInventory(){
        return inventory;
    }

    @Override
    public @NotNull Map<Integer, ItemStack> getItemMap(){
        crashMap.clear();
        crashMap.putAll(itemMap);

        // 这里是需要异步加载的按钮
        // 例如需要先读取数据再放置的按钮
        crashMap.put(0, new ItemStack(Material.STONE));

        return crashMap;
    }

    @Override
    public @NotNull Map<Integer, ItemStack> getButtonMap() {
        // 这里是固定按钮
        // 打开容器时会先显示
        return new HashMap<>(itemMap);
    }

    @Override
    public @Nullable ItemStack getItem(int slot) {
        return crashMap.get(slot);
    }

    @Override
    public String getTarget() {
        return target;
    }

    @Override
    public int getPage() {
        return page;
    }

    @Override
    public ViewPage getNext() {
        if(next == null && page < 3){
            next = new SimplePage(pluginName, target, page+1, size);
            next.setPre(this);
        }
        return next;
    }

    @Override
    public ViewPage getPre() {
        return pre;
    }

    @Override
    public void setPre(ViewPage pre) {
        this.pre = pre;
    }

    @Override
    public void setNext(ViewPage next) {
        this.next = next;
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
        inventory = Bukkit.createInventory(null, size, title);
    }

}
