package cn.structure.common.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 校验失败信息实体
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2020-12-26
 */
@Getter
@Setter
@ToString
public class VerificationFailedMsg {
    private String field;
    private String errorMessage;
}