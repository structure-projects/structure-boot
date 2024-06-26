package cn.structure.common.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 *     controller日志实体
 * </p>
 * @author chuck
 * @since 2020-12-26
 * @version 1.0.1
 */
@Getter
@Setter
@ToString
public class ControllerLog extends BaseLog {

    private String method;

    private String url;

    private String ipAddress;
}
