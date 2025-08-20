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
package cn.structure.starter.redisson.enumerate;

/**
 * <p>
 * 锁模式枚举
 * </P>
 */
public enum LockModelEnum {
    /**
     * 可重入锁
     */
    REENTRANT,
    /**
     * 公平锁
     */
    FAIR,
    /**
     * 联锁
     */
    MULTIPLE,
    /**
     * 红锁
     */
    REDLOCK,
    /**
     * 读锁
     */
    READ,
    /**
     * 写锁
     */
    WRITE,
    /**
     * 自动模式,当参数只有一个.使用 REENTRANT 参数多个 MULTIPLE
     */
    AUTO
}
