package net.bamss.bamss.connections;

import redis.clients.jedis.Jedis;

import java.net.URI;
import java.net.URISyntaxException;

public class RedisConnection {
    private static Jedis cache;

    private static void createInstance() throws URISyntaxException {
        URI redisURI = new URI(System.getenv("REDIS_URL"));
        cache = new Jedis(redisURI);
    }

    public static Jedis getRedisCache() {
        if (cache == null) {
            try {
                createInstance();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return cache;
    }
}
