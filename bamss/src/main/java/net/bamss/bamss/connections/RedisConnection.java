package net.bamss.bamss.connections;

import com.mongodb.client.MongoDatabase;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;

public class RedisConnection {

    private static JedisPool pool;
//    private static Jedis cache;
//
//    private static void createInstance() throws URISyntaxException {
//        URI redisURI = new URI(System.getenv("REDIS_URL"));
//        cache = new Jedis(redisURI);
//    }
//
//    public static Jedis getRedisCache() {
//        if (cache == null) {
//            try {
//                createInstance();
//            } catch (URISyntaxException e) {
//                e.printStackTrace();
//            }
//        }
//        return cache;
//    }

    public static void createPool() throws URISyntaxException {
        URI redisURI = new URI(System.getenv("REDIS_URL"));
        pool = new JedisPool(redisURI);
    }

    public static Jedis getResource() {

        if (pool != null) {
            return pool.getResource();
        } else {
            try {
                createPool();
                return pool.getResource();

            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}