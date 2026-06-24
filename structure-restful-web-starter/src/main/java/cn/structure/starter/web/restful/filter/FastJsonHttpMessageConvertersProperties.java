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
package cn.structure.starter.web.restful.filter;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * FastJson消息转换器配置属性
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2025-06-25
 */
@Getter
@Setter
public class FastJsonHttpMessageConvertersProperties {

    /**
     * 将long转换为String字符串 js没有long类型会有精度丢失
     */
    private boolean longToString = false;

    /**
     * 是否展示值为null的key
     */
    private boolean nullShowValue = false;
}