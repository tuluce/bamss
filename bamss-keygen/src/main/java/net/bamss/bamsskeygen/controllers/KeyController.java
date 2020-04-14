package net.bamss.bamsskeygen.controllers;

import net.bamss.bamsskeygen.connections.RedisConnection;
import net.bamss.bamsskeygen.models.Key;
import net.bamss.bamsskeygen.util.AsyncService;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@RestController
public class KeyController {

    @Autowired
    private AsyncService service;

    private static final JedisPool jedisPool = RedisConnection.getRedisPool();
    private static final int minKeyNumber = 100;
    private static final int maxKeyNumber = 1000;
    private static final int keyLength = 6;


    @GetMapping("/cached")
    public ResponseEntity<Key> getKeyCached() {
        Jedis jedis = jedisPool.getResource();

        if (jedis != null) {

            Long size = jedis.scard("keys");
            if(size <= minKeyNumber){
                service.generateKeys(minKeyNumber, maxKeyNumber, keyLength);
            }

            String shortKey = jedis.spop("keys");

            return new ResponseEntity<>(new Key(shortKey), HttpStatus.OK);

        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @GetMapping("/")
    public ResponseEntity<Key> getKey() {
        String key = RandomStringUtils.randomAlphanumeric(keyLength);
        return new ResponseEntity<>(new Key(key), HttpStatus.OK);
    }
    
}
