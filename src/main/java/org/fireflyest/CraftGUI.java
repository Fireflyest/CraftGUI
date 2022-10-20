package org.fireflyest;

import com.comphenix.protocol.ProtocolLibrary;
import com.cryptomorin.xseries.XMaterial;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.fireflyest.craftdatabase.sql.SQLConnector;
import org.fireflyest.craftgui.api.ViewGuide;
import org.fireflyest.craftgui.core.ViewGuideImpl;
import org.fireflyest.craftgui.listener.ViewEventListener;
import org.fireflyest.crafttask.api.TaskHandler;
import org.fireflyest.crafttask.core.TaskHandlerImpl;

/**
 * 插件加载入口
 */
public final class CraftGUI extends JavaPlugin {

    public static final String SIMPLE_VIEW = "craftgui.simple";
    public static final int BUKKIT_VERSION = XMaterial.getVersion();

    public final TaskHandler taskHandler;

    /**
     * 入口类
     */
    public CraftGUI() {
        taskHandler = new TaskHandlerImpl();
    }

    @Override
    public void onEnable() {
        // 统计
        new Metrics(this, 15676);

        // 新建导航
        this.getLogger().info("Try to add packet listener.");
        ViewGuideImpl viewGuideImpl = new ViewGuideImpl();
        this.getLogger().info("Registering service of guide.");
        this.getServer().getServicesManager().register(ViewGuide.class, viewGuideImpl, this, ServicePriority.Normal);

        // 任务处理
        this.getLogger().info("Registering service of tasks.");
        this.getServer().getServicesManager().register(TaskHandler.class, taskHandler, this, ServicePriority.Normal);

        // 注册监听
        this.getLogger().info("Registering listener for view events.");
        this.getServer().getPluginManager().registerEvents(new ViewEventListener(viewGuideImpl), this);
    }

    @Override
    public void onDisable() {
        // 关闭协议监听
        ProtocolLibrary.getProtocolManager().removePacketListeners(this);
        this.getLogger().info("Removed packet listener.");

        // 关闭任务线程
        taskHandler.disable();

        // 关闭所有数据库连接
        this.getLogger().info("Closing SQL connections.");
        SQLConnector.closeAll();
        this.getLogger().info("All SQL connections are closed.");

        // 注销服务
        this.getServer().getServicesManager().unregisterAll(this);
    }

    /**
     * 获取插件
     * @return 插件
     */
    public static CraftGUI getPlugin() {
        return getPlugin(CraftGUI.class);
    }

}
