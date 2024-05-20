package org.fireflyest.craftparticle.stroke;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.World;
import org.fireflyest.craftparticle.Brush;
import org.fireflyest.craftparticle.DynamicLocation;

/**
 * 笔画抽象类
 * @author Fireflyest
 * @since 1.2
 */
public abstract class Stroke<T> {
    
    protected final List<Location> cache = new ArrayList<>();
    protected final Brush<T> brush;
    protected final DynamicLocation dLocation;
    protected int maxTime;
    protected int drawSpacing;
    protected int count;
    
    protected boolean constant = true;
    
    /**
     * 笔画
     * @param brush 画笔
     * @param dLocation 起点
     * @param maxTime 绘制次数
     * @param drawSpacing 绘制间隔帧
     */
    protected Stroke(@Nonnull Brush<T> brush, @Nonnull DynamicLocation dLocation, int maxTime, int drawSpacing) {
        this.brush = brush;
        this.dLocation = dLocation;
        this.maxTime = maxTime;
        this.drawSpacing = drawSpacing;
    }

    /**
     * 笔画
     * @param brush 画笔
     * @param dLocation 起点
     * @param maxTime 绘制次数
     */
    protected Stroke(@Nonnull Brush<T> brush, @Nonnull DynamicLocation dLocation, int maxTime) {
        this(brush, dLocation, maxTime, 5);
    }

    /**
     * 绘制直到最大数量
     * @return 是否继续绘制
     */
    public boolean draw() {
        // 这一帧是否绘制
        if (this.canDraw()) {
            // 缓存机制
            if (constant && !cache.isEmpty()) {
                this.drawCache();
            } else {
                cache.clear();
                this.realtimeDraw();
            }
            // 扣除次数
            maxTime--;
        }
        return maxTime >= 0;
    }

    /**
     * 当没有缓存或者动态绘制时需要实时绘制，由子类实现绘制并实现缓存
     */
    public abstract void realtimeDraw();

    /**
     * 直接停止绘制
     */
    public void stop() {
        maxTime = 0;
    }

    /**
     * 获取当前帧是否需要进行绘制
     * @return 是否需要进行绘制
     */
    protected boolean canDraw() {
        return count++ % drawSpacing == 0;
    }
    
    /**
     * 绘制缓存
     */
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

    /**
     * 设置绘制模式是固定绘制还是动态绘制，动态绘制情况下不启用缓存
     * @param constant 是否固定绘制
     */
    public void setConstant(boolean constant) {
        this.constant = constant;
    }

}
