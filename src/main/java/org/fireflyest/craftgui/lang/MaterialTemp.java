package org.fireflyest.craftgui.lang;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class MaterialTemp implements MaterialName{

    private boolean enable = false;

    private final Map<String, String> translateMap = new HashMap<String, String>();

    private static final MaterialTemp instance = new MaterialTemp();

    private MaterialTemp(){
    }

    public static MaterialTemp getInstance() {
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
