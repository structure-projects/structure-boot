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
package cn.structure.example.redis.service;

import cn.structure.example.redis.entity.RedisLockBo;
import cn.structure.starter.redis.annotation.RedisLock;
import cn.structure.starter.redis.lock.IDistributedLock;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * redis Lock 的service
 *
 * @author chuck
 */
@Service
public class RedisLockService {

    @Resource
    private IDistributedLock iDistributedLock;

    /**
     * 注解使用redis锁 参数为非对象的使用
     *
     * @param key
     */
    @RedisLock("#key")
    public void redisLock(String key) {
        System.out.println("redisLock ----> key = " + key);
    }

    /**
     * 注解使用redis锁 参数为对象的使用
     *
     * @param redisLockBo
     */
    @RedisLock("#key")
    public void redisLock(RedisLockBo redisLockBo) {
        System.out.println("redisLock ----> redisLockBo ----> key = " + redisLockBo.getKey());
    }

    /**
     * 手动处理分布式锁
     */
    public void redisLock() {
        //redis - key
        String key = "123456";
        //获取锁
        boolean lock = iDistributedLock.lock(key);
        //判断是否获取到锁
        if (!lock) {
            return;
        }
        //todo 执行您的业务
        //释放锁
        iDistributedLock.releaseLock(key);
    }

}
