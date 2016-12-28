package com.example.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by meijun on 2016/11/14.
 */
@RestController
@CrossOrigin
public class RedisStringController {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Resource(name="stringRedisTemplate")
    ValueOperations<String, String> valOpsStr;

    @RequestMapping("set")
    public String setKeyAndValue(String key, String value) {
        valOpsStr.set(key, value);
        return "Set OK!";
    }

    @RequestMapping("get")
    public String getKey(String key) {
        return valOpsStr.get(key);
    }
}
