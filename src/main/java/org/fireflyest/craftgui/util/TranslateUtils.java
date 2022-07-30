package org.fireflyest.craftgui.util;

import org.bukkit.Material;
import org.fireflyest.craftgui.lang.MaterialChinese;

import java.util.Locale;

public class TranslateUtils {

    private static String language = Locale.getDefault().toLanguageTag();

    private TranslateUtils(){
    }

    /**
     * 设置国家和地区
     * @param locale 语言地区
     */
    public static void setLocale(Locale locale) {
        TranslateUtils.language = locale.toLanguageTag();
    }

    /**
     * 设置语言
     * @param language 语言
     */
    public static void setLanguage(String language) {
        TranslateUtils.language = language;
    }

    /**
     * 获取材料本地名称
     * @param material 材料
     * @return 名称
     */
    public static String translate(Material material){
        if ("zh-CN".equals(language)) {
            return MaterialChinese.translate(material);
        }
        return material.name().toLowerCase(Locale.ROOT);
    }

}
