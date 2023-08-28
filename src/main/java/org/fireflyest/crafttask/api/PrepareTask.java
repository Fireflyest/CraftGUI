package org.fireflyest.crafttask.api;

public abstract class PrepareTask extends Task {

    protected String value;

    protected PrepareTask(String value, String playerName) {
        super(playerName);
        this.value = value;
    }

    protected PrepareTask(String value) {
        this(value, "");
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
