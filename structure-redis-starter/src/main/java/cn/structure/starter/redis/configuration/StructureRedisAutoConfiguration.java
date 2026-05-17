/*
 * Copyright (c) 2025 Structure Boot
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.structure.starter.redis.configuration;

import cn.structure.starter.redis.lock.IDistributedLock;
import cn.structure.starter.redis.lock.RedisDistributedLockImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * <p>
 * redis自动装配类
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 */
@Slf4j
@AutoConfiguration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@Import(DistributedLockAspectConfiguration.class)
public class StructureRedisAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean(StringRedisTemplate.class)
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        log.info("[StructureRedisAutoConfiguration] 创建RedisStringTemplate");
        return new StringRedisTemplate(redisConnectionFactory);
    }

    @Bean
    @ConditionalOnBean(StringRedisTemplate.class)
    public IDistributedLock iDistributedLock(StringRedisTemplate stringRedisTemplate) {
        log.info("[StructureRedisAutoConfiguration] 创建Redis分布式锁实现类 - RedisDistributedLockImpl");
        return new RedisDistributedLockImpl(stringRedisTemplate);
    }

}
