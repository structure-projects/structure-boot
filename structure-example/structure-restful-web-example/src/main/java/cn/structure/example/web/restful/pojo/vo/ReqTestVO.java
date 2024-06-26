package cn.structure.example.web.restful.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 入参：测试 - VO
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/1/3 14:15
 */
@Data
@ApiModel(description = "入参：测试 - VO")
public class ReqTestVO {

    @NotNull(message = "ID不能为NULL")
    @ApiModelProperty(value = "ID")
    private String id;

    @NotBlank(message = "名字不能为空")
    @ApiModelProperty(value = "名字")
    private String name;
}
