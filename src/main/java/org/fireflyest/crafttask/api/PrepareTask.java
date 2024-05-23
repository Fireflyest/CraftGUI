package org.fireflyest.crafttask.api;

import javax.annotation.Nonnull;

/**
 * 预备任务
 * @author Fireflyest
 * @since 1.2
 */
public abstract class PrepareTask extends Task {

    protected String value;

    protected PrepareTask(@Nonnull String playerName, String value) {
        super(playerName);
        this.value = value;
    }

    protected PrepareTask(String value) {
        this("", value);
    }

    /**
     * 获取数据值
     * @return 数据文本
     */
    public String getValue() {
        return value;
    }

    /**
     * 设置数据值
     * @param value 数据文本
     */
    public void setValue(String value) {
        this.value = value;
    }


}
