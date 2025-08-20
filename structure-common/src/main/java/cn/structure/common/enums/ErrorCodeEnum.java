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
package cn.structure.common.enums;

import lombok.Getter;

/**
 * <p>
 * 系统错误枚举类
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2020-12-26
 */
@Getter
public enum ErrorCodeEnum {

    /**
     * 未知异常
     */
    UNKNOWN_EXCEPTION("未知异常"),
    /**
     * 系统错误
     */
    SYSTEM_ERROR("系统错误"),
    /**
     * 第三方错误
     */
    THIRD_PARTY_ERROR("第三方异常"),
    /**
     * 逻辑异常
     */
    LOGIC_ERROR("业务异常"),

    COMMON_EXCEPTION("自定义异常");

    private final String errorType;

    ErrorCodeEnum(String errorType) {
        this.errorType = errorType;
    }
}
