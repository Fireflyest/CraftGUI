package org.fireflyest.craftdatabase.jedis;

import redis.clients.jedis.JedisPooled;

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

    public JedisService create(String pluginName){
        return new JedisService(jedisPooled, pluginName);
    }

}
