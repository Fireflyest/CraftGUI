package org.fireflyest.craftgui.view;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.fireflyest.craftgui.api.ViewPage;

/**
 * 模板页面
 * @author Fireflyest
 */
public abstract class TemplatePage implements ViewPage {

    // 固定按钮，快速显示
    protected final Map<Integer, ItemStack> buttonMap = new ConcurrentHashMap<>();
    // 全部按钮的缓存，点击时返回被点击物品
    protected Map<Integer, ItemStack> asyncButtonMap = new ConcurrentHashMap<>();

    // 界面载体
    protected Inventory inventory;
    // 标题
    protected String title;
    // 目标
    protected final String target;
    // 页码
    protected final int page;
    // 容器大小
    protected final int size;

    protected ViewPage next = null;
    protected ViewPage pre = null;

    /**
     * 新建一个页面
     * @param title 标题
     * @param target 归属
     * @param page 页码
     * @param size 大小
     */
    protected TemplatePage(String title, String target, int page, int size) {
        this.title = title;
        this.target = target;
        this.page = page;
        this.size = size;

        // 界面容器
        String guiTitle = title.replace("%page%", String.valueOf(page)).replace("%target%", String.valueOf(page));
        this.inventory = Bukkit.createInventory(null, size, guiTitle);
    }

    @Override
    public @Nonnull Inventory getInventory() {
        return inventory;
    }

    @Override
    public @Nonnull Map<Integer, ItemStack> getButtonMap() {
        return buttonMap;
    }

    @Override
    public @Nullable ItemStack getItem(int slot) {
        return asyncButtonMap.get(slot);
    }

    @Override
    public @Nullable String getTarget() {
        return target;
    }

    @Override
    public int getPage() {
        return page;
    }

    @Override
    public @Nullable ViewPage getNext() {
        return next;
    }

    @Override
    public @Nullable ViewPage getPre() {
        return pre;
    }

    @Override
    public void setPre(@Nullable ViewPage pre) {
        this.pre = pre;
    }

    @Override
    public void setNext(@Nullable ViewPage next) {
        this.next = next;
    }

    @Override
    public void updateTitle(String title) {
        this.title = title;
        inventory = Bukkit.createInventory(null, size, title);
    }

}
