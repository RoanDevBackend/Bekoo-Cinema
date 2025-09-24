package org.bekoocinema.repository.impl;

import lombok.RequiredArgsConstructor;
import org.bekoocinema.repository.RedisRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisRepositoryImpl implements RedisRepository {

    final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void setTimeToLive(String key, Long time) {
        redisTemplate.expire(key, time, TimeUnit.SECONDS);
    }

    @Override
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
