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
package cn.structure.starter.redisson.anno;

import cn.structure.starter.redisson.enumerate.LockModelEnum;

import java.lang.annotation.*;

/**
 * <p>
 * 锁注解
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2020-12-23
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Lock {
    /**
     * 锁的模式:如果不设置,自动模式,当参数只有一个.使用 REENTRANT 参数多个 MULTIPLE
     */
    LockModelEnum lockModel() default LockModelEnum.AUTO;

    /**
     * 如果keys有多个,如果不设置,则使用 联锁
     */
    String[] keys() default {};

    /**
     * 锁超时时间,默认30000毫秒(可在配置文件全局设置)
     */
    long lockWatchdogTimeout() default 30000;

    /**
     * 等待加锁超时时间,默认10000毫秒 -1 则表示一直等待(可在配置文件全局设置)
     */
    long attemptTimeout() default 10000;
}
