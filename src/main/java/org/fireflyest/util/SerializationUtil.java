package org.fireflyest.util;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

/**
 * 物品析构与解析
 * @author Fireflyest
 * @since 1.1.6
 */
public class SerializationUtil {

    private static final String DATA_PATH = "root";

    // 缓存解析
    private static final Map<String, ItemStack> stackCrashMap = new HashMap<>();

    private SerializationUtil() {
    }

    /**
     * 序列化
     * @param configurationSerializable 可序列化对象
     * @return 文本数据
     */
    public static String serialize(ConfigurationSerializable configurationSerializable) {
        YamlConfiguration yaml = new YamlConfiguration();
        yaml.set(DATA_PATH, configurationSerializable);
        return yaml.saveToString();
    }

    /**
     * 反序列化
     * @param <T> 可序列化泛型
     * @param data 文本数据
     * @param clazz 可序列化对象的类
     * @return 可序列化对象
     */
    public static <T extends ConfigurationSerializable> T deserialize(String data, Class<T> clazz) {
        YamlConfiguration yaml = new YamlConfiguration();
        try {
            yaml.loadFromString(data);
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return yaml.getSerializable(DATA_PATH, clazz);
    }

    /**
     * 序列化物品
     * @param itemStack 物品
     * @return 文本数据
     */
    public static String serializeItemStack(ItemStack itemStack) {
        String data = serialize(itemStack);
        stackCrashMap.put(data, itemStack.clone());
        return data;
    }

    /**
     * 反序列化物品
     * @param stackData 物品数据
     * @return 物品
     */
    public static ItemStack deserializeItemStack(String stackData) {
        // 解析物品堆
        ItemStack stack;
        if (stackCrashMap.containsKey(stackData)) {
            stack = stackCrashMap.get(stackData).clone();
        } else {
            stack = deserialize(stackData, ItemStack.class);
            stackCrashMap.put(stackData, stack.clone());
        }
        return stack;
    }

}
