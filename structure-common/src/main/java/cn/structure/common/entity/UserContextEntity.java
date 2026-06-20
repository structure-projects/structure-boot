package cn.structure.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserContextEntity {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 部门ID
     */
    private String deptId;

    /**
     * 租户ID
     */
    protected String tenantId;

    /**
     * 数据部门ID
     */
    protected Set<String> deptIds;

    /**
     * 角色ID
     */
    private Set<String> roles;


    /**
     * 权限ID
     */
    private Set<String> permissions;

    /**
     * 登录时间
     */
    private LocalDateTime loginTime;
}
