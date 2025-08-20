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

import org.redisson.api.SortOrder;

import java.lang.annotation.*;

/**
 * <p>
 * 读缓存注解默认降序执行结构为 sort desc + 分页值存key 基础
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2020-12-23
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RListCache {
    /**
     * key
     */
    String key() default "";

    /**
     * 设置分页的大小
     */
    String page() default "1";

    /**
     * 默认大小如果是集合需要设置大小
     */
    String size() default "20";

    /**
     * 排序
     */
    SortOrder sort() default SortOrder.DESC;

    /**
     * 和map关联的KEY
     */
    String mapKey() default "";

    /**
     * 读取集合的data类型是data 还是key
     */
    CList.ListType value() default CList.ListType.DATA;
}
