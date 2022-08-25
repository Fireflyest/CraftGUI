package org.fireflyest.craftdatabase.jedis;

import redis.clients.jedis.JedisPooled;

import javax.annotation.Nonnull;

/**
 * @author Fireflyest
 * @since 2022/8/25
 */
public class JedisServiceFactory {

    private static final JedisPooled jedisPooled = new JedisPooled("localhost", 6379);

    private JedisServiceFactory() {
    }

    public static void close(){
        jedisPooled.close();
    }

    public JedisService create(@Nonnull String pluginName){
        return new JedisService(jedisPooled, pluginName);
    }

}
