package org.fireflyest.craftgui.lang;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class MaterialRussian implements MaterialName{

    private boolean enable = false;

    private final Map<String, String> translateMap = new HashMap<String, String>();

    private static final MaterialRussian instance = new MaterialRussian();

    private MaterialRussian(){
    }

    public static MaterialRussian getInstance() {
        return instance;
    }

    @Override
    public void enable(){
        if (enable) return;
        //<editor-fold defaultstate="collapsed" desc="translate">

        //</editor-fold>
        enable = true;
    }

    @Override
    public String translate(Material material){
        String type = material.name();
        if(translateMap.containsKey(type)){
            return translateMap.get(type);
        }else {
            return type.toLowerCase();
        }
    }

}
