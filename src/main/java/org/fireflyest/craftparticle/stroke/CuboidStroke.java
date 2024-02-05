package org.fireflyest.craftparticle.stroke;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.fireflyest.craftparticle.Brush;
import org.fireflyest.craftparticle.DynamicLocation;

public class CuboidStroke<T> extends Stroke<T> {

    protected final List<Location> cache = new ArrayList<>();
    protected final DynamicLocation dLocation2;
    protected int time;
    protected boolean constant;

    public CuboidStroke(Brush<T> brush, DynamicLocation dLocation, int maxTime, DynamicLocation dLocation2) {
        super(brush, dLocation, maxTime);
        this.dLocation2 = dLocation2;
    }

    public CuboidStroke(Brush<T> brush, DynamicLocation dLocation, int maxTime, int drawSpacing, DynamicLocation dLocation2) {
        super(brush, dLocation, maxTime, drawSpacing);
        this.dLocation2 = dLocation2;
    }

    @Override
    public boolean draw() {
        if (this.canDraw()) {
            if (constant && !cache.isEmpty()) {
                this.drawCache();
            } else {
                cache.clear();
                ParticleUtils.cuboid(brush, constant ? null : cache, dLocation.getLocation(), dLocation2.getLocation());
            }
        }
        return maxTime >= 0;
    }

    public void setConstant(boolean constant) {
        this.constant = constant;
    }
    
    private void drawCache() {
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
    
}
