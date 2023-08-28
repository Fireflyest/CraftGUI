package org.fireflyest.crafttask.api;

public abstract class TaskFactory<T extends PrepareTask> {
    
    public abstract T create(String value);

}
