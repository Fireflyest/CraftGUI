package org.fireflyest.craftmsg;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class MessageService {
    
    private final JavaPlugin plugin;
    private final Map<String, ScoreService> scoreMap = new HashMap<>();
    private final Map<String, Instant> timeMap = new HashMap<>();
    private ScoreService shareScore;
    private String title;

    /**
     * 使用单独的计分榜创建
     * @param plugin 插件
     * @param shareBoard 计分榜
     */
    public MessageService(JavaPlugin plugin, String title, Scoreboard shareBoard) {
        this.plugin = plugin;
        this.title = title;
        this.shareScore = new ScoreService(plugin, shareBoard);
        shareScore.intiScoreboard(title);
    }

    /**
     * 各自独立计分榜创建
     * @param plugin 插件
     */
    public MessageService(JavaPlugin plugin, String title) {
        this.plugin = plugin;
        this.title = title;
    }

    /**
     * 玩家加入
     * @param player 玩家
     */
    public void playerJoin(Player player) {
        // 新建
        ScoreService score = shareScore != null ? shareScore : new ScoreService(plugin, Bukkit.getScoreboardManager().getNewScoreboard());
        score.intiScoreboard(title);
        // 使用
        scoreMap.put(player.getName(), score);
        player.setScoreboard(score.getScoreboard());
    }

    /**
     * 玩家离开
     * @param player 玩家
     */
    public void playerQuit(Player player) {
        if (scoreMap.containsKey(player.getName())) {
            ScoreService score = scoreMap.get(player.getName());
            score.setEnable(false);
            scoreMap.remove(player.getName());
        }
    }

    /**
     * 推送全服信息
     * @param message 内容
     * @param second 持续时间
     * @param delay 延迟
     */
    public void pushGlobalMessage(String message, int second, int delay) {
        // 是否全统一计分榜
        if (shareScore != null) {
            shareScore.setExistDelay(message, second, delay);
            return;
        }
        // 或者单独发送
        for (ScoreService score : scoreMap.values()) {
            if (score == null || !score.isEnable()) {
                continue;
            }
            score.setExistDelay(message, second, delay);
        }
    }
    
    /**
     * 推送全服信息
     * @param message 内容
     * @param second 持续时间
     */
    public void pushGlobalMessage(String message, int second) {
        this.pushGlobalMessage(message, second, 0);
    }

    /**
     * 推送玩家信息
     * @param playerName 玩家名
     * @param message 信息
     * @param second 持续时间
     * @param delay 延迟
     */
    public void pushMessage(String playerName, String message, int second, int delay) {
        ScoreService score = scoreMap.get(playerName);
        if (score == null || !score.isEnable()) {
            return;
        }
        score.setExistDelay(message, second, delay);
    }

    /**
     * 推送玩家信息
     * @param playerName 玩家名
     * @param message 信息
     * @param second 持续时间
     */
    public void pushMessage(String playerName, String message, int second) {
        this.pushMessage(playerName, message, second, 0);
    }

    /**
     * 玩家弹出信息
     * @param player 玩家
     * @param message 信息
     */
    public void popMessage(Player player, String message) {
        final String playerName = player.getName();
        // 下次可弹出时间
        if (!timeMap.containsKey(playerName) || timeMap.get(playerName).isBefore(Instant.now())) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
            timeMap.put(playerName, Instant.now().plusSeconds(1));
        } else {
            // 发送的时间点
            System.out.println("");
            System.out.println((timeMap.get(playerName).compareTo(Instant.now())));
            long timePoint = (long)(timeMap.get(playerName).compareTo(Instant.now()) / (50 * 1e6));
            System.out.println(timePoint);
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
                }
            }.runTaskLater(plugin, timePoint);
            // 可发送时间再推迟一秒
            timeMap.put(playerName, timeMap.get(playerName).plusSeconds(1));
        }
    }

}
