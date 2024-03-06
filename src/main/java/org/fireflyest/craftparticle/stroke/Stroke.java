package org.fireflyest.craftparticle.stroke;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.World;
import org.fireflyest.craftparticle.Brush;
import org.fireflyest.craftparticle.DynamicLocation;

public abstract class Stroke<T> {
    
    protected final List<Location> cache = new ArrayList<>();
    protected final Brush<T> brush;
    protected final DynamicLocation dLocation;
    protected int maxTime;
    protected int drawSpacing;
    protected int count;
    protected boolean constant;
    
    protected Stroke(@Nonnull Brush<T> brush, @Nonnull DynamicLocation dLocation, int maxTime, int drawSpacing) {
        this.brush = brush;
        this.dLocation = dLocation;
        this.maxTime = maxTime;
        this.drawSpacing = drawSpacing;
    }

    protected Stroke(@Nonnull Brush<T> brush, @Nonnull DynamicLocation dLocation, int maxTime) {
        this(brush, dLocation, maxTime, 5);
    }

    public boolean draw() {
        if (this.canDraw()) {
            if (constant && !cache.isEmpty()) {
                this.drawCache();
            } else {
                cache.clear();
                this.realtimeDraw();
            }
        }
        return maxTime >= 0;
    }

    public abstract void realtimeDraw();

    public void stop() {
        maxTime = 0;
    }

    protected boolean canDraw() {
        return count++ % drawSpacing == 0;
    }
    
    protected void drawCache() {
        for (Location location : cache) {
            World world = location.getWorld();
            world.spawnParticle(brush.getParticle(), 
                location, 
                brush.getCount(), 
                brush.getOffsetX(), 
                brush.getOffsetY(), 
                brush.getOffsetZ(), 
                brush.getExtra(), 
                brush.getData());
        }
    }

    public void setConstant(boolean constant) {
        this.constant = constant;
    }

}
