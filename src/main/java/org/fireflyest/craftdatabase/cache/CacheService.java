package org.fireflyest.craftdatabase.cache;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Fireflyest
 * @since 2022/8/26
 */
public class CacheService<T> {

    private final Map<String, Cache<T>> cacheMap = new ConcurrentHashMap<>();

    public CacheService() {
    }

    /**
     * 设置无期限数据
     * @param key 键
     * @param value 值
     */
    public void set(@Nonnull String key, T value){
        cacheMap.put(key, new Cache<>(value));
    }

    /**
     * 获取数据
     * @param key 键
     * @return 值
     */
    @Nullable
    public T get(@Nonnull String key){
        return cacheMap.containsKey(key) ? cacheMap.get(key).get() : null;
    }

}
