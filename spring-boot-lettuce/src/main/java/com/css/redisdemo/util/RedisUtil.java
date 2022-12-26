package com.css.redisdemo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by jiming.jing on 2021/8/24
 */
@Component
public class RedisUtil {

    /**
     * redisTemplate.opsForValue(); //操作字符串
     * redisTemplate.opsForHash();  //操作hash
     * redisTemplate.opsForList();  //操作list
     * redisTemplate.opsForSet();   //操作set
     * redisTemplate.opsForZSet();  //操作有序set
     */

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 模糊查询keys
     *
     * @param pattern
     * @return set
     */
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * 计数器
     *
     * @param key
     */
    public void increment(String key) {
        redisTemplate.opsForValue().increment(key, 1);
    }

    /**
     * 可设置步长计数器
     * @param key
     * @param step
     */
    public Long increment(String key, Long step) {
        return redisTemplate.opsForValue().increment(key, step);
    }


    /**
     * 批量删除对应的value
     * @param keys
     */
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * 批量模糊删除key
     * @param pattern
     */
    public void removePattern(final String pattern) {
        Set<Serializable> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0) {
            redisTemplate.delete(keys);
        }
    }

    /**
     * 删除对应的value
     * @param key
     */
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    /**
     * 判断缓存中是否有对应的key
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 根据key读取Object
     * @param key
     * @return
     */
    public Object get(final String key) {
        Object obj = null;
        //redisTemplate.setValueSerializer(new StringRedisSerializer());
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        obj = operations.get(key);
        return obj;
    }

    /**
     * 写入缓存
     * @param key
     * @param t
     * @param <T>
     * @return
     */
    public <T> boolean set(final String key, T t) {
        boolean result = false;
        try {
            ValueOperations<Serializable, T> operations = redisTemplate.opsForValue();
            operations.set(key, t);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 写入缓存
     * @param key
     * @param t
     * @param expireTime
     * @param <T>
     * @return
     */
    public <T> boolean set(final String key, T t, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, t);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 添加Map
     * @param key
     * @param value
     * @return
     */
    public boolean hmset(String key, Map<String, Object> value) {
        boolean result = false;
        try {
            redisTemplate.opsForHash().putAll(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 批量设置hash键值对
     * @param key
     * @param dataMap
     */
    public void putAll(String key, Map<String, Object> dataMap) {
        redisTemplate.boundHashOps(key).putAll(dataMap);
    }

    /**
     * 设置hash键值对
     * @param key
     * @param hkey
     * @param hval
     */
    public void put(String key, String hkey, Object hval) {
        redisTemplate.boundHashOps(key).put(hkey, hval);
    }


    /**
     * 获取Map
     * @param key
     * @return
     */
    public Map<String, Object> hmget(String key) {
        Map<String, Object> map = null;
        try {
            map = redisTemplate.opsForHash().entries(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public List<Object> hmget(List<String> keys) {
        List<Object> list = this.redisTemplate.opsForValue().multiGet(keys);
        return list;
    }

    public void pipeSet(List<Map<String, String>> list, TimeUnit timeUnit, int timeout) {
        redisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public <K, V> Object execute(RedisOperations<K, V> redisOperations) throws DataAccessException {
                for (Map<String, String> map : list) {
                    redisTemplate.opsForValue().set(map.get("key"), map.get("value"), timeout, timeUnit);
                }
                return null;
            }
        });
    }

    public List<Object> pipeGet(List<String> keys) {
        List<Object> list = redisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            public <K, V> Object execute(RedisOperations<K, V> redisOperations) throws DataAccessException {
                for (String key : keys) {
                    redisOperations.opsForValue().get(key);
                }
                return null;
            }
        });
        return list;
    }

    public List<String> pipeBatchGet(List<String> keys) {
        List<Object> list = redisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                StringRedisConnection connection = (StringRedisConnection) redisConnection;
                for (String key : keys) {
                    connection.get(key);
                }
                return null;
            }
        });
        List<String> collect = list.stream().map(val -> String.valueOf(val)).collect(Collectors.toList());
        return collect;
    }

}
