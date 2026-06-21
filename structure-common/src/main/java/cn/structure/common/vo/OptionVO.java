package cn.structure.common.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 下拉选-VO
 *
 * @author cqliut
 * @version 2023.0731
 * @since 1.0.1
 */
@Data
@Schema(description = "下拉选-VO")
public class OptionVO {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "名称", example = "公共空间")
    private String label;

    @Schema(description = "编码/id", example = "public")
    private Serializable value;

    @Schema(description = "子集", example = "[]")
    private List<OptionVO> children;

}
