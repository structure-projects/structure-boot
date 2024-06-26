package cn.structure.common.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 *     校验失败信息实体
 * </p>
 * @author chuck
 * @since 2020-12-26
 * @version 1.0.1
 */
@Getter
@Setter
@ToString
public class VerificationFailedMsg {
    private String field;
    private String errorMessage;
}