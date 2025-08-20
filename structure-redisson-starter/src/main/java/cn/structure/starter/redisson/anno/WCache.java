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

/**
 * <p>
 * 写缓存注解
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2020-12-23
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WCache {
    /**
     * keyName
     */
    String key() default "";

    /**
     * 是否對象緩存
     */
    boolean isObjCache() default true;

    /**
     * 更新集合配置
     */
    CList list() default @CList;

    /**
     * Map 配置
     */
    CMap map() default @CMap;

    /**
     * 时间配置
     */
    CTime time() default @CTime;
}
