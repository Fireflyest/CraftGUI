package org.fireflyest.craftdatabase.cache;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 带泛型的缓存服务
 * @author Fireflyest
 * @since 2022/8/26
 */
public class CacheService<T> {

    private final Random random;
    protected final Map<String, Cache<T>> cacheMap = new ConcurrentHashMap<>();
    protected final Map<String, Cache<Set<T>>> cacheSetMap = new ConcurrentHashMap<>();

    public CacheService() {
        random = new Random();
    }

    /**
     * 删除数据
     * @param key 键
     */
    public void del(@Nonnull String key) {
        cacheMap.remove(key);
        cacheSetMap.remove(key);
    }

    /**
     * 设置数据期限
     * @param key 键
     * @param second 限制秒数
     */
    public void expire(@Nonnull String key, int second) {
        Cache<T> stringCache;
        Cache<Set<T>> stringSetCache;
        if ((stringCache = cacheMap.get(key)) != null) stringCache.expire(second);
        if ((stringSetCache = cacheSetMap.get(key)) != null) stringSetCache.expire(second);
    }

    /**
     * 判断某个键是否存在，
     * 只要其中一种存在就是存在
     * @param key 键
     * @return 存在
     */
    public boolean exist(@Nonnull String key) {
        return this.existValue(key) || this.existValueSet(key);
    }

    /**
     * 是否存在字符串类型数据
     * @param key 键
     * @return 是否存在
     */
    private boolean existValue(@Nonnull String key) {
        if (!cacheMap.containsKey(key)) return false;
        return cacheMap.get(key).get() != null;
    }

    /**
     * 是否存在字符串集数据
     * @param key 键
     * @return 是否存在
     */
    private boolean existValueSet(@Nonnull String key) {
        if (!cacheSetMap.containsKey(key)) return false;
        return cacheSetMap.get(key).get() != null;
    }

    /**
     * 设置显示数据为永久
     * @param key 键
     */
    public void persist(@Nonnull String key) {
        Cache<T> stringCache;
        Cache<Set<T>> stringSetCache;
        if ((stringCache = cacheMap.get(key)) != null) stringCache.persist();
        if ((stringSetCache = cacheSetMap.get(key)) != null) stringSetCache.persist();
    }

    /**
     * 数据剩余保留时间
     * @param key 键
     * @return 剩余时间
     */
    public long ttl(@Nonnull String key) {
        Cache<T> stringCache;
        Cache<Set<T>> stringSetCache;
        if (existValue(key) && (stringCache = cacheMap.get(key)) != null){
            return stringCache.ttl();
        }
        if (existValueSet(key) && (stringSetCache = cacheSetMap.get(key)) != null) {
            return stringSetCache.ttl();
        }
        return 0;
    }

    /**
     * 设置无期限数据
     * @param key 键
     * @param value 值
     */
    public void set(@Nonnull String key, T value) {
        cacheMap.put(key, new Cache<>(value));
    }

    /**
     * 设置限期数据
     * @param key 键
     * @param second 期限
     * @param value 值
     */
    public void setex(@Nonnull String key, int second, T value) {
        cacheMap.put(key, new Cache<>(value, second * 1000L));
    }

    /**
     * 设置限期数据
     * @param key 键
     * @param ms 期限
     * @param value 值
     */
    public void setexms(@Nonnull String key, int ms, T value) {
        cacheMap.put(key, new Cache<>(value, ms));
    }

    /**
     * 获取数据
     * @param key 键
     * @return 值
     */
    @Nullable
    public T get(@Nonnull String key) {
        return cacheMap.containsKey(key) ? cacheMap.get(key).get() : null;
    }

    /**
     * 获取并设置值
     * @param key 键
     * @param value 新值
     * @return 旧值
     */
    public T getSet(@Nonnull String key, T value) {
        T cacheValue = get(key);
        set(key, value);
        return cacheValue;
    }

    /**
     * 获取数据最后修改后存在时间
     * @param key 键
     * @return 存在时间
     */
    public long age(@Nonnull String key) {
        return cacheMap.containsKey(key) ? cacheMap.get(key).age() : -1;
    }

    /**
     * 添加一个数据到集
     * @param key 键
     * @param value 值
     */
    @SafeVarargs
    public final void sadd(@Nonnull String key, T... value) {
        Set<T> stringSet = cacheSetMap.computeIfAbsent(key, k -> new Cache<>(new HashSet<>())).get();
        if (stringSet != null){
            stringSet.addAll(Set.of(value));
        }
    }

    /**
     * 获取某集所有数据
     * @param key 键
     * @return 数据集
     */
    public Set<T> smembers(@Nonnull String key) {
        return cacheSetMap.containsKey(key) ? cacheSetMap.get(key).get() : Collections.emptySet();
    }

    /**
     * 删除集里的元素
     * @param key 键
     * @param value 值
     */
    @SafeVarargs
    public final void srem(@Nonnull String key, T... value) {
        smembers(key).removeAll(Set.of(value));
    }

    /**
     * 随机出栈一个值
     * @param key 键
     * @return 值
     */
    @Nullable
    public T spop(@Nonnull String key) {
        Set<T> smembers = smembers(key);
        int size;
        if ((size = smembers.size()) == 0) return null;
        int randomInt = random.nextInt(size);
        Iterator<T> iterator = smembers.iterator();
        while (iterator.hasNext()){
            T value = iterator.next();
            if (randomInt-- == 0){
                iterator.remove();
                return value;
            }
        }
        return null;
    }

    /**
     * 获取集合里面的元素数量
     * @param key 键
     * @return 数量
     */
    public int scard(@Nonnull String key) {
        return smembers(key).size();
    }

}
