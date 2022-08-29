package org.fireflyest.craftgui.util;

import org.bukkit.inventory.ItemStack;
import org.fireflyest.util.SerializationUtil;

@Deprecated
public class SerializeUtil {

    private SerializeUtil(){
    }

    public static void setDebug(boolean debug) {
        SerializeCustomUtil.setDebug(debug);
    }

    /**
     * 析构堆
     * @param itemStack 物品
     * @return 堆析构后的文本
     */
    public static String serializeItemStack(ItemStack itemStack) {
        return "data=" + itemStack.getType().name();
    }

    /**
     * 析构元
     * @param itemStack 物品
     * @return 元析构后的文本
     */
    public static String serializeItemMeta(ItemStack itemStack){
        return serializeItemMeta(itemStack, false);
    }

    public static String serializeItemMeta(ItemStack itemStack, boolean mysql){
        return SerializationUtil.serializeItemStack(itemStack);
    }
    /**
     * 解析物品
     * @param itemStack 堆
     * @param itemMeta 元
     * @return 物品
     */
    public static ItemStack deserialize(String itemStack, String itemMeta) {
        if (itemStack.startsWith("data")){
            return SerializationUtil.deserializeItemStack(itemMeta);
        }
        return SerializeCustomUtil.deserialize(itemStack, itemMeta);
    }

}
