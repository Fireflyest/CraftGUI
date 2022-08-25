package org.fireflyest.craftdatabase.jedis;

import redis.clients.jedis.JedisPooled;

import javax.annotation.Nonnull;

/**
 * @author Fireflyest
 * @since 2022/8/25
 */
public class JedisService {

    private final JedisPooled jedisPooled;
    private final String keyOutset;

    public JedisService(JedisPooled jedisPooled, String pluginName) {
        this.jedisPooled = jedisPooled;
        this.keyOutset = String.format("minecraft.plugin.%s.", pluginName);
    }

    public void set(@Nonnull String key, String value){
        jedisPooled.set(keyOutset + key, value);
    }

    public String get(@Nonnull String key){
        return jedisPooled.get(keyOutset + key);
    }

}
