package org.fireflyest.util;

import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.util.Vector;


public class ParticleUtils {
    
    private ParticleUtils() {
    }

    /**
     * 直线
     * @param particle 粒子
     * @param loc1 角点1
     * @param loc2 角点2
     */
    public static void line(@Nonnull Particle particle, @Nonnull Location loc1, @Nonnull Location loc2) {
        line(particle, loc1, loc2, .5);
    }

    /**
     * 直线
     * @param particle 粒子
     * @param loc1 角点1
     * @param loc2 角点2
     * @param multiply 间隔控制参数
     */
    public static void line(@Nonnull Particle particle, @Nonnull Location loc1, @Nonnull Location loc2, double multiply) {
        World world = loc1.getWorld();
        Vector vector = new Vector(loc1.getX() - loc2.getX(), loc1.getY() - loc2.getY(), loc1.getZ() - loc2.getZ()).normalize().multiply(multiply);
        double distance = loc1.distance(loc2) * (1 / multiply);
        for (float i = 0; i < distance; i++) {
            Location loc = loc2.clone().add(vector.clone().multiply(i));
            world.spawnParticle(particle, loc, 1, 0, 0, 0, 0);
        }
    }

    /**
     * 垂直Y的正方形
     * @param particle 粒子
     * @param loc1 角点1
     * @param loc2 角点2
     */
    public static void squareY(@Nonnull Particle particle, @Nonnull Location loc1, @Nonnull Location loc2) {
        Vector vector = new Vector(loc1.getX() - loc2.getX(), loc1.getY() - loc2.getY(), loc1.getZ() - loc2.getZ());
        Location loc3 = loc1.clone().add(vector.clone().multiply(Math.sqrt(2) * 0.5).rotateAroundY((Math.PI * 3) / 4));
        Location loc4 = loc1.clone().add(vector.clone().multiply(Math.sqrt(2) * 0.5).rotateAroundY((-Math.PI * 3) / 4));
        line(particle, loc1, loc3);
        line(particle, loc1, loc4);
        line(particle, loc2, loc3);
        line(particle, loc2, loc4);
    }

    /**
     * 顶部为正方形的长方体
     * @param particle 粒子
     * @param loc1 角点1
     * @param loc2 角点2
     */
    public static void squareCuboidY(@Nonnull Particle particle, @Nonnull Location loc1, @Nonnull Location loc2) {
        double dy = loc1.getY() - loc2.getY();
        Location loc3 = loc1.clone().add(0, -dy, 0);
        Location loc4 = loc2.clone().add(0, dy, 0);

        Vector vector1 = new Vector(loc1.getX() - loc4.getX(), loc1.getY() - loc4.getY(), loc1.getZ() - loc4.getZ());
        Location loc5 = loc1.clone().add(vector1.clone().multiply(Math.sqrt(2) * 0.5).rotateAroundY((Math.PI * 3) / 4));
        Location loc6 = loc1.clone().add(vector1.clone().multiply(Math.sqrt(2) * 0.5).rotateAroundY((-Math.PI * 3) / 4));
        Vector vector2 = new Vector(loc2.getX() - loc3.getX(), loc2.getY() - loc3.getY(), loc2.getZ() - loc3.getZ());
        Location loc7 = loc2.clone().add(vector2.clone().multiply(Math.sqrt(2) * 0.5).rotateAroundY((Math.PI * 3) / 4));
        Location loc8 = loc2.clone().add(vector2.clone().multiply(Math.sqrt(2) * 0.5).rotateAroundY((-Math.PI * 3) / 4));
        // 上下两个
        squareY(particle, loc1, loc4);
        squareY(particle, loc2, loc3);
        // 四条柱子
        line(particle, loc1, loc3);
        line(particle, loc2, loc4);
        line(particle, loc5, loc8);
        line(particle, loc6, loc7);
    }

    /**
     * 矩形
     * @param particle 粒子
     * @param loc1 角点1
     * @param loc2 角点2
     */
    public static void rectangle(@Nonnull Particle particle, @Nonnull Location loc1, @Nonnull Location loc2) {
        Location loc3 = new Location(loc1.getWorld(), loc1.getX(), loc1.getY(), loc2.getZ());
        Location loc4 = new Location(loc1.getWorld(), loc2.getX(), loc2.getY(), loc1.getZ());

        line(particle, loc1, loc3);
        line(particle, loc1, loc4);
        line(particle, loc2, loc3);
        line(particle, loc2, loc4);
    }

    /**
     * 长方体
     * @param particle 粒子
     * @param loc1 角点1
     * @param loc2 角点2
     */
    public static void cuboid(@Nonnull Particle particle, @Nonnull Location loc1, @Nonnull Location loc2) {
        double dy = loc1.getY() - loc2.getY();
        Location loc3 = loc1.clone().add(0, -dy, 0);
        Location loc4 = loc2.clone().add(0, dy, 0);

        Location loc5 = new Location(loc1.getWorld(), loc1.getX(), loc1.getY(), loc2.getZ());
        Location loc6 = new Location(loc1.getWorld(), loc2.getX(), loc1.getY(), loc1.getZ());
        Location loc7 = new Location(loc1.getWorld(), loc1.getX(), loc2.getY(), loc2.getZ());
        Location loc8 = new Location(loc1.getWorld(), loc2.getX(), loc2.getY(), loc1.getZ());

        // 四条柱子
        line(particle, loc1, loc3);
        line(particle, loc2, loc4);
        line(particle, loc5, loc7);
        line(particle, loc6, loc8);

        line(particle, loc1, loc5);
        line(particle, loc1, loc6);
        line(particle, loc4, loc5);
        line(particle, loc4, loc6);

        line(particle, loc2, loc7);
        line(particle, loc2, loc8);
        line(particle, loc3, loc7);
        line(particle, loc3, loc8);
    }

}