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
 * 返回结果code枚举
 * </p>
 *
 * @author chuck
 * @version 1.0.0
 * @since 2019-10-20
 */
@Getter
public enum ResultCodeEnum {
    /**
     * 验证成功
     */
    SUCCESS("成功！", "SUCCESS"),
    /**
     * 内部业务错误
     */
    FAIL("内部业务错误", "FAIL"),
    /**
     * 资源不存在
     */
    NOT_FOUND("资源不存在", "NOT_FOUND"),
    /**
     * 断路
     */
    FALLBACK("断路", "FALLBACK"),
    /**
     * 权限验证失败
     */
    UNAUTHORIZED("无权访问", "UNAUTHORIZED"),
    /**
     * 格式校验失败
     */
    VERIFICATION_FAILED("格式校验失败", "VERIFICATION_FAILED"),
    /**
     * 格式转换失败
     */
    CONVERT_FAILED("格式转换失败", "CONVERT_FAILED"),
    /**
     * 未知的
     */
    ERR("异常", "ERR");

    private final String msg;
    private final String code;

    ResultCodeEnum(String msg, String code) {
        this.msg = msg;
        this.code = code;
    }
}
