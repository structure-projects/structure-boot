package cn.structured.mybatis.plus.starter.entity;


import cn.structured.mybatis.plus.starter.annotations.LogicDelete;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 *
 */
@Setter
@Getter
public class BaseEntity<ID> {

    /** 聚合根唯一标识 */
    @TableId(value = "id", type = IdType.AUTO)
    protected ID id;

    /** 创建时间 */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    protected LocalDateTime createTime;

    /** 删除标识 */
    @LogicDelete
    @TableField(value = "is_deleted", fill = FieldFill.INSERT)
    protected Boolean deleted;

    /** 创建人ID */
    protected ID createBy;

    /** 更新时间 */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    protected LocalDateTime updateTime;

    /** 更新人ID */
    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    protected ID updateBy;


}
