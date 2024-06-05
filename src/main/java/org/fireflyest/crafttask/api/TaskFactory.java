package org.fireflyest.crafttask.api;

import javax.annotation.Nonnull;

/**
 * 任务工厂，由其他插件继承实现
 * @author Fireflyest
 * @since 1.2
 */
public abstract class TaskFactory<T extends PrepareTask> {
    
    /**
     * @see TaskHandler#prepareTask(String, TaskFactory)
     * @param values 创建任务所需参数
     * @return 任务
     */
    public abstract T create(@Nonnull String playerName, Object... values);

}
