package org.fireflyest.craftmsg;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreService {
    
    public static final String DEFAULT_OBJECTIVE = "sidebar";

    private final JavaPlugin plugin;
    private final Scoreboard scoreboard;

    public ScoreService(JavaPlugin plugin, Scoreboard scoreboard) {
        this.plugin = plugin;
        this.scoreboard = scoreboard;
    }

    /**
     * 初始化计分榜
     * @param displayName 展示名
     */
    public void intiScoreboard(String displayName) {
        for (Objective objective : scoreboard.getObjectives()) {
            objective.unregister();
        }
        this.registerObjective(DEFAULT_OBJECTIVE, "none", displayName, RenderType.INTEGER, DisplaySlot.SIDEBAR);
   }

   /**
    * 注册
    * @param name 名称
    * @param criteria 计分类别
    * @param displayName 展示名
    * @param renderType 渲染类型
    * @param displaySlot 展示位置
    */
    public void registerObjective(String name, String criteria, String displayName, RenderType renderType, DisplaySlot displaySlot) {
        Objective objective = scoreboard.getObjective(name);
        if (objective == null) {
            objective = Bukkit.getScoreboardManager()
                .getMainScoreboard()
                .registerNewObjective(name, criteria, displayName, renderType);
        }
        objective.setDisplaySlot(displaySlot);
    }

    /**
     * 设置一个计分
     * @param entry 名称
     * @param score 分数
     */
    public void set(String entry, int score) {
        this.set(DEFAULT_OBJECTIVE, entry, score);
    }

    /**
     * 设置一个计分
     * @param objective 对象
     * @param entry 名称
     * @param score 分数
     */
    public void set(String objective, String entry, int score) {
        scoreboard.getObjective(objective).getScore(entry).setScore(score);
    }

    /**
     * 设置一个递减计分
     * @param entry 名称
     * @param second 秒
     */
    public void setExist(String entry, int second) {
        this.setExist(DEFAULT_OBJECTIVE, entry, second);
    }

    /**
     * 设置一个递减计分
     * @param objective 对象
     * @param entry 名称
     * @param second 秒
     */
    public void setExist(String objective, String entry, int second) {
        this.setExistDelay(objective, entry, second, 0);
    }

    /**
     * 设置一个递减计分
     * @param entry 名称
     * @param second 秒
     * @param delay 延迟秒
     */
    public void setExistDelay(String entry, int second, int delay) {
        this.setExistDelay(DEFAULT_OBJECTIVE, entry, second, delay);
    }

    /**
     * 设置一个递减计分
     * @param objective 对象
     * @param entry 名称
     * @param second 存在秒
     * @param delay 延迟秒
     */
    public void setExistDelay(String objective, String entry, int second, int delay) {
        final Score score = scoreboard.getObjective(objective).getScore(entry);
        if (score.getScore() > 0) {
            score.setScore(score.getScore() + second);
        } else {
            new BukkitRunnable() {
                private boolean set = false;
                @Override
                public void run() {
                    if (set) {
                        score.setScore(score.getScore() - 1);
                    } else {
                        score.setScore(second);
                        set = true;
                    }
                    if (score.getScore() <= 0) {
                        scoreboard.resetScores(entry);
                        cancel();
                    }
                }
            }.runTaskTimer(plugin, (delay + 1) * 20L, 20L);
        }
    }

    /**
     * 移除某个计分
     * @param entry 名称
     */
    public void remove(String entry) {
        scoreboard.resetScores(entry);
    }

    /**
     * 获取计分榜
     */
    public Scoreboard getScoreboard() {
        return scoreboard;
    }

}
