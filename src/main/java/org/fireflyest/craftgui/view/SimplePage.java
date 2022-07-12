package org.fireflyest.craftgui.view;

import org.fireflyest.craftgui.api.ViewPage;
import org.fireflyest.craftgui.util.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Fireflyest
 * 2022/2/15 0:00
 */
public class SimplePage implements ViewPage {

    private final Map<Integer, ItemStack> itemMap = new ConcurrentHashMap<>();

    private final Inventory inventory;
    private final String title;
    private final String target;
    private final int page;
    private final int size;

    private ViewPage next = null;
    private ViewPage pre = null;

    public SimplePage(String title, String target, int page, int size) {
        this.title = title;
        this.target = target;
        this.page = page;
        this.size = size;
        String guiTitle = title;

        if (target != null)  guiTitle += ("§9" + target);    // 副标题
        if (page != 0) guiTitle += (" §7#§8" + page);          // 给标题加上页码

        // 界面容器
        this.inventory = Bukkit.createInventory(null, size, guiTitle);

        this.refreshPage();
    }

    @NotNull
    @Override
    public Inventory getInventory(){
        return inventory;
    }

    @Override
    public @NotNull Map<Integer, ItemStack> getItemMap(){
        Map<Integer, ItemStack> itemStackMap = new ConcurrentHashMap<>(itemMap);
        // 这里是需要异步加载的按钮
        // 例如需要先读取数据再放置的按钮
        itemStackMap.put(0, new ItemStack(Material.STONE));
        return itemStackMap;
    }

    @Override
    public @NotNull Map<Integer, ItemStack> getButtonMap() {
        // 这里是固定按钮
        // 打开容器时会先显示
        return new ConcurrentHashMap<>(itemMap);
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
        if(next == null && page < 30){
            next = new SimplePage(title, target, page+1, size);
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
        ItemStack close = new ItemStack(Material.BARRIER);
        ItemUtils.setDisplayName(close, "§c关闭");
        ItemUtils.addLore(close, "§f点击关闭界面");

        itemMap.put(8, close);
    }

}
