package org.fireflyest.craftgui.view;

import org.fireflyest.craftgui.api.ViewPage;
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
            ItemStack f = new ViewItemBuilder(Material.STONE)
                    .name(String.format("F%s", i))
                    .action(ViewItem.ACTION_OPEN)
                    .value(String.format("F%s", i))
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
            ItemStack blankButton = new ViewItemBuilder(Material.WHITE_STAINED_GLASS_PANE)
                    .name(" ")
                    .build();
            itemMap.put(i, blankButton);
        }
        // 跳转翻页
        for (int i = -2; i < 3; i++) {
            int p = page + i;
            if (p < 0 || p > 7) continue;
            ItemStack pageButton = new ViewItemBuilder(p == page ? Material.MAP : Material.PAPER)
                    .name(String.valueOf(p))
                    .action(ViewItem.ACTION_PAGE)
                    .value(String.valueOf(p))
                    .build();
            itemMap.put(20+i, pageButton);
        }
        // 左右翻页
        ItemStack preButton = new ViewItemBuilder(Material.GRAY_DYE)
                .name("pre")
                .action(ViewItem.ACTION_PAGE)
                .value("pre")
                .build();
        itemMap.put(23, preButton);
        ItemStack nextButton = new ViewItemBuilder(Material.GRAY_DYE)
                .name("next")
                .action(ViewItem.ACTION_PAGE)
                .value("next")
                .build();
        itemMap.put(24, nextButton);
        // 返回
        ItemStack backButton = new ViewItemBuilder(Material.REDSTONE)
                .name("§c返回")
                .lore("§f上一页")
                .action(ViewItem.ACTION_BACK)
                .build();
        itemMap.put(25, backButton);
        // 关闭
        ItemStack closeButton = new ViewItemBuilder(Material.REDSTONE)
                .name("§c关闭")
                .lore("§f关闭界面")
                .action(ViewItem.ACTION_CLOSE)
                .build();
        itemMap.put(26, closeButton);
    }

}
