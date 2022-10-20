package org.fireflyest.crafttask.api;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.fireflyest.crafttask.exception.ExecuteException;

public abstract class Task {
    
    // 产生的新任务
    protected final List<Task> follows = new ArrayList<>();

    /**
     * 执行任务
     * @throws ExecuteException 执行出错
     */
    public abstract void execute() throws ExecuteException;

    /**
     * 获取是否有后续的任务需要处理
     * @return 是否有后续任务
     */
    public boolean hasFollowTasks() {
        return !follows.isEmpty();
    }

    /**
     * 获取后续任务
     * @return 后续任务列表
     */
    @Nonnull
    public List<Task> followTasks() {
        return follows;
    }

}
