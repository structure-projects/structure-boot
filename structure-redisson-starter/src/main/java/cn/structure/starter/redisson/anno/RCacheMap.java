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

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 读取map集合中的缓存
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2020-12-23
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RCacheMap {
    /**
     * mapKey
     */
    String mapKey() default "";

    /**
     * 关联情况下的KEY
     */
    CList list() default @CList;

    /**
     * key
     */
    String key() default "";

    /**
     * 时间配置 是否有时效果
     */
    boolean isTime() default false;

    /**
     * 时间限制
     */
    long time() default 0L;

    /**
     * 时间类型
     */
    TimeUnit timeType() default TimeUnit.MINUTES;
}
