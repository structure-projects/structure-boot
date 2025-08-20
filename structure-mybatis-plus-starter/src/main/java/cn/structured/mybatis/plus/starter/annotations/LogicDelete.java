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
package cn.structured.mybatis.plus.starter.annotations;

import java.lang.annotation.*;

/**
 * <p>
 * 逻辑删除注解
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/8/10 17:15
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface LogicDelete {
    /**
     * 字段名
     *
     * @return {@link String}
     */
    String value() default "";

    /**
     * 删除的值
     *
     * @return {@link Integer}
     */
    int deleteValue() default 1;


}
