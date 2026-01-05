/*
 * Copyright (c) 2025 Structure Boot
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.structure.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * <p>
 * 出参分页
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/6/26 13:45
 */
@Data
@ApiModel(description = "出参分页")
public class ResPage<T> {

    @ApiModelProperty(value = "当前页")
    private Long current;
    @ApiModelProperty(value = "总页数")
    private Long pages;
    @ApiModelProperty(value = "数据")
    private List<T> records;
    @ApiModelProperty(value = "每页数量")
    private Long size;
    @ApiModelProperty(value = "总记录数")
    private Long total;


}
