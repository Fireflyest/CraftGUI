package org.fireflyest.craftparticle.stroke;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.fireflyest.craftparticle.Brush;


public class ParticleUtils {
    
    private ParticleUtils() {
    }

    public static <T> void line(@Nonnull Brush<T> brush, @Nullable List<Location> cache, @Nonnull Location loc, @Nonnull Vector vector) {
        double steps = vector.length() / brush.getSpacing();
        vector.normalize().multiply(brush.getSpacing());
        for (float i = 0; i <= steps; i++) {
            Location step = loc.clone().add(vector.clone().multiply(i));
            if (cache != null) {
                cache.add(step);
            }
            World world = loc.getWorld();
            world.spawnParticle(brush.getParticle(), 
                loc, 
                brush.getCount(), 
                brush.getOffsetX(), 
                brush.getOffsetY(), 
                brush.getOffsetZ(), 
                brush.getExtra(), 
                brush.getData());
        }
    }

    public static <T> void line(@Nonnull Brush<T> brush, @Nullable List<Location> cache, @Nonnull Location loc1, @Nonnull Location loc2) {
        line(brush, cache, loc2, new Vector(loc2.getX() - loc1.getX(), loc2.getY() - loc1.getY(), loc2.getZ() - loc1.getZ()));
    }

    public static <T> void rectangle(@Nonnull Brush<T> brush, @Nullable List<Location> cache, @Nonnull Location loc1, @Nonnull Location loc2) {
        double y = (loc1.getY() + loc2.getY()) / 2;
        Location loc3 = new Location(loc1.getWorld(), loc1.getX(), y, loc2.getZ());
        Location loc4 = new Location(loc1.getWorld(), loc2.getX(), y, loc1.getZ());

        line(brush, cache, loc1, loc3);
        line(brush, cache, loc1, loc4);
        line(brush, cache, loc2, loc3);
        line(brush, cache, loc2, loc4);
    }

    public static <T> void cuboid(@Nonnull Brush<T> brush, @Nullable List<Location> cache, @Nonnull Location loc1, @Nonnull Location loc2) {
        double dy = loc1.getY() - loc2.getY();
        Location loc3 = loc1.clone().add(0, -dy, 0);
        Location loc4 = loc2.clone().add(0, dy, 0);

        Location loc5 = new Location(loc1.getWorld(), loc1.getX(), loc1.getY(), loc2.getZ());
        Location loc6 = new Location(loc1.getWorld(), loc2.getX(), loc1.getY(), loc1.getZ());
        Location loc7 = new Location(loc1.getWorld(), loc1.getX(), loc2.getY(), loc2.getZ());
        Location loc8 = new Location(loc1.getWorld(), loc2.getX(), loc2.getY(), loc1.getZ());

        // 四条柱子
        line(brush, cache, loc1, loc3);
        line(brush, cache, loc2, loc4);
        line(brush, cache, loc5, loc7);
        line(brush, cache, loc6, loc8);

        line(brush, cache, loc1, loc5);
        line(brush, cache, loc1, loc6);
        line(brush, cache, loc4, loc5);
        line(brush, cache, loc4, loc6);

        line(brush, cache, loc2, loc7);
        line(brush, cache, loc2, loc8);
        line(brush, cache, loc3, loc7);
        line(brush, cache, loc3, loc8);
    }


    /**
     * 法线与Y平行的正方形
     */
    // public static <T> void squareY(@Nonnull Brush<T> brush, @Nonnull Location loc1, @Nonnull Location loc2) {
    //     Vector vector = new Vector(loc1.getX() - loc2.getX(), loc1.getY() - loc2.getY(), loc1.getZ() - loc2.getZ());
    //     Location loc3 = loc1.clone().add(vector.clone().multiply(Math.sqrt(2) * 0.5).rotateAroundY((Math.PI * 3) / 4));
    //     Location loc4 = loc1.clone().add(vector.clone().multiply(Math.sqrt(2) * 0.5).rotateAroundY((-Math.PI * 3) / 4));
    //     line(brush, loc1, loc3);
    //     line(brush, loc1, loc4);
    //     line(brush, loc2, loc3);
    //     line(brush, loc2, loc4);
    // }

    /**
     * 顶部为正方形的长方体
     */
    // public static <T> void squareCuboidY(@Nonnull Brush<T> brush, @Nonnull Location loc1, @Nonnull Location loc2) {
    //     double dy = loc1.getY() - loc2.getY();
    //     Location loc3 = loc1.clone().add(0, -dy, 0);
    //     Location loc4 = loc2.clone().add(0, dy, 0);

    //     Vector vector1 = new Vector(loc1.getX() - loc4.getX(), loc1.getY() - loc4.getY(), loc1.getZ() - loc4.getZ());
    //     Location loc5 = loc1.clone().add(vector1.clone().multiply(Math.sqrt(2) * 0.5).rotateAroundY((Math.PI * 3) / 4));
    //     Location loc6 = loc1.clone().add(vector1.clone().multiply(Math.sqrt(2) * 0.5).rotateAroundY((-Math.PI * 3) / 4));
    //     Vector vector2 = new Vector(loc2.getX() - loc3.getX(), loc2.getY() - loc3.getY(), loc2.getZ() - loc3.getZ());
    //     Location loc7 = loc2.clone().add(vector2.clone().multiply(Math.sqrt(2) * 0.5).rotateAroundY((Math.PI * 3) / 4));
    //     Location loc8 = loc2.clone().add(vector2.clone().multiply(Math.sqrt(2) * 0.5).rotateAroundY((-Math.PI * 3) / 4));
    //     // 上下两个
    //     squareY(brush, loc1, loc4);
    //     squareY(brush, loc2, loc3);
    //     // 四条柱子
    //     line(brush, loc1, loc3);
    //     line(brush, loc2, loc4);
    //     line(brush, loc5, loc8);
    //     line(brush, loc6, loc7);
    // }

}
