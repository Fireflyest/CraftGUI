package org.fireflyest.craftdatabase.jedis;

import redis.clients.jedis.Jedis;

import javax.annotation.Nonnull;

/**
 * 以后用
 * @author Fireflyest
 * @since 2022/8/25
 */
public class JedisServiceFactory {

    private static final Jedis jedis = new Jedis("localhost", 6379);

    private JedisServiceFactory() {
    }

    public static void close(){
        if (jedis.isConnected())  jedis.close();
    }

    public static JedisService create(@Nonnull String pluginName){
        return new JedisService(jedis, pluginName);
    }

}
