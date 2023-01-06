package org.fireflyest.craftcommand.argument;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class OfficePlayerArgs implements Argument {

    @Override
    public List<String> tab(String arg) {
        List<String> argList = new ArrayList<>();
        this.putOfflinePlayer(argList, arg);
        return argList;
    }
    
    /**
     * 离线玩家提示
     * @param argList 提示列表
     * @param arg 参数
     */
    private void putOfflinePlayer(List<String> argList, String arg) {
        for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
            if (argList.size() > 20) {
                break;
            }
            String name = offlinePlayer.getName();
            if (name.startsWith(arg)) {
                argList.add(name);
            }
        }
    }

}
