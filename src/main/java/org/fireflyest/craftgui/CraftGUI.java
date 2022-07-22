package org.fireflyest.craftgui;

import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.fireflyest.craftgui.api.ViewGuide;
import org.fireflyest.craftgui.core.ViewGuideImpl;
import org.fireflyest.craftgui.listener.ViewEventListener;
import org.fireflyest.craftgui.protocol.ViewProtocol;

public final class CraftGUI extends JavaPlugin {

    private static JavaPlugin plugin;

    public static JavaPlugin getPlugin(){
        return plugin;
    }

    private static ViewGuideImpl viewGuide;

    public static ViewGuideImpl getViewGuide() {
        return viewGuide;
    }

    @Override
    public void onEnable() {
        plugin = this;

        // 统计
        new Metrics(this, 15676);
        // 新建导航
        viewGuide = new ViewGuideImpl();

        // 注册监听
        this.getServer().getPluginManager().registerEvents( new ViewEventListener(), this);

        // 注册服务
        this.getServer().getServicesManager().register(ViewGuide.class, viewGuide, plugin, ServicePriority.Normal);

        // 初始化监听
        ViewProtocol.initViewProtocol();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
