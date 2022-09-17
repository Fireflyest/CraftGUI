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
import org.fireflyest.craftgui.protocol.ViewProtocol;
import org.fireflyest.craftgui.view.SimpleView;

/**
 * 插件加载入口
 */
public final class CraftGUI extends JavaPlugin {

    private static JavaPlugin plugin;
    private static ViewGuideImpl viewGuide;

    public static final String SIMPLE_VIEW = "craftgui.simple";
    public static final int BUKKIT_VERSION = XMaterial.getVersion();

    @Override
    public void onEnable() {
        CraftGUI.setPlugin(this);

        // 统计
        new Metrics(this, 15676);

        // 新建导航
        ViewGuideImpl viewGuideImpl = new ViewGuideImpl();
        this.getServer().getServicesManager().register(ViewGuide.class, viewGuideImpl, plugin, ServicePriority.Normal);
        viewGuideImpl.addView(SIMPLE_VIEW, new SimpleView("[CraftGUI]"));
        CraftGUI.setViewGuide(viewGuideImpl);

        // 注册监听
        this.getServer().getPluginManager().registerEvents(new ViewEventListener(), this);

        // 初始化监听
        ViewProtocol.initViewProtocol();
    }

    @Override
    public void onDisable() {
        // 关闭协议监听
        ProtocolLibrary.getProtocolManager().removePacketListeners(CraftGUI.getPlugin());

        // 关闭所有数据库连接
        SQLConnector.closeAll();
    }

    /**
     * 设置其他类可获取的导航对象
     * @param guide 导航
     */
    public static void setViewGuide(ViewGuideImpl guide) {
        CraftGUI.viewGuide = guide;
    }

    /**
     * 设置其他类可获取的插件对象
     * @param javaPlugin 插件
     */
    public static void setPlugin(JavaPlugin javaPlugin) {
        CraftGUI.plugin = javaPlugin;
    }

    /**
     * 获取导航
     * @return 导航
     */
    public static ViewGuideImpl getViewGuide() {
        return viewGuide;
    }

    /**
     * 获取插件
     * @return 插件
     */
    public static JavaPlugin getPlugin() {
        return plugin;
    }

}
