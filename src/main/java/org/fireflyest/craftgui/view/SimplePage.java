package org.fireflyest.craftgui.view;

import java.util.Map;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.fireflyest.craftgui.api.ViewPage;
import org.fireflyest.craftgui.button.ButtonItemBuilder;

/**
 * @author Fireflyest
 */
public class SimplePage extends TemplatePage {

    protected int maxPage;

    /**
     * 示例页面
     */
    public SimplePage(String title, String target, int page, int size, int maxPage) {
        super(title, target, page, size);
        this.maxPage = maxPage;
    }

    @Override
    public @Nonnull Map<Integer, ItemStack> getItemMap() {
        crashMap.clear();
        crashMap.putAll(itemMap);

        return crashMap;
    }

    @Override
    public ViewPage getNext() {
        if (next == null && page < maxPage) {
            next = new SimplePage(title, target, page + 1, size, maxPage);
            next.setPre(this);
        }
        return next;
    }

    @Override
    public void refreshPage() {
        
    }

}
