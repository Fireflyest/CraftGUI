package org.fireflyest.crafttask.api;

import javax.annotation.Nonnull;

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
     * 布置任务
     * @param name 名称
     * @param tasks 任务
     */
    void putTasks(@Nonnull String name, @Nonnull Task... tasks);

    /**
     * 关闭
     */
    void disable();
}
