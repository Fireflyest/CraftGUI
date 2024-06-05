package org.fireflyest.crafttask.api;

import javax.annotation.Nonnull;

/**
 * 预备任务
 * @author Fireflyest
 * @since 1.2
 */
public abstract class PrepareTask extends Task {

    protected Object[] values;

    protected PrepareTask(@Nonnull String playerName, Object... values) {
        super(playerName);
        this.values = values;
    }

}
