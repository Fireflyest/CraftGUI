package org.fireflyest.craftparticle;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.fireflyest.craftparticle.stroke.Stroke;

public class ParticleTasks {
    
    private final Set<Stroke<?>> taskSet = new HashSet<>();
    private final JavaPlugin plugin;
    private BukkitTask task;

    public ParticleTasks(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void putTasks(Stroke<?>... strokes) {
        taskSet.addAll(Arrays.asList(strokes));
        if (task == null || task.isCancelled()) {
            task = new BukkitRunnable() {
                @Override
                public void run() {
                    draw();
                    if (taskSet.isEmpty()) {
                        this.cancel();
                    }
                }
            }.runTaskTimer(plugin, 0, 1);
        }
    }

    private void draw() {
        Set<Stroke<?>> drawnSet = new HashSet<>();
        taskSet.parallelStream().forEach(stroke -> {
            if (stroke.draw()) drawnSet.add(stroke);
        });
        taskSet.removeAll(drawnSet);
    }

}
