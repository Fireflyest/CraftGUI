package org.fireflyest.craftgui.button;

/**
 * @author Fireflyest
 * @since 2022/8/29
 */
public class ButtonAction {

    // nbt数据键，存储的值是String类型
    public static final String NBT_VALUE_KEY = "craft-gui-value";
    // nbt行为键，存储int类型值，让插件开发者在监听点击事件的时候判断按钮是用来干嘛
    public static final String NBT_ACTION_KEY = "craft-gui-action";

    public static final int ACTION_NONE = 0;

    // 返回
    public static final int ACTION_BACK = 1;
    // 关闭
    public static final int ACTION_CLOSE = 2;
    // 界面编辑，不需要bukkit的事件监听处理，由ViewPage自己处理
    public static final int ACTION_EDIT = 7;
    // 翻页，值一般是pre或next
    public static final int ACTION_PAGE_PRE = 11;
    public static final int ACTION_PAGE_NEXT = 12;
    public static final int ACTION_PAGE_JUMP = 12;

    // 以下行为调用事件，由插件处理
    // 打开某个界面
    public static final int ACTION_OPEN = 3;
    // 执行指令，值一般是要执行的指令
    public static final int ACTION_PLAYER_COMMAND = 4;
    // 控制台指令
    public static final int ACTION_CONSOLE_COMMAND = 5;
    // 翻页，值一般是pre或next
    public static final int ACTION_PAGE = 6;
    // 插件自定义行为，由插件识别value自己判别
    public static final int ACTION_PLUGIN = 8;
    // 未知
    public static final int ACTION_UNKNOWN = 10;

    public ButtonAction() {

    }
}
