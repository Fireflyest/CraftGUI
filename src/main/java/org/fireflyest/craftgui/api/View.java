package org.fireflyest.craftgui.api;

import javax.annotation.Nullable;

/**
 * 视图
 * @param <T> 实现ViewPage的页面类
 */
public interface View<T extends ViewPage> {

    /**
     * 获取某个标签的界面的首页<br/>
     * 标签一般指向一个ViewPage链表的头部<br/>
     * @param target 页面标签
     * @return 展示页面
     */
    @Nullable
    T getFirstPage(@Nullable String target);

    /**
     * 删除某个标签的页面
     * @param target 页面标签
     */
    void removePage(@Nullable String target);

}
