package org.fireflyest.craftparticle.stroke;

import org.fireflyest.craftparticle.Brush;
import org.fireflyest.craftparticle.DynamicLocation;

/**
 * 线组成的规则立方体
 * @author Fireflyest
 * @since 1.2
 */
public class CuboidStroke<T> extends Stroke<T> {

    protected final DynamicLocation dLocation2;
    protected int time;

    public CuboidStroke(Brush<T> brush, DynamicLocation dLocation, int maxTime, DynamicLocation dLocation2) {
        super(brush, dLocation, maxTime);
        this.dLocation2 = dLocation2;
    }

    public CuboidStroke(Brush<T> brush, DynamicLocation dLocation, int maxTime, int drawSpacing, DynamicLocation dLocation2) {
        super(brush, dLocation, maxTime, drawSpacing);
        this.dLocation2 = dLocation2;
    }

    @Override
    public void realtimeDraw() {
        ParticleUtils.cuboid(brush, constant ? null : cache, dLocation.getLocation(), dLocation2.getLocation());
    }

}
