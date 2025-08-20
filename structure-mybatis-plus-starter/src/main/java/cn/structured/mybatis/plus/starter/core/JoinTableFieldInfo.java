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
package cn.structured.mybatis.plus.starter.core;

import lombok.Data;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * <p>
 * 联表字段属性
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/8/3 11:41
 */
@Data
public class JoinTableFieldInfo {

    /**
     * 属性
     *
     * @since 3.3.1
     */
    private Field field;
    /**
     * 字段名
     */
    private String column;
    /**
     * 属性名
     */
    private String property;
    /**
     * 属性类型
     */
    private Class<?> propertyType;

    /**
     * sql条件组
     */
    private Map<Class<?>, String> sqlConditionGroup;
}
