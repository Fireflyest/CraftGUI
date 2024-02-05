package org.fireflyest.craftparticle;

import javax.annotation.Nonnull;

import org.bukkit.Particle;

/**
 * Particle (the number of times specified by count)
 * The position of each particle will be
 * randomized positively and negatively by the offset parameters
 * on each axis.
 * @param <T> type of particle data (see {@link Particle#getDataType()}
 */
public class Brush<T> {

    private T data;
    private Particle particle;
    private double spacing;
    private int count;
    private double offsetX;
    private double offsetY;
    private double offsetZ;
    private double extra;

        /**
         * @param particle the particle to spawn
        * @param count the number of particles
        * @param spacing the spacing between particles
        */
    public Brush(@Nonnull Particle particle, int count, double spacing) {
        this.particle = particle;
        this.count = count;
        this.spacing = spacing;
    }

    /**
     * @param particle the particle to spawn
     * @param count the number of particles
     */
    public Brush(@Nonnull Particle particle, int count) {
        this(particle, count, .5);
    }

    /**
     * @param particle the particle to spawn
     */
    public Brush(@Nonnull Particle particle) {
        this(particle, 1);
    }

    /**
     * @param <T> type of particle data (see {@link Particle#getDataType()}
     * @param data the data to use for the particle or null,
     *             the type of this depends on {@link Particle#getDataType()}
     * @return brush itself
     */
    public Brush<T> data(T data) {
        this.data = data;
        return this;
    }

    /**
     * @param extra the extra data for this particle, depends on the
     *              particle used (normally speed)
     * @return brush itself
     */
    public Brush<T> extra(double extra) {
        this.extra = extra;
        return this;
    }

    /**
     * @param offsetX the maximum random offset on the X axis
     * @param offsetY the maximum random offset on the Y axis
     * @param offsetZ the maximum random offset on the Z axis
     * @return brush itself
     */
    public Brush<T> offset(double offsetX, double offsetY, double offsetZ) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        return this;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Particle getParticle() {
        return particle;
    }

    public void setParticle(Particle particle) {
        this.particle = particle;
    }

    public double getSpacing() {
        return spacing;
    }

    public void setSpacing(double spacing) {
        this.spacing = spacing;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(double offsetX) {
        this.offsetX = offsetX;
    }

    public double getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(double offsetY) {
        this.offsetY = offsetY;
    }

    public double getOffsetZ() {
        return offsetZ;
    }

    public void setOffsetZ(double offsetZ) {
        this.offsetZ = offsetZ;
    }

    public double getExtra() {
        return extra;
    }

    public void setExtra(double extra) {
        this.extra = extra;
    }


    
}
