package org.fireflyest.crafttask.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * 工作线程管理和任务分发
 * @author Fireflyest
 * @since 1.2
 */
public interface TaskHandler {

    /**
     * 新建一个工作线程
     * @param name 名称
     * @param plugin 插件
     */
    void createWorker(@Nonnull String name, @Nonnull JavaPlugin plugin);

    /**
     * 关闭某个工作线程
     * @param name 名称
     */
    void removeWorker(@Nonnull String name);

    /**
     * 布置任务到工作线程
     * @param name 名称
     * @param tasks 任务
     */
    void putTasks(@Nonnull String worker, @Nonnull Task... tasks);

    /**
     * 准备任务
     * @param key 名称
     * @param task 任务
     */
    void prepareTask(@Nonnull String key, @Nonnull TaskFactory<?> factory);

    void runTask(@Nonnull String key, @Nullable String value);

    void runTask(@Nonnull String key, @Nullable String value, long delay, long period, int count);

    void runTaskAsynchronously(@Nonnull String key, @Nullable String value);

    void runTaskAsynchronously(@Nonnull String key, @Nullable String value, long delay, long period, int count);

    /**
     * 关闭
     */
    void disable();
}
