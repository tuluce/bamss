package net.bamss.bamss.connections;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.net.URI;
import java.net.URISyntaxException;

public class RedisConnection {
    private static JedisPool jedisPool;

    private static void createInstance() throws URISyntaxException {
        URI redisURI = new URI(System.getenv("REDIS_URL"));
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(4);
        jedisPoolConfig.setMaxTotal(8);
        jedisPool = new JedisPool(jedisPoolConfig, redisURI);
    }

    public static JedisPool getRedisPool() {
        if (jedisPool == null) {
            try {
                createInstance();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return jedisPool;
    }
}
