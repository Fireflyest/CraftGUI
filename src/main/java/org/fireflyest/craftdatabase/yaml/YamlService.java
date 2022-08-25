package org.fireflyest.craftdatabase.yaml;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Fireflyest
 * @since 2022/8/23
 */
public abstract class YamlService {

    protected static final String LANGUAGE_FOLDER = "lang";
    protected JavaPlugin plugin;
    protected File dataFolder;
    protected FileConfiguration config;
    protected FileConfiguration lang;

    public YamlService(@Nonnull JavaPlugin plugin) {
        this.plugin = plugin;
        this.dataFolder = plugin.getDataFolder();
        // TODO: 2022/8/23 该类未测试
    }

    /**
     * 加载配置文件
     * @param aClass 配置类
     */
    public void setupConfig(Class<?> aClass){
        this.config = loadYamlFile("config");
        this.setupClass(config, aClass);
    }

    /**
     * 加载语言文件
     * @param aClass 语言类
     * @param local 语言
     */
    public void setupLanguage(Class<?> aClass, @Nullable String local){
        this.lang = loadYamlFile(LANGUAGE_FOLDER + "/" + (local == null ? "default" : local));
        this.setupClass(lang, aClass);
    }


    /**
     * 保存配置数据
     * @param key 据键值
     * @param value 数据值
     */
    public void setConfigData(String key, Object value) {
//        if (config == null) {
//            return;
//        }
//        config.set(key, value);
//        File file = new File(dataFolder, "config.yml");
//
//        try {
//            config.save(file);
//        } catch (IOException e) {
//            Bukkit.getServer().getLogger().severe(String.format("无法保存数据 %s!", "config.yml"));
//        }
    }


    /**
     * 用反射给类的静态数据赋值
     * @param yamlFile 数据文件
     * @param aClass 类
     */
    protected void setupClass(FileConfiguration yamlFile, Class<?> aClass){
        for (Field field : aClass.getDeclaredFields()){
            if("instance".equals(field.getName())) continue;
            String key = this.toLowerCase(field.getName());
            String type = this.toFirstUpCase(field.getType().getSimpleName());
            Method method = null;
            try {
                method = FileConfiguration.class.getMethod("get" + type, String.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            if(method == null) continue;
            try {
                field.set(null, method.invoke(yamlFile, key));
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 加载一个配置文件
     * @param fileName 不带后缀文件名
     * @return FileConfiguration
     */
    protected FileConfiguration loadYamlFile(String fileName) {
        File file = new File(dataFolder, fileName+".yml");
        if (!file.exists()) plugin.saveResource(fileName+".yml", false);
        return YamlConfiguration.loadConfiguration(file);
    }

    /**
     * TEST_TEST转换为TestTest
     * @param str 文本
     * @return 拼接
     */
    protected String toLowerCase(String str){
        StringBuilder sb = new StringBuilder();
        for(String word: str.split("_")){
            sb.append(word.charAt(0))
                    .append(word.substring(1).toLowerCase());
        }
        return sb.toString();
    }

    /**
     * test转换为Test
     * @param str 文本
     * @return 首字母大写
     */
    protected String toFirstUpCase(String str){
        return Character.toUpperCase(str.charAt(0)) + str.substring(1).toLowerCase();
    }

}
