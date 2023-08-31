package org.fireflyest.craftmap;

import org.bukkit.map.MapCanvas;

public class MapCanvasDrawer {
    
    private final MapCanvas canvas;

    public MapCanvasDrawer(MapCanvas canvas) {
        this.canvas = canvas;
    }

    /**
     * Bresenham算法
     * @param x0 起始x
     * @param y0 起始y
     * @param x1 终点x
     * @param y1 终点y
     * @param color 颜色
     * @return
     */
    public MapCanvasDrawer drawLine(int x0, int y0, int x1, int y1,  byte color) {
        int dx = Math.abs(x1 - x0), sx = x0 < x1 ? 1 : -1;
        int dy = Math.abs(y1 - y0), sy = y0 < y1 ? 1 : -1;
        int erro = (dx > dy ? dx : -dy) / 2;
     
        do {
            canvas.setPixel(x0, y0, color);
            int e2 = erro;
            if(e2 > -dx) { erro -= dy; x0 += sx;}
            if(e2 <  dy) { erro += dx; y0 += sy;}
        } while (x0 != x1 || y0 != y1);
        return this;
    }

}
