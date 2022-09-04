package org.fireflyest.craftgui.view;

import org.fireflyest.craftgui.api.ViewPage;
import org.fireflyest.craftgui.button.ButtonItemBuilder;
import org.fireflyest.craftgui.item.ViewItem;
import org.fireflyest.craftgui.item.ViewItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * @author Fireflyest
 * 2022/2/15 0:00
 */
public class SimplePage extends TemplatePage {

    public SimplePage(String pluginName, String target, int page, int size) {
        super(pluginName, target, page, size);
    }

    @Override
    public @Nonnull Map<Integer, ItemStack> getItemMap(){
        crashMap.clear();
        crashMap.putAll(itemMap);

        // 这里是需要异步加载的按钮
        // 例如需要先读取数据再放置的按钮
        for (int i = 0; i < 9; i++) {
            ItemStack f = new ButtonItemBuilder(Material.STONE)
                    .actionOpenPage(String.format("craftgui.simple.F%s", i))
                    .name(String.format("F%s", i))
                    .build();
            crashMap.put(i, f);
        }

        return crashMap;
    }

    @Override
    public ViewPage getNext() {
        if(next == null && page < 7){
            next = new SimplePage(pluginName, target, page+1, size);
            next.setPre(this);
        }
        return next;
    }

    @Override
    public void refreshPage() {
        for (int i = 9; i < 18; i++) {
            ItemStack blankButton = new ButtonItemBuilder(Material.WHITE_STAINED_GLASS_PANE)
                    .name(" ")
                    .build();
            itemMap.put(i, blankButton);
        }
        // 跳转翻页
        for (int i = -2; i < 3; i++) {
            int p = page + i;
            if (p < 0 || p > 7) continue;
            ItemStack pageButton = new ButtonItemBuilder(p == page ? Material.MAP : Material.PAPER)
                    .actionPageJump(p)
                    .name(String.valueOf(p))
                    .build();
            itemMap.put(20+i, pageButton);
        }
        // 左右翻页
        ItemStack preButton = new ButtonItemBuilder(Material.GRAY_DYE)
                .actionPagePre()
                .name("pre")
                .build();
        itemMap.put(23, preButton);
        ItemStack nextButton = new ButtonItemBuilder(Material.GRAY_DYE)
                .actionPageNext()
                .name("next")
                .build();
        itemMap.put(24, nextButton);
        // 返回
        ItemStack backButton = new ButtonItemBuilder(Material.REDSTONE)
                .actionBack()
                .name("§c返回")
                .lore("§f上一页")
                .build();
        itemMap.put(25, backButton);
        // 关闭
        ItemStack closeButton = new ButtonItemBuilder(Material.REDSTONE)
                .actionClose()
                .name("$<colors=#feabac:#9283fe>关闭关闭关闭关$<colors=#feabac:#9283fe>闭aaaaaaaaaaaaaaaa")
                .lore("$<colors=#feabac:#9283fe>关闭界面关闭界面关闭界面关闭界面")
                .lore("$<color=#9283fe>关闭界面关闭界面关闭界面关闭界面")
                .lore("§m$<color=#9283fe>关闭界面关闭界面关闭界面关闭界面")
                .colorful()
                .build();
        itemMap.put(26, closeButton);
    }

}
