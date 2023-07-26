package org.fireflyest.crafttask.core;

import java.util.ArrayDeque;
import java.util.Arrays;

import javax.annotation.Nonnull;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.fireflyest.crafttask.api.Task;

/**
 * 任务执行类
 * @author Fireflyest
 * @since 1.2
 */
public class TaskWorker {

    private final String name;
    private boolean enable;

    private BukkitTask bukkitTask;
    private final JavaPlugin plugin;
    private final ArrayDeque<Task> taskQueue = new ArrayDeque<>();

    /**
     * 创建任务工作者
     * @param name 名称
     * @param plugin 插件
     */
    public TaskWorker(@Nonnull String name, @Nonnull JavaPlugin plugin) {
        this.name = name;
        this.plugin = plugin;

        // 开始工作
        this.start();
    }

    /**
     * 添加任务
     * @param tasks 任务
     */
    public void add(@Nonnull Task... tasks) {
        if (!enable) {
            String info = String.format("The task worker '%s' start to work! ", name);
            plugin.getLogger().info(info);
            this.start();
        }
        synchronized (taskQueue) {
            taskQueue.addAll(Arrays.asList(tasks));
            taskQueue.notifyAll();
        }
    }

    /**
     * 开始工作
     */
    public void start() {
        this.stop();
        this.enable = true;
        this.bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {
                loop();
            }
        }.runTaskAsynchronously(plugin);
    }

    /**
     * 停止工作
     */
    public void stop() {
        // 停止循环
        enable = false;
        // 解锁
        synchronized (taskQueue) {
            taskQueue.notifyAll();
        }
        // 关闭线程
        if (bukkitTask != null) {
            bukkitTask.cancel();
        }
    }

    /**
     * 循环执行，执行完队列后进入等待状态
     * @see org.fireflyest.crafttask.api.Task#execute()
     */
    private void loop() {
        while (enable) {
            try {
                // 如果执行完，就锁住
                if (taskQueue.isEmpty()) {
                    synchronized (taskQueue) {
                        taskQueue.wait();
                    }
                    continue;
                }
                Task task = taskQueue.poll();
                task.execute();
                if (task.hasFollowTasks()) {
                    taskQueue.addAll(task.followTasks());
                }
            } catch (InterruptedException e) {
                String info = String.format("error on taskQueue take, '%s' stop working!", name);
                plugin.getLogger().severe(info);
                e.printStackTrace();
                // Restore interrupted state...
                Thread.currentThread().interrupt();
                this.stop();
            } catch (Exception e) {
                String info = String.format("error on task execute, '%s' stop working!", name);
                plugin.getLogger().severe(info);
                e.printStackTrace();
                this.stop();
            }
        }
    }

}
