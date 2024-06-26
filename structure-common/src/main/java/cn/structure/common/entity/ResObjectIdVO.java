package cn.structure.common.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <p>
 *     出参：ID 实体 - VO
 * </p>
 * @author chuck
 * @since 2020-12-26
 * @version 1.0.1
 */
@ApiModel(description = "出参：ID 实体 - VO")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResObjectIdVO {

    @ApiModelProperty(value = "ID",example = "1")
    private Object id;
}
