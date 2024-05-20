package org.fireflyest.craftparticle.stroke;

import org.bukkit.Location;
import org.bukkit.World;
import org.fireflyest.craftparticle.Brush;
import org.fireflyest.craftparticle.DynamicLocation;

/**
 * 点
 * @author Fireflyest
 * @since 1.2
 */
public class PointStroke<T> extends Stroke<T> {

    protected PointStroke(Brush<T> brush, DynamicLocation dLocation, int maxTime, int drawSpacing) {
        super(brush, dLocation, maxTime, drawSpacing);
    }

    public PointStroke(Brush<T> brush, DynamicLocation dLocation, int maxTime) {
        super(brush, dLocation, maxTime);
    }

    @Override
    public void realtimeDraw() {
        Location location = dLocation.getLocation();
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
