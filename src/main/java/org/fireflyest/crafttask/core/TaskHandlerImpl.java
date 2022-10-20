package org.fireflyest.crafttask.core;

import java.util.HashMap;

import javax.annotation.Nonnull;

import org.bukkit.plugin.java.JavaPlugin;
import org.fireflyest.crafttask.api.Task;
import org.fireflyest.crafttask.api.TaskHandler;

/**
 * 工作线程处理和工作分配的实现
 * @author Fireflyest
 * @since 1.2
 */
public class TaskHandlerImpl implements TaskHandler {

    private final HashMap<String, TaskWorker> workerMap = new HashMap<>();

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
    public void putTasks(@Nonnull String name, @Nonnull Task... tasks) {
        if (workerMap.containsKey(name)) {
            workerMap.get(name).add(tasks);
        }
    }

    @Override
    public void disable() {
        for (TaskWorker worker : workerMap.values()) {
            if (worker != null) worker.stop();
        }
    }
    
}
