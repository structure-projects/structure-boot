package cn.structure.common.exception;

import lombok.*;

/**
 * <p>
 * 业务公共异常类
 * </p>
 *
 * @author CHUCK
 * @version 1.0.1
 * @since 2020-12-26
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CommonException extends RuntimeException {

    /**
     * 业务code
     */
    protected String code;

    /**
     * 错误描述
     */
    protected String msg;
}
