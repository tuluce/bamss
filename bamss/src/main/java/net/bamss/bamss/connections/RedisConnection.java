package net.bamss.bamss.connections;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicInteger;

public class RedisConnection {
    private static int MAX_IDLE;
    private static int MAX_TOTAL;
    private static AtomicInteger connectionCount = new AtomicInteger(0);
    private static JedisPool jedisPool;

    private static void initPool() throws URISyntaxException {
        URI redisURI = new URI(System.getenv("REDIS_URL"));
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(MAX_IDLE);
        jedisPoolConfig.setMaxTotal(MAX_TOTAL + 2);
        jedisPool = new JedisPool(jedisPoolConfig, redisURI);
    }

    public static Jedis get() {
        if (jedisPool == null) {
            try {
                initPool();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        if (connectionCount.get() >= MAX_IDLE) {
            return null;
        }
        connectionCount.incrementAndGet();
        return jedisPool.getResource();
    }

    public static void release(Jedis connection) {
        if (connection != null) {
            connection.close();
            connectionCount.decrementAndGet();
        }
    }
}
