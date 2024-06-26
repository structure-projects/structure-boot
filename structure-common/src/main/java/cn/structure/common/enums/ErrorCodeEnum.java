package cn.structure.common.enums;

import lombok.Getter;

/**
 * <p>
 *     系统错误枚举类
 * </p>
 * @author chuck
 * @since 2020-12-26
 * @version 1.0.1
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
