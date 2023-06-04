package org.fireflyest.crafttask.api;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.fireflyest.crafttask.exception.ExecuteException;

public abstract class Task {
    
    private final String playerName;
    private final Player player;

    protected Task(String playerName) {
        this.playerName = playerName;
        this.player = Bukkit.getPlayerExact(playerName);
    }

    // 产生的新任务
    protected final List<Task> follows = new ArrayList<>();

    /**
     * 执行任务
     * @throws ExecuteException 执行出错
     */
    public abstract void execute() throws ExecuteException;

    /**
     * 执行通知
     * @param info 执行信息
     */
    public void executeInfo(String info) {
        if (player != null) {
            player.sendMessage(info);
        }
    }

    /**
     * 获取执行者名称
     * @return 名称
     */
    public String getPlayerName() {
        return playerName;
    }

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
