package cn.structure.common.entity;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

/**
 *
 */
@Setter
@Getter
public class BaseEntity<ID> {

    /** 聚合根唯一标识 */
    protected ID id;

    /** 创建时间 */
    protected LocalDateTime createTime;

    /** 创建人ID */
    protected ID createBy;

    /** 更新时间 */
    protected LocalDateTime updateTime;

    /** 更新人ID */
    protected ID updateBy;

    /**
     * 租户ID
     */
    protected String tenantId;

    /**
     * 数据部门ID
     */
    protected Set<ID> deptIds;

}
