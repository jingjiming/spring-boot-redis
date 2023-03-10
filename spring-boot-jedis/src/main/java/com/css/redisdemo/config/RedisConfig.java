package com.css.redisdemo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.timeout}")
    private int timeout;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.jedis.pool.max-active}")
    private int maxTotal;

    @Value("${spring.redis.jedis.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.redis.jedis.pool.min-idle}")
    private int minIdle;

    @Value("${spring.redis.jedis.pool.max-wait}")
    private long maxWaitMillis;

    @Value("${spring.redis.jedis.pool.time-between-eviction-runs}")
    private long timeBetweenEvictionRunsMillis;

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        jedisPoolConfig.setNumTestsPerEvictionRun(50);
        jedisPoolConfig
                .setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        jedisPoolConfig
                .setMinEvictableIdleTimeMillis(1800000);
        jedisPoolConfig
                .setSoftMinEvictableIdleTimeMillis(10000);
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setTestWhileIdle(true);
        jedisPoolConfig.setTestOnReturn(true);
        jedisPoolConfig.setBlockWhenExhausted(true);

        return jedisPoolConfig;
    }

    @Bean
    public JedisPool jedisPoolFactory() {
        JedisPoolConfig jedisPoolConfig = jedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        // ???????????????????????????, false?????????,true??????????????????, ??????true
        jedisPoolConfig.setBlockWhenExhausted(true);
        // ????????????pool???jmx????????????, ??????true
        jedisPoolConfig.setJmxEnabled(true);
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout/*, password*/);
        return jedisPool;
    }

}
