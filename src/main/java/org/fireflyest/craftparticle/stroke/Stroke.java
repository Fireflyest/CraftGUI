package org.fireflyest.craftparticle.stroke;

import javax.annotation.Nonnull;

import org.fireflyest.craftparticle.DynamicLocation;

public abstract class Stroke<T> {
    
    protected final Brush<T> brush;
    protected final DynamicLocation dLocation;
    protected int maxTime;
    protected int drawSpacing;
    protected int count;
    
    protected Stroke(@Nonnull Brush<T> brush, @Nonnull DynamicLocation dLocation, int maxTime, int drawSpacing) {
        this.brush = brush;
        this.dLocation = dLocation;
        this.maxTime = maxTime;
        this.drawSpacing = drawSpacing;
    }

    protected Stroke(@Nonnull Brush<T> brush, @Nonnull DynamicLocation dLocation, int maxTime) {
        this(brush, dLocation, maxTime, 5);
    }

    public abstract boolean draw();

    public void stop() {
        maxTime = 0;
    }

    protected boolean canDraw() {
        if (++count == drawSpacing) {
            count = 0;
            maxTime--;
            return true;
        }
        return false;
    }
    

}
