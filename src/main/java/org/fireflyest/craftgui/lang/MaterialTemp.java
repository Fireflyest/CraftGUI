package org.fireflyest.craftgui.lang;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MaterialTemp {

    private static final Map<String, String> translateMap = new HashMap<String, String>(){
        {
            //<editor-fold defaultstate="collapsed" desc="translate">
            //</editor-fold>
        }
    };

    private MaterialTemp(){
    }

    public static String translate(Material material){
        String type = material.name();
        if(translateMap.containsKey(type)){
            return translateMap.get(type);
        }else {
            return type.replace("_", " ").toLowerCase(Locale.ROOT);
        }
    }

}
