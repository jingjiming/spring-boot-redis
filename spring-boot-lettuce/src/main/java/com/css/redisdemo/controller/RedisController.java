package com.css.redisdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jiming.jing on 2022/12/26
 */
@RestController
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/set")
    public String set() {
        redisTemplate.opsForValue().set("k1", "test");
        return "success!";
    }

    @GetMapping("/get")
    public String get() {
        String s = (String) redisTemplate.opsForValue().get("k1");
        return s;
    }
}
