package com.css.redisdemo.controller;

import com.css.redisdemo.util.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jiming.jing on 2022/12/26
 */
@RestController
@RequestMapping("/jedis")
public class JedisController {

    @Autowired
    private JedisUtil jedisUtil;

    @RequestMapping("/set")
    public String set() {
        jedisUtil.set("k1", "jedis");
        return "success!";
    }

    @RequestMapping("/get")
    public String get() {
        return jedisUtil.get("k1");
    }
}
