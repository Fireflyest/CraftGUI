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

    public void set(@Nonnull String key, String value){
        jedis.set(keyOutset + key, value);
    }

    public String get(@Nonnull String key){
        return jedis.get(keyOutset + key);
    }

}
