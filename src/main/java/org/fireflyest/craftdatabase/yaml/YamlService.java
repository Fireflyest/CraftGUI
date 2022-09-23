package org.fireflyest.craftdatabase.yaml;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.fireflyest.util.ReflectionUtils;
import org.fireflyest.util.StringUtils;

/**
 * 配置数据服务
 * @author Fireflyest
 * @since 1.1.5
 */
public abstract class YamlService {

    protected static final String LANGUAGE_FOLDER = "lang";
    protected JavaPlugin plugin;
    protected File dataFolder;
    protected FileConfiguration config;
    protected FileConfiguration lang;

    /**
     * 配置文件操作
     * @param plugin 插件
     */
    protected YamlService(@Nonnull JavaPlugin plugin) {
        this.plugin = plugin;
        this.dataFolder = plugin.getDataFolder();
    }

    /**
     * 加载配置文件
     * @param clazz 配置类
     */
    protected void setupConfig(@Nonnull Class<?> clazz) {
        this.config = loadYamlFile("config");
        this.setupClass(config, clazz);
    }

    /**
     * 加载语言文件
     * @param clazz 语言类
     * @param local 语言
     */
    protected void setupLanguage(@Nonnull Class<?> clazz, @Nullable String local) {
        this.lang = loadYamlFile(LANGUAGE_FOLDER + "/" + (local == null ? "default" : local));
        this.setupClass(lang, clazz);
    }

    /**
     * 设置配置数据
     * @param key 据键值
     * @param value 数据值
     */
    protected void setConfigData(@Nonnull String key, Object value) {
        if (config == null) return;

        config.set(key, value);
        File file = new File(dataFolder, "config.yml");

        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().warning("The given file 'config.yml' cannot be written to for.");
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("File 'config.yml' don't exist.");
        }
    }


    /**
     * 用反射给类的静态数据赋值
     * @param yamlFile 数据文件
     * @param clazz 类
     */
    protected void setupClass(@Nonnull FileConfiguration yamlFile, @Nonnull Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            if ("instance".equals(field.getName())) continue;
            String key = StringUtils.toLowerCase(field.getName());
            String type = StringUtils.toFirstUpCase(field.getType().getSimpleName());
            try {
                Method method = FileConfiguration.class.getMethod("get" + type, String.class);
                Object value = method.invoke(yamlFile, key);
                ReflectionUtils.setField(field, null, value);
            } catch (NoSuchMethodException e) {
                plugin.getLogger().warning("No such method 'get" + type + "'");
            } catch (IllegalAccessException | InvocationTargetException  e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 加载一个配置文件
     * @param fileName 不带后缀文件名
     * @return FileConfiguration
     */
    protected FileConfiguration loadYamlFile(@Nonnull String fileName) {
        File file = new File(dataFolder, fileName + ".yml");
        if (!file.exists()) plugin.saveResource(fileName + ".yml", false);
        return YamlConfiguration.loadConfiguration(file);
    }

}
