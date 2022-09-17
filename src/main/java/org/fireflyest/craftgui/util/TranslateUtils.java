package org.fireflyest.craftgui.util;

import org.bukkit.Material;
import org.fireflyest.craftgui.lang.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 材料翻译
 */
public class TranslateUtils {

    private static final Map<String, MaterialName> langMap = new HashMap<>();
    private static String language = Locale.getDefault().toLanguageTag();

    static{
        langMap.put("zh", MaterialChinese.getInstance()); // 简体中文
        langMap.put("zh-CN", MaterialChinese.getInstance()); // 简体中文
        langMap.put("zh-TW", MaterialChineseF.getInstance()); // 繁体中文
        langMap.put("zh-HK", MaterialChineseF.getInstance()); // 繁体中文
        langMap.put("de", MaterialGerman.getInstance()); // 德语 German
        langMap.put("de-DE", MaterialGerman.getInstance()); // 德语 German
        langMap.put("fr", MaterialFrench.getInstance()); // 法语 French
        langMap.put("fr-FR", MaterialFrench.getInstance()); // 法语 French
        langMap.put("ja", MaterialJapanese.getInstance()); // 日语 Japanese
        langMap.put("ja-JA", MaterialJapanese.getInstance()); // 日语 Japanese
        langMap.put("ru", MaterialRussian.getInstance()); // 俄语 Russian
        langMap.put("ru-RU", MaterialRussian.getInstance()); // 俄语 Russian
    }

    private TranslateUtils(){
        // 工具类
    }

    /**
     * 设置国家和地区
     * @param locale 语言地区
     */
    public static void setLocale(Locale locale) {
        setLanguage(locale.toLanguageTag());
    }

    /**
     * 设置语言
     * @param language 语言
     */
    public static void setLanguage(String language) {
        TranslateUtils.language = language;
        // 初始化
        if (langMap.containsKey(language)) langMap.get(language).enable();
    }

    /**
     * 获取材料本地名称
     * @param material 材料
     * @return 名称
     */
    public static String translate(Material material){
        if (langMap.containsKey(language)) {
            return langMap.get(language).translate(material);
        }
        return material.name().toLowerCase();
    }

}
