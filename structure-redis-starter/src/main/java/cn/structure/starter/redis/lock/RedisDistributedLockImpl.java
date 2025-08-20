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
package cn.structure.starter.redis.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * 分布式锁实现类
 * </p>
 *
 * @author chuck
 */
public class RedisDistributedLockImpl implements IDistributedLock {

    private final Logger logger = LoggerFactory.getLogger(RedisDistributedLockImpl.class);

    private final RedisTemplate<String, Object> redisTemplate;

    private final ThreadLocal<String> lockFlag = new ThreadLocal<String>();

    public RedisDistributedLockImpl(RedisTemplate<String, Object> redisTemplate) {
        super();
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean lock(String key, long expire, int retryTimes, long sleepMillis) {
        boolean result = setRedis(key, expire);
        // 如果获取锁失败，按照传入的重试次数进行重试
        while ((!result) && retryTimes-- > 0) {
            try {
                logger.debug("lock failed, retrying...{}", retryTimes);
                Thread.sleep(sleepMillis);
            } catch (InterruptedException e) {
                return false;
            }
            result = setRedis(key, expire);
        }
        return result;
    }

    private boolean setRedis(String key, long expire) {
        try {
            List<String> keys = new ArrayList<>();
            keys.add(key);
            List<Object> args = new ArrayList<>();
            args.add(lockFlag.get());
            args.add(expire);
            DefaultRedisScript<Long> script = new DefaultRedisScript<>();
            script.setResultType(Long.class);
            //获取lua文件方式
            script.setScriptSource(new ResourceScriptSource(new ClassPathResource("script/lock.lua")));
            Long execute = redisTemplate.execute(script, keys, args);
            return execute != null && execute > 0;
        } catch (Exception e) {
            logger.error("set redis proceed an exception", e);
        }
        return false;
    }

    @Override
    public boolean releaseLock(String key) {
        // 释放锁的时候，有可能因为持锁之后方法执行时间大于锁的有效期，此时有可能已经被另外一个线程持有锁，所以不能直接删除
        try {
            List<String> keys = new ArrayList<>();
            keys.add(key);
            List<String> args = new ArrayList<>();
            args.add(lockFlag.get());
            DefaultRedisScript<Long> script = new DefaultRedisScript<>();
            script.setResultType(Long.class);
            //获取lua文件方式
            script.setScriptSource(new ResourceScriptSource(new ClassPathResource("script/unLock.lua")));
            Long execute = redisTemplate.execute(script, keys, args);
            return execute != null && execute > 0;
        } catch (Exception e) {
            logger.error("release lock proceed an exception", e);
        }
        return false;
    }
}
