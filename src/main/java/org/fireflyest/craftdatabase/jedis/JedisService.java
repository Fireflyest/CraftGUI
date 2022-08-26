package org.fireflyest.craftdatabase.jedis;

import redis.clients.jedis.Jedis;

import javax.annotation.Nonnull;

/**
 * @author Fireflyest
 * @since 2022/8/25
 */
public class JedisService {

    private final Jedis jedis;
    private final String keyOutset;

    public JedisService(Jedis jedis, String pluginName) {
        this.jedis = jedis;
        this.keyOutset = String.format("minecraft.plugin.%s.", pluginName);
    }

    /**
     * 设置数据
     * @param key 键
     * @param value 值
     */
    public void set(@Nonnull String key, String value){
        jedis.set(keyOutset + key, value);
    }

    /**
     * 设置限时数据
     * @param key 键
     * @param seconds 秒
     * @param value 值
     */
    public void setex(@Nonnull String key, long seconds, String value){
        jedis.setex(keyOutset + key, seconds, value);
    }

    /**
     * 获取数据
     * @param key 键
     * @return 值
     */
    public String get(@Nonnull String key){
        return jedis.get(keyOutset + key);
    }


}
