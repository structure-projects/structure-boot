package cn.structure.common.enums;

import lombok.Getter;

/**
 * <p>
 *     鉴权异常返回枚举
 * </p>
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
