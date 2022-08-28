package org.fireflyest.craftgui.lang;

import org.bukkit.Material;

/**
 * 把材料名称翻译成各种语言
 * @author Fireflyest
 * @since 2022/7/31
 */
public interface MaterialName {

    void enable();

    String translate(Material material);
}
