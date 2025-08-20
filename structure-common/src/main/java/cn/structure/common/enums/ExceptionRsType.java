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
 * 鉴权异常返回枚举
 * </p>
 *
 * @author chuck
 */
@Getter
public enum ExceptionRsType {
    /**
     * 用户未登录
     */
    NOT_LOGGED_IN("用户未登录", "NOT_LOGGED_IN"),
    /**
     * 无效TOKEN
     */
    INVALID_AUTHENTICATION("无效的token", "INVALID_AUTHENTICATION"),
    /**
     * 权限被拒绝
     */
    PERMISSION_DENIED("无权限", "PERMISSION_DENIED"),
    ;

    private final String msg;
    private final String code;

    ExceptionRsType(String msg, String code) {
        this.msg = msg;
        this.code = code;
    }
}
