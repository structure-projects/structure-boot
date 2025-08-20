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

import cn.structured.mybatis.plus.starter.enums.JoinResultEnum;
import cn.structured.mybatis.plus.starter.enums.JoinTypeEnum;

import java.lang.annotation.*;

/**
 * <p>
 *
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/8/5 14:28
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface FieldJoin {

    /**
     * 联表类型
     *
     * @return {@link JoinTypeEnum}
     */
    JoinTypeEnum joinType() default JoinTypeEnum.JOIN;

    /**
     * 联表返回的类型 如果是集合则表示泛型的值
     *
     * @return {@link Class}
     */
    Class<?> result() default Object.class;


    /**
     * 联表结果类型
     *
     * @return {@link Class}
     */
    JoinResultEnum type() default JoinResultEnum.ONE;

    /**
     * 要链的表
     *
     * @return {@link Join}
     */
    Join[] value() default {};
}
