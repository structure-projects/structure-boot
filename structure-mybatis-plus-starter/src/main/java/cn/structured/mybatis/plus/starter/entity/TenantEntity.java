package cn.structured.mybatis.plus.starter.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

/**
 *
 */
@Setter
@Getter
public class TenantEntity<ID> extends DataScopeEntity<ID> {

    /**
     * 租户ID
     */
    @TableField(value = "tenant_id")
    protected String tenantId;

}
