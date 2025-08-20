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
package cn.structured.mybatisplus.generate.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 组织职务
 * </p>
 *
 * @author chuck
 * @since 2021-08-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("org_post")
public class OrgPost implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId("id")
    private Integer id;

    /**
     * 职务名
     */
    @TableField("post_name")
    private String postName;

    /**
     * 父级职务ID
     */
    @TableField("parent_id")
    private Integer parentId;

    /**
     * 排序
     */
    @TableField("sort")
    private Integer sort;

    /**
     * 是否删除:0 否,1是
     */
    @TableField(value = "is_del", fill = FieldFill.INSERT)
    @TableLogic
    private Boolean del;

    /**
     * 创建人
     */
    @TableField(value = "create_id", fill = FieldFill.INSERT)
    private Integer createId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 最后修改人
     */
    @TableField(value = "update_id", fill = FieldFill.INSERT_UPDATE)
    private Integer updateId;

    /**
     * 最后修改时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


}
