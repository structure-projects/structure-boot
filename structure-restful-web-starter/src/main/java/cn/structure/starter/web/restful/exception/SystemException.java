package cn.structure.starter.web.restful.exception;

import lombok.*;

/**
 * <p>
 * 系统异常类
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021-01-03
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
