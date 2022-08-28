package org.fireflyest.craftgui;

import com.cryptomorin.xseries.XMaterial;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.fireflyest.craftdatabase.sql.SQLConnector;
import org.fireflyest.craftgui.api.ViewGuide;
import org.fireflyest.craftgui.core.ViewGuideImpl;
import org.fireflyest.craftgui.listener.SimpleEventListener;
import org.fireflyest.craftgui.listener.ViewEventListener;
import org.fireflyest.craftgui.protocol.ViewProtocol;
import org.fireflyest.craftgui.view.SimpleView;

/**
 * 插件加载入口
 */
public final class CraftGUI extends JavaPlugin {

    private static JavaPlugin plugin;

    public static JavaPlugin getPlugin(){
        return plugin;
    }

    private static ViewGuideImpl viewGuide;

    public static ViewGuideImpl getViewGuide() {
        return viewGuide;
    }

    public static final String SIMPLE_VIEW = "craftgui.simple";

    public static final int BUKKIT_VERSION = XMaterial.getVersion();

    @Override
    public void onEnable() {
        plugin = this;

        // 统计
        new Metrics(this, 15676);
        // 新建导航
        viewGuide = new ViewGuideImpl();
        viewGuide.addView(SIMPLE_VIEW, new SimpleView("[CraftGUI]"));

        // 注册监听
        this.getServer().getPluginManager().registerEvents( new ViewEventListener(), this);
        this.getServer().getPluginManager().registerEvents( new SimpleEventListener(), this);

        // 注册服务
        this.getServer().getServicesManager().register(ViewGuide.class, viewGuide, plugin, ServicePriority.Normal);

        // 初始化监听
        ViewProtocol.initViewProtocol();
    }

    @Override
    public void onDisable() {
        // 关闭协议监听
        ViewProtocol.close();
        // 关闭所有数据库连接
        SQLConnector.closeAll();
    }
}
