package org.fireflyest.craftdatabase.cache;

import javax.annotation.Nullable;
import java.time.Duration;
import java.time.Instant;

/**
 * @author Fireflyest
 * @since 2022/8/27
 */
public class Cache<T> {

    private T value;
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

    public void set(T value){
        this.value = value;
    }

    /**
     * 数据剩余保留时间，如果没有限制，返回-1；如果到期，返回0
     * @return 剩余时间
     */
    public long ttl(){
        if (deadline == null) return -1;
        long second = Duration.between(Instant.now(), deadline).toSeconds();
        return Math.max(second, 0);
    }

    /**
     * 设置数据保留时间
     * @param second 时间
     */
    public void expire(long second){
         deadline = Instant.now().plusSeconds(second);
    }

    /**
     * 设置为无限
     */
    public void persist(){
        deadline = null;
    }
}
