package org.fireflyest.craftparticle.stroke;

import org.bukkit.util.Vector;
import org.fireflyest.craftparticle.Brush;
import org.fireflyest.craftparticle.DynamicLocation;
import org.fireflyest.craftparticle.DynamicVector;

public class LineStroke<T> extends Stroke<T> {

    protected final DynamicVector dVector;
    protected int time;

    public LineStroke(Brush<T> brush, DynamicLocation dLocation, int maxTime, int drawSpacing, DynamicVector dVector) {
        super(brush, dLocation, maxTime, drawSpacing);
        this.dVector = dVector;
    }

    public LineStroke(Brush<T> brush, DynamicLocation dLocation, int maxTime, DynamicVector dVector) {
        super(brush, dLocation, maxTime);
        this.dVector = dVector;
    }

    @Override
    public void realtimeDraw() {
        Vector vector = dVector.getVector(time++);
        ParticleUtils.line(brush, constant ? null : cache, dLocation.getLocation(), vector);
    }
    
}
