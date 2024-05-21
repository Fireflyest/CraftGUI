package org.fireflyest.craftdatabase.cache;

import javax.annotation.Nonnull;

/**
 * 字符串类型的缓存服务
 * @author Fireflyest
 * @since 2022/8/28
 */
public class StringCacheService extends CacheService<String>{

    public StringCacheService() {
        //
    }

    /**
     * 扩展字符串
     * @param key 键
     * @param value 扩展值
     */
    public void append(@Nonnull String key, String value) {
        Cache<String> cache;
        if ((cache = cacheMap.get(key)) != null){
            String cacheValue = cache.get();
            cacheValue = (cacheValue == null ? value : cacheValue.concat(value));
            cache.set(cacheValue);
        } else {
            set(key, value);
        }
    }
}
