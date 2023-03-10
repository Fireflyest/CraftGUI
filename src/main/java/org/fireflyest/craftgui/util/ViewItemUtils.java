package org.fireflyest.craftgui.util;

import com.cryptomorin.xseries.XMaterial;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.NumberConversions;
import org.fireflyest.craftgui.button.ButtonAction;

import javax.annotation.Nonnull;

/**
 * 界面物品工具类
 * @author Fireflyest
 */
public class ViewItemUtils {

    private ViewItemUtils(){
    }

    public static void setItemValue(@Nonnull ItemStack item, String value) {
        if (item.getType() == XMaterial.AIR.parseMaterial()) return;
        NBTItem nbtItem = new NBTItem(item, true);
        nbtItem.setString(ButtonAction.NBT_VALUE_KEY, value);
    }

    public static String getItemValue(@Nonnull ItemStack item) {
        if (item.getType() == XMaterial.AIR.parseMaterial()) return null;
        NBTItem nbtItem = new NBTItem(item);
        String value = nbtItem.getString(ButtonAction.NBT_VALUE_KEY);
        if (value.length() > 2) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }

    public static void setItemAction(@Nonnull ItemStack item, int action) {
        if (item.getType() == XMaterial.AIR.parseMaterial()) return;
        NBTItem nbtItem = new NBTItem(item, true);
        nbtItem.setInteger(ButtonAction.NBT_ACTION_KEY, action);
    }

    public static int getItemAction(@Nonnull ItemStack item) {
        if (item.getType() == XMaterial.AIR.parseMaterial()) return 0;
        NBTItem nbtItem = new NBTItem(item);
        return NumberConversions.toInt(nbtItem.getString(ButtonAction.NBT_ACTION_KEY));
    }

}
