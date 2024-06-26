package cn.structure.common.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;


/**
 * <p>
 * 出参 ： 返回结果封装 - VO
 * 对微服务比较友好
 * </p>
 *
 * @param <T> data部分的类型
 * @author chuck
 * @version 1.0.1
 * @since 2020-12-26
 */
@ApiModel(description = "出参 ： 返回结果封装 - VO")
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ResultVO<T> implements IResult {
    /**
     * 系统级别的CODE码
     */
    @ApiModelProperty(value = "系统级别的CODE码", example = "SUCCESS")
    private String code;
    /**
     * 系统级别的消息内容
     */
    @ApiModelProperty(value = "系统级别的消息内容", example = "成功！")
    private String msg;
    /**
     * 业务级别的code码
     */
    @ApiModelProperty(value = "业务级别的code码", example = "SUCCESS")
    private String subCode;
    /**
     * 业务级别的消息内容
     */
    @ApiModelProperty(value = "业务级别的消息内容", example = "成功！")
    private String subMsg;
    /**
     * 系统响应的时间戳
     */
    @ApiModelProperty(value = "系统响应的时间戳", example = "112345644446")
    private Long timestamp;
    /**
     * 系统响应的回参数据是一个泛型
     */
    @ApiModelProperty(value = "系统响应的回参数据是一个泛型", example = "{}")
    private T data;

    @ApiModelProperty(value = "业务是否成功", example = "{}")
    private Boolean success;
}

