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

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * <p>
 * 分布式锁实现类
 * </p>
 *
 * @author chuck
 */
@Slf4j
public class RedisDistributedLockImpl implements IDistributedLock {

    private final StringRedisTemplate stringRedisTemplate;

    private final ThreadLocal<String> lockFlag = new ThreadLocal<>();

    public RedisDistributedLockImpl(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean lock(String key, long expire, int retryTimes, long sleepMillis) {
        String lockValue = UUID.randomUUID().toString() + ":" + Thread.currentThread().getId();
        lockFlag.set(lockValue);
        log.debug("[RedisDistributedLockImpl] 准备获取分布式锁 - key: {}, expire: {}, retryTimes: {}, sleepMillis: {}, lockValue: {}",
                key, expire, retryTimes, sleepMillis, lockValue);
        
        boolean result = setRedis(key, expire);
        while ((!result) && retryTimes-- > 0) {
            try {
                log.debug("[RedisDistributedLockImpl] 获取锁失败，正在重试 - key: {}, retryTimes: {}", key, retryTimes);
                Thread.sleep(sleepMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("[RedisDistributedLockImpl] 获取锁被中断 - key: {}", key);
                lockFlag.remove();
                return false;
            }
            result = setRedis(key, expire);
        }
        
        if (result) {
            log.info("[RedisDistributedLockImpl] 获取分布式锁成功 - key: {}, lockValue: {}", key, lockValue);
        } else {
            log.warn("[RedisDistributedLockImpl] 获取分布式锁失败 - key: {}, lockValue: {}", key, lockValue);
            lockFlag.remove();
        }
        return result;
    }

    private boolean setRedis(String key, long expire) {
        try {
            List<String> keys = new ArrayList<>();
            keys.add(key);
            List<String> args = new ArrayList<>();
            args.add(lockFlag.get());
            args.add(String.valueOf(expire));
            DefaultRedisScript<Long> script = new DefaultRedisScript<>();
            script.setResultType(Long.class);
            script.setScriptSource(new ResourceScriptSource(new ClassPathResource("script/lock.lua")));
            Long execute = stringRedisTemplate.execute(script, keys, args.toArray(new String[0]));
            log.debug("[RedisDistributedLockImpl] Lua脚本执行结果 - key: {}, result: {}", key, execute);
            return execute != null && execute > 0;
        } catch (Exception e) {
            log.error("[RedisDistributedLockImpl] 设置Redis锁发生异常 - key: {}, error: {}", key, e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean releaseLock(String key) {
        log.debug("[RedisDistributedLockImpl] 准备释放分布式锁 - key: {}", key);
        try {
            List<String> keys = new ArrayList<>();
            keys.add(key);
            List<String> args = new ArrayList<>();
            args.add(lockFlag.get());
            DefaultRedisScript<Long> script = new DefaultRedisScript<>();
            script.setResultType(Long.class);
            script.setScriptSource(new ResourceScriptSource(new ClassPathResource("script/unLock.lua")));
            Long execute = stringRedisTemplate.execute(script, keys, args.toArray(new String[0]));
            boolean result = execute != null && execute > 0;
            if (result) {
                log.info("[RedisDistributedLockImpl] 释放分布式锁成功 - key: {}", key);
            } else {
                log.warn("[RedisDistributedLockImpl] 释放分布式锁失败 - key: {}", key);
            }
            return result;
        } catch (Exception e) {
            log.error("[RedisDistributedLockImpl] 释放锁发生异常 - key: {}, error: {}", key, e.getMessage(), e);
            return false;
        } finally {
            lockFlag.remove();
        }
    }
}
