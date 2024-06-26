package cn.structure.starter.web.restful.exception;

import lombok.*;

/**
 * <p>
 *     系统异常类
 * </p>
 * @author chuck
 * @since 2021-01-03
 * @version 1.0.1
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SystemException extends RuntimeException {

    protected String code;

    protected String msg;
}
