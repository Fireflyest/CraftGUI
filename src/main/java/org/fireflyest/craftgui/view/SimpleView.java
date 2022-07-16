package org.fireflyest.craftgui.view;

import org.fireflyest.craftgui.api.View;

import java.util.HashMap;
import java.util.Map;

public class SimpleView implements View<SimplePage> {

    // 插件名称用于在监听事件时判断点击的是否本插件的界面
    protected final String pluginName;
    // 存储各个target的页面
    protected final Map<String, SimplePage> pageMap = new HashMap<>();

    public SimpleView(String pluginName) {
        this.pluginName = pluginName;
    }

    @Override
    public SimplePage getFirstPage(String target){
        if (!pageMap.containsKey(target)){
            pageMap.put(target, new SimplePage(pluginName, target, 0, 54));
        }
        return pageMap.get(target);
    }

    @Override
    public void removePage(String target) {
        pageMap.remove(target);
    }
}
