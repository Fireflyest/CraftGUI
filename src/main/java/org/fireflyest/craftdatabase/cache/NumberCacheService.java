package org.fireflyest.craftdatabase.cache;

import javax.annotation.Nonnull;

/**
 * 数字类型的缓存服务
 * @author Fireflyest
 * @since 2022/8/28
 */
public class NumberCacheService extends CacheService<Number>{

    public NumberCacheService() {
        // 
    }

    public void incr(@Nonnull String key) {
        incrBy(key, 1);
    }

    public void incrBy(@Nonnull String key, Number n) {
        Cache<Number> cache;
        if ((cache = cacheMap.get(key)) != null) {
            Number number = cache.get();
            if (number != null) {
                cache.set(number.doubleValue() + n.doubleValue());
            } else {
                cache.set(n);
            }
        }
    }

    public void decr(@Nonnull String key) {
        decrBy(key, 1);
    }

    public void decrBy(@Nonnull String key, Number n) {
        Cache<Number> cache;
        if ((cache = cacheMap.get(key)) != null) {
            Number number = cache.get();
            if (number != null) {
                cache.set(number.doubleValue() - n.doubleValue());
            } else {
                cache.set(n);
            }
        }
    }

}
