package org.fireflyest.util;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * 物品析构与解析
 * @author Fireflyest
 * @since 2022/8/21
 */
public class SerializationUtil {

    private static final String DATA_PATH = "root";

    // 缓存解析
    public static final Map<String, ItemStack> stackCrashMap = new HashMap<>();

    private SerializationUtil() {
    }

    public static String serialize(ConfigurationSerializable configurationSerializable){
        YamlConfiguration yaml = new YamlConfiguration();
        yaml.set(DATA_PATH, configurationSerializable);
        return yaml.saveToString();
    }

    public static <T extends ConfigurationSerializable> T deserialize(String data, Class<T>tClass){
        YamlConfiguration yaml = new YamlConfiguration();
        try {
            yaml.loadFromString(data);
        } catch (InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
        return yaml.getSerializable(DATA_PATH, tClass);
    }

    public static ItemStack deserializeItemStack(String stackData){
        // 解析物品堆
        ItemStack stack;
        if (stackCrashMap.containsKey(stackData)) {
            stack = stackCrashMap.get(stackData).clone();
        }else {
            stack = deserialize(stackData, ItemStack.class);
            stackCrashMap.put(stackData, stack.clone());
        }
        return stack;
    }

    public static String serializeItemStack(ItemStack itemStack){
        String data = serialize(itemStack);
        stackCrashMap.put(data, itemStack.clone());
        return data;
    }

}
