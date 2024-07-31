package cn.structure.common.entity;

import cn.structure.common.exception.CommonException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * <p>
 * 出参：返回结果 - VO
 * 对单体比较友好也可以支持微服务
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2020/12/26 21:32
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "出参：返回结果 - VO")
public class ResResultVO<T> implements IResult {

    /**
     * 业务状态码
     */
    @ApiModelProperty(value = "业务状态码", example = "SUCCESS")
    private String code;

    /**
     * 返回的消息内容
     */
    @ApiModelProperty(value = "返回的消息内容", example = "成功！")
    private String message;

    /**
     * 业务是否成功
     */
    @ApiModelProperty(value = "业务是否成功", example = "true")
    private Boolean success;

    /**
     * 返回的数据
     */
    @ApiModelProperty(value = "返回的数据", example = "{}")
    private T data;

    /**
     * 系统响应的时间戳
     */
    @ApiModelProperty(value = "系统响应的时间戳", example = "112345644446")
    private Long timestamp;

}
