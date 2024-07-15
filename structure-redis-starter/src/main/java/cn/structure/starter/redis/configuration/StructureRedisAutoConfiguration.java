package cn.structure.starter.redis.configuration;

import cn.structure.starter.redis.lock.IDistributedLock;
import cn.structure.starter.redis.lock.RedisDistributedLockImpl;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * <p>
 * redis自动装配类
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@Import(DistributedLockAspectConfiguration.class)
public class StructureRedisAutoConfiguration {

    @Bean
    @ConditionalOnBean(RedisTemplate.class)
    public IDistributedLock iDistributedLock(RedisTemplate redisTemplate) {
        return new RedisDistributedLockImpl(redisTemplate);
    }

}
