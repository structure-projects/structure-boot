package cn.structure.example.redis.config;

import cn.structure.starter.redis.lock.IDistributedLock;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import static org.mockito.Mockito.mock;

@Configuration
public class MockRedisConfig {

    @Bean
    @Qualifier("stringRedisTemplate")
    @SuppressWarnings("unchecked")
    public RedisTemplate<String, String> stringRedisTemplate() {
        return mock(RedisTemplate.class);
    }

    @Bean
    public IDistributedLock iDistributedLock() {
        return mock(IDistributedLock.class);
    }
}
