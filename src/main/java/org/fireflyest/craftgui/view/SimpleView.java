package org.fireflyest.craftgui.view;

import org.fireflyest.craftgui.api.View;

import java.util.HashMap;
import java.util.Map;

public class SimpleView implements View<SimplePage> {

    public static final String TYPE = "固定几个类型";
    public static final String PLAYER = "当然也可以不固定，而是一个玩家名代表一种类型";
    public static final String POINT = "不同类型页面可以不同";

    private final Map<String, SimplePage> pageMap = new HashMap<>();

    public SimpleView(String title) {
        pageMap.put(TYPE, new SimplePage(title, TYPE, 1, 54));
        pageMap.put(PLAYER, new SimplePage(title, PLAYER, 1, 54));
        pageMap.put(POINT, new SimplePage(title, POINT, 1, 54));
    }

    @Override
    public SimplePage getFirstPage(String target){
        return pageMap.get(target);
    }

    @Override
    public void removePage(String target) {
        pageMap.remove(target);
    }
}
