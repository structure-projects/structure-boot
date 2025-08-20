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
package cn.structure.starter.redisson.utils;

import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 分布式锁接口
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2020-12-23
 */
public interface IDistributedLocker {

    /**
     * 通过key获取锁 {@link RLock}
     *
     * @param lockKey 锁的key
     * @return org.redisson.api.RLock
     */
    RLock lock(String lockKey);

    /**
     * 通过key获取锁 {@link RLock}并设置一个超时时间
     *
     * @param lockKey 锁的key
     * @param timeout 超时时间
     * @return org.redisson.api.RLock
     */
    RLock lock(String lockKey, int timeout);

    /**
     * 通过key获取锁 {@link RLock}并设置一个超时时间,并设置一个时间单位
     *
     * @param lockKey 锁的key
     * @param unit    时间的单位
     * @param timeout 超时时间
     * @return org.redisson.api.RLock
     */
    RLock lock(String lockKey, TimeUnit unit, int timeout);

    /**
     * 通过key获取锁 {@link RLock}并设置一个超时时间,并设置一个时间单位和一个等待时间
     *
     * @param lockKey
     * @param unit
     * @param waitTime
     * @param leaseTime
     * @return org.redisson.api.RLock
     */
    boolean tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime);

    /**
     * <p>
     * 关闭锁
     * </P>
     *
     * @param lockKey 锁的key
     */
    void unlock(String lockKey);

    /**
     * <p>
     * 关闭锁
     * </P>
     *
     * @param lock 要关闭的锁
     */
    void unlock(RLock lock);
}