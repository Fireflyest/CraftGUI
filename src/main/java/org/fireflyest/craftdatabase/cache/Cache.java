package org.fireflyest.craftdatabase.cache;

import javax.annotation.Nullable;
import java.time.Instant;

/**
 * @author Fireflyest
 * @since 2022/8/27
 */
public class Cache<T> {

    private final T value;
    private Instant deadline;

    public Cache(T value) {
        this.value = value;
    }

    public Cache(T value, long second) {
        this.value = value;
        this.deadline = Instant.now().plusSeconds(second);
    }

    /**
     * 获取数据
     * @return 数据
     */
    @Nullable
    public T get(){
        if (deadline == null) return value;
        if (Instant.now().isBefore(deadline)) return value;
        return null;
    }

    /**
     * 延长数据保留时间
     * @param second 延长秒数
     */
    public void prolong(long second){
         deadline = deadline.plusSeconds(second);
    }

}
