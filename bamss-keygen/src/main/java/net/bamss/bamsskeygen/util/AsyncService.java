package net.bamss.bamsskeygen.util;

import net.bamss.bamsskeygen.connections.RedisConnection;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class AsyncService {
    private static final JedisPool jedisPool = RedisConnection.getRedisPool();

    @Async("asyncExecutor")
    public void generateKeys(int minKeyNumber, int maxKeyNumber, int keyLength){
        Jedis jedis = jedisPool.getResource();
        int cur = minKeyNumber;

        while(cur < maxKeyNumber){
            String generatedString = RandomStringUtils.randomAlphanumeric(keyLength);
            if(!jedis.sismember("keys", generatedString)){
                jedis.sadd("keys", generatedString);
                cur += 1;
            }
        }
    }
}
