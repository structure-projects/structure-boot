package cn.structure.common.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <p>
 *     出参 : 计数 - VO
 * </p>
 * @author chuck
 * @version 1.0.1
 * @since 2020-12-26
 */
@ApiModel(description = "出参 : 计数实体 - VO")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResCountVO {

    @ApiModelProperty(value = "数量",example = "0")
    private Integer count;
}
