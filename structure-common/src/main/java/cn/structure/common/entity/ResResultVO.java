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
package cn.structure.common.entity;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "出参：返回结果 - VO")
public class ResResultVO<T> implements IResult {

    /**
     * 业务状态码
     */
    @Schema(description = "业务状态码", example = "SUCCESS")
    private String code;

    /**
     * 返回的消息内容
     */
    @Schema(description = "返回的消息内容", example = "成功！")
    private String message;

    /**
     * 业务是否成功
     */
    @Schema(description = "业务是否成功", example = "true")
    private Boolean success;

    /**
     * 返回的数据
     */
    @Schema(description = "返回的数据", example = "{}")
    private T data;

    /**
     * 系统响应的时间戳
     */
    @Schema(description = "系统响应的时间戳", example = "112345644446")
    private Long timestamp;

}
