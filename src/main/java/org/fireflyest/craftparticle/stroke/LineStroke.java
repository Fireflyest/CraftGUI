package org.fireflyest.craftparticle.stroke;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.fireflyest.craftparticle.DynamicLocation;
import org.fireflyest.craftparticle.DynamicVector;

public class LineStroke<T> extends Stroke<T> {

    protected final List<Location> cache = new ArrayList<>();
    protected final DynamicVector dVector;
    protected int time;
    protected boolean constant;

    public LineStroke(Brush<T> brush, DynamicLocation dLocation, int maxTime, int drawSpacing, DynamicVector dVector) {
        super(brush, dLocation, maxTime, drawSpacing);
        this.dVector = dVector;
    }

    public LineStroke(Brush<T> brush, DynamicLocation dLocation, int maxTime, DynamicVector dVector) {
        super(brush, dLocation, maxTime);
        this.dVector = dVector;
    }

    @Override
    public boolean draw() {
        if (this.canDraw()) {
            if (constant && !cache.isEmpty()) {
                this.drawCache();
            } else {
                Vector vector = dVector.getVector(time++);
                cache.clear();
                ParticleUtils.line(brush, constant ? null : cache, dLocation.getLocation(), vector);
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
