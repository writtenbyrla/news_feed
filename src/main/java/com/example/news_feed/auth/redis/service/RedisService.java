package com.example.news_feed.auth.redis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;

    // redis에 저장
    public void setValues(String key, String value, Long expiration, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, expiration, timeUnit);
    }

    // key에 해당하는 value를 redis에서 가져옴
    public String getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // key가 redis에 존재하는지 확인
    public boolean keyExists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    // key 삭제
    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }

    // value값 있는지 확인
    public boolean checkExistsValue(String value) {
        return !value.equals("false");
    }

}
