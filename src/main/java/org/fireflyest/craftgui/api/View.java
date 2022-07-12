package org.fireflyest.craftgui.api;

import org.jetbrains.annotations.Nullable;

/**
 * 视图
 * @param <T> 视图页面
 */
public interface View<T extends ViewPage> {

    /**
     * 获取某个标签的界面的首页
     * @param target 页面标签
     * @return 展示页面
     */
    @Nullable
    T getFirstPage(@Nullable String target);

    /**
     * 删除某个标签的所有界面
     * @param target 页面标签
     */
    void removePage(@Nullable String target);

}
