package cn.structure.common.enums;

import lombok.Getter;

/**
 * <p>
 * log类型枚举
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2020-12-26
 */
@Getter
public enum LogEnums {

    /**
     * 控制器日志
     */
    CONTROLLER,

    /**
     * 方法日志
     */
    FUNCTION,

    /**
     * 控制台异常
     */
    CONTROLLER_ERROR,

    /**
     * 方法异常
     */
    FUNCTION_ERROR,

}
