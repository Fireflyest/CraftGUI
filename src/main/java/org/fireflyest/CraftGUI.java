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

/**
 * 插件加载入口
 */
public final class CraftGUI extends JavaPlugin {

    public static final String SIMPLE_VIEW = "craftgui.simple";
    public static final int BUKKIT_VERSION = XMaterial.getVersion();

    @Override
    public void onEnable() {
        // 统计
        new Metrics(this, 15676);

        // 新建导航
        ViewGuideImpl viewGuideImpl = new ViewGuideImpl();
        this.getLogger().info("Registering service of guide");
        this.getServer().getServicesManager().register(ViewGuide.class, viewGuideImpl, this, ServicePriority.Normal);

        // 注册监听
        this.getLogger().info("Registering listener for view event");
        this.getServer().getPluginManager().registerEvents(new ViewEventListener(viewGuideImpl), this);
    }

    @Override
    public void onDisable() {
        // 关闭协议监听
        ProtocolLibrary.getProtocolManager().removePacketListeners(this);

        // 关闭所有数据库连接
        SQLConnector.closeAll();
    }

    /**
     * 获取插件
     * @return 插件
     */
    public static CraftGUI getPlugin() {
        return getPlugin(CraftGUI.class);
    }

}
