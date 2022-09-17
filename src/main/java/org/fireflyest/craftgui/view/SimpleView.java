package org.fireflyest.craftgui.view;

import java.util.HashMap;
import java.util.Map;

import org.fireflyest.craftgui.api.View;

public class SimpleView implements View<SimplePage> {

    // 插件名称用于在监听事件时判断点击的是否本插件的界面
    protected final String title;
    protected final int size;
    protected int maxPage;
    // 存储各个target的页面
    protected final Map<String, SimplePage> pageMap = new HashMap<>();

    /**
     * 示例界面
     * @param title 标题
     */
    public SimpleView(String title, int size, int maxPage) {
        this.title = title;
        this.size = size;
        this.maxPage = maxPage;
    }

    @Override
    public SimplePage getFirstPage(String target) {
        return pageMap.computeIfAbsent(target, k -> new SimplePage(title, target, 0, size, maxPage));
    }

    @Override
    public void removePage(String target) {
        this.pageMap.remove(target);
    }
}
