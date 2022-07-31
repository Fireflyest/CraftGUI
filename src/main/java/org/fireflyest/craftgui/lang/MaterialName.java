package org.fireflyest.craftgui.lang;

import org.bukkit.Material;

/**
 * @author Fireflyest
 * @since 2022/7/31
 */
public interface MaterialName {

    void enable();

    String translate(Material material);
}
