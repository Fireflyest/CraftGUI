package org.fireflyest.craftgui.view;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.fireflyest.craftgui.api.ViewPage;

<<<<<<< HEAD
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

=======
>>>>>>> 9d73041 (更新页模板)
/**
 * 模板页面
 * @author Fireflyest
 */
public abstract class TemplatePage implements ViewPage {

    // 固定按钮，快速显示
    protected final Map<Integer, ItemStack> itemMap = new ConcurrentHashMap<>();
    // 全部按钮的缓存，点击时返回被点击物品
    protected Map<Integer, ItemStack> crashMap = new ConcurrentHashMap<>();

    // 界面载体
    protected Inventory inventory;
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
        this.target = target;
        this.page = page;
        this.size = size;

        // 界面容器
        this.updateTitle(title);
        // 界面固定按钮
        this.refreshPage();
    }

    @Nonnull
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public @Nonnull Map<Integer, ItemStack> getButtonMap() {
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
    public void updateTitle(String title) {
        inventory = Bukkit.createInventory(null, size, title);
    }

}
