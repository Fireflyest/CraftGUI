package org.fireflyest.craftgui.view;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.fireflyest.craftgui.api.ViewPage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 模板页面
 * @author Fireflyest
 * 2022/2/15 0:00
 */
public abstract class TemplatePage implements ViewPage {

    // 固定按钮，快速显示
    protected final Map<Integer, ItemStack> itemMap = new ConcurrentHashMap<>();
    // 全部按钮的缓存，点击时返回被点击物品
    protected Map<Integer, ItemStack> crashMap = new ConcurrentHashMap<>();

    // 界面载体
    protected Inventory inventory;
    // 插件名称，用来判断界面由谁处理
    protected final String pluginName;
    // 目标
    protected final String target;
    // 页码
    protected final int page;
    // 容器大小
    protected final int size;

    protected ViewPage next = null;
    protected ViewPage pre = null;

    public TemplatePage(String pluginName, String target, int page, int size) {
        this.pluginName = pluginName;
        this.target = target;
        this.page = page;
        this.size = size;

        // 标题
        String guiTitle = pluginName;
        if (target != null)  guiTitle += ("§9" + target);
        if (page != -1) guiTitle += (" §7#§8" + page);

        // 界面容器
        this.updateTitle(guiTitle);
        // 界面固定按钮
        this.refreshPage();
    }

    @Nonnull
    @Override
    public Inventory getInventory(){
        return inventory;
    }

    /**
     * 由异步线程调用
     * @return 容器物品
     */
    @Override
    public abstract @Nonnull Map<Integer, ItemStack> getItemMap();

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

    /**
     * 可能异步
     */
    @Override
    public abstract void refreshPage();

    @Override
    public void updateTitle(String title) {
        // 下次启动容器才能发现不同，如果需要实时更新，需要强制让玩家重新打开界面
        inventory = Bukkit.createInventory(null, size, title);
    }

}
