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
 * 对redis-Map存储结构封装map缓存注解 可以搭配 {@link CList} List 结构和对象结构混合使用
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2020-12-23
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CMap {
    /**
     * 更新Map的key
     */
    String mapKey() default "";

    /**
     * 是否存入map集合中
     */
    boolean isMap() default false;

    /**
     * Map的时效设置 {@link CTime}
     */
    CTime time() default @CTime();
}