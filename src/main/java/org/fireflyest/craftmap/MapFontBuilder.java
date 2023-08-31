package org.fireflyest.craftmap;

import org.bukkit.map.MapFont;

public class MapFontBuilder {
    
    private final MapFont font;

    public MapFontBuilder() {
        font = new MapFont();
    }

    public MapFont build() {
        return font;
    }
    

}
