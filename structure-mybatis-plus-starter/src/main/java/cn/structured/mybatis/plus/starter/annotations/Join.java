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


import cn.structured.mybatis.plus.starter.enums.JoinTypeEnum;

import java.lang.annotation.*;

/**
 * <p>
 * 字段需要关联
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/8/2 14:39
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface Join {

    boolean result() default false;

    /**
     * 联表类型
     *
     * @return {@link JoinTypeEnum}
     */
    JoinTypeEnum joinType() default JoinTypeEnum.JOIN;

    /**
     * 联表的目标表
     *
     * @return {@link Class}
     */
    Class<?> joinTarget() default Object.class;

    /**
     * 联表条件
     *
     * @return {@link JoinCondition}
     */
    JoinCondition[] value() default {};

    /**
     * 别名
     *
     * @return {@link String}
     */
    String aliasName() default "";

    /**
     * 返回结果
     *
     * @return {@link String}
     */
    String[] columns() default {};

    /**
     * 组
     *
     * @return {@link Class}
     */
    Class<?>[] group() default {};

}
