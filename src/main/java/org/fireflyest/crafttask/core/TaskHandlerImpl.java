package org.fireflyest.crafttask.core;

import java.util.HashMap;

import javax.annotation.Nonnull;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.fireflyest.CraftGUI;
import org.fireflyest.crafttask.api.PrepareTask;
import org.fireflyest.crafttask.api.Task;
import org.fireflyest.crafttask.api.TaskFactory;
import org.fireflyest.crafttask.api.TaskHandler;
import org.fireflyest.crafttask.exception.ExecuteException;

/**
 * 工作线程处理和工作分配的实现
 * @author Fireflyest
 * @since 1.2
 */
public class TaskHandlerImpl implements TaskHandler {

    private final HashMap<String, TaskWorker> workerMap = new HashMap<>();
    private final HashMap<String, TaskFactory<?>> factoryMap = new HashMap<>();

    /**
     * 由本插件实例化后发放到服务
     */
    public TaskHandlerImpl() {
        // 
    }

    @Override
    public void createWorker(@Nonnull String name, @Nonnull JavaPlugin plugin) {
        if (workerMap.containsKey(name)) {
            workerMap.get(name).stop();
        }
        workerMap.put(name, new TaskWorker(name, plugin));        
    }

    @Override
    public void removeWorker(@Nonnull String name) {
        if (workerMap.containsKey(name)) {
            workerMap.get(name).stop();
        }
        workerMap.remove(name);
    }

    @Override
    public void putTasks(@Nonnull String worker, @Nonnull Task... tasks) {
        if (workerMap.containsKey(worker)) {
            workerMap.get(worker).add(tasks);
        }
    }

    @Override
    public void prepareTask(String key, TaskFactory<?> factory) {
        factoryMap.put(key, factory);
    }

    @Override
    public void runTask(String key, String value) {
        this.runTask(key, value, 0, 0, 1);
    }

    @Override
    public void runTask(String key, String value, long delay, long period, int count) {
        BukkitRunnable runnable = this.runnable(key, value, count);
        if (runnable != null) {
            runnable.runTaskTimer(CraftGUI.getPlugin(), delay, period);
        }
    }

    @Override
    public void runTaskAsynchronously(String key, String value) {
        this.runTaskAsynchronously(key, value, 0, 0, 1);
    }

    @Override
    public void runTaskAsynchronously(String key, String value, long delay, long period, int count) {
        BukkitRunnable runnable = this.runnable(key, value, count);
        if (runnable != null) {
            runnable.runTaskTimerAsynchronously(CraftGUI.getPlugin(), delay, period);
        }
    }
    
    @Override
    public void disable() {
        for (TaskWorker worker : workerMap.values()) {
            if (worker != null) worker.stop();
        }
    }

    private BukkitRunnable runnable(String key, String value, final int count) {
        TaskFactory<?> factory = factoryMap.get(key);
        if (factory != null) {
            return new BukkitRunnable() {

                private PrepareTask task = factory.create(value);
                private String taskName = task.getClass().getSimpleName();
                private int runCount = Math.abs(count);

                @Override
                public void run() {
                    try {
                        task.execute();
                    } catch (ExecuteException e) {
                        String info = String.format("error on '%s' execute!", taskName);
                        CraftGUI.getPlugin().getLogger().severe(info);
                        e.printStackTrace();
                        cancel();
                    }
                    runCount--;
                    if (runCount <= 0) {
                        cancel();
                    }
                }
            };
        }
        return null;
    }

}
