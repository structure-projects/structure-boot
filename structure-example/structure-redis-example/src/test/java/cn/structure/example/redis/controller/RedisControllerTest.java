package cn.structure.example.redis.controller;

import cn.structure.example.redis.config.AbstractIntegrationTest;
import cn.structure.example.redis.config.MockRedisConfig;
import cn.structure.example.redis.service.RedisLockService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Import(MockRedisConfig.class)
class RedisControllerTest extends AbstractIntegrationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        assertNotNull(applicationContext);
    }

    @Test
    void redisControllerBeanExists() {
        assertNotNull(applicationContext.getBean("redisController"));
    }

    @Test
    void redisLockServiceBeanExists() {
        assertNotNull(applicationContext.getBean(RedisLockService.class));
    }
}
