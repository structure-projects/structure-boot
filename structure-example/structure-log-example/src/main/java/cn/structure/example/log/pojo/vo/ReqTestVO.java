package cn.structure.example.log.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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

    @ApiModelProperty(value = "ID")
    private String id;

    @ApiModelProperty(value = "名字")
    private String name;
}
