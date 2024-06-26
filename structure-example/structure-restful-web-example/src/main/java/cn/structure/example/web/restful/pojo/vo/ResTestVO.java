package cn.structure.example.web.restful.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 出参：测试 - VO
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/1/3 14:17
 */
@Data
@ApiModel(description = "出参：测试 - VO")
public class ResTestVO {
    @ApiModelProperty(value = "ID")
    private String id;
    @ApiModelProperty(value = "名字")
    private String name;
}
