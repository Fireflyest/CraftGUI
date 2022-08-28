package org.fireflyest.craftdatabase.cache;

import org.bukkit.util.NumberConversions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Fireflyest
 * @since 2022/8/26
 */
public class CacheService {

    private final Map<String, Cache<String>> cacheMap = new ConcurrentHashMap<>();
    private final Map<String, Cache<Set<String>>> cacheSetMap = new ConcurrentHashMap<>();

    public CacheService() {
    }

    /**
     * 删除数据
     * @param key 键
     */
    public void del(@Nonnull String key){
        cacheMap.remove(key);
        cacheSetMap.remove(key);
    }

    public void expire(@Nonnull String key, int second){
        Cache<String> stringCache;
        Cache<Set<String>> stringSetCache;
        if ((stringCache = cacheMap.get(key)) != null) stringCache.expire(second);
        if ((stringSetCache = cacheSetMap.get(key)) != null) stringSetCache.expire(second);
    }

    /**
     * 判断某个键是否存在<br/>
     * 只要其中一种存在就是存在
     * @param key 键
     * @return 存在
     */
    public boolean exist(@Nonnull String key){
        return this.existString(key) || this.existStringSet(key);
    }

    /**
     * 是否存在字符串类型数据
     * @param key 键
     * @return 是否存在
     */
    private boolean existString(@Nonnull String key){
        if (!cacheMap.containsKey(key)) return false;
        return cacheMap.get(key).get() != null;
    }

    /**
     * 是否存在字符串集数据
     * @param key 键
     * @return 是否存在
     */
    private boolean existStringSet(@Nonnull String key){
        if (!cacheSetMap.containsKey(key)) return false;
        return cacheSetMap.get(key).get() != null;
    }

    /**
     * 设置显示数据为永久
     * @param key 键
     */
    public void persist(@Nonnull String key){
        Cache<String> stringCache;
        Cache<Set<String>> stringSetCache;
        if ((stringCache = cacheMap.get(key)) != null) stringCache.persist();
        if ((stringSetCache = cacheSetMap.get(key)) != null) stringSetCache.persist();
    }

    /**
     * 数据剩余保留时间
     * @param key 键
     * @return 剩余时间
     */
    public long ttl(@Nonnull String key){
        Cache<String> stringCache;
        Cache<Set<String>> stringSetCache;
        if (existString(key) && (stringCache = cacheMap.get(key)) != null){
            return stringCache.ttl();
        }
        if (existStringSet(key) && (stringSetCache = cacheSetMap.get(key)) != null) {
            return stringSetCache.ttl();
        }
        return 0;
    }

    /**
     * 设置无期限数据
     * @param key 键
     * @param value 值
     */
    public void set(@Nonnull String key, String value){
        cacheMap.put(key, new Cache<>(value));
    }

    /**
     * 设置限期数据
     * @param key 键
     * @param second 期限
     * @param value 值
     */
    public void setex(@Nonnull String key, int second, String value){
        cacheMap.put(key, new Cache<>(value, second));
    }

    /**
     * 扩展字符串
     * @param key 键
     * @param value 扩展值
     */
    public void append(@Nonnull String key, String value){
        Cache<String> cache;
        if ((cache = cacheMap.get(key)) != null){
            String cacheValue = cache.get();
            cacheValue = (cacheValue == null ? value : cacheValue.concat(value));
            cache.set(cacheValue);
        } else {
            set(key, value);
        }
    }

    /**
     * 获取数据
     * @param key 键
     * @return 值
     */
    @Nullable
    public String get(@Nonnull String key){
        return cacheMap.containsKey(key) ? cacheMap.get(key).get() : null;
    }

    /**
     * 获取并设置值
     * @param key 键
     * @param value 新值
     * @return 旧值
     */
    public String getSet(@Nonnull String key, String value){
        String cacheValue = get(key);
        set(key, value);
        return cacheValue;
    }

    /**
     * 添加一个数据到集
     * @param key 键
     * @param value 值
     */
    public void sadd(@Nonnull String key, String... value){
        if (!cacheSetMap.containsKey(key)) cacheSetMap.put(key, new Cache<>(new HashSet<>()));
        Set<String> stringSet;
        if ((stringSet = cacheSetMap.get(key).get()) != null){
            stringSet.addAll(Set.of(value));
        }
    }

    /**
     * 获取某集所有数据
     * @param key 键
     * @return 数据集
     */
    public Set<String> smembers(@Nonnull String key){
        return cacheSetMap.containsKey(key) ? cacheSetMap.get(key).get() : Collections.emptySet();
    }

    /**
     * 删除集里的元素
     * @param key 键
     * @param value 值
     */
    public void srem(@Nonnull String key, String... value){
        smembers(key).removeAll(Set.of(value));
    }

    /**
     * 获取集合里面的元素数量
     * @param key 键
     * @return 数量
     */
    public int scard(@Nonnull String key){
        return smembers(key).size();
    }

}
