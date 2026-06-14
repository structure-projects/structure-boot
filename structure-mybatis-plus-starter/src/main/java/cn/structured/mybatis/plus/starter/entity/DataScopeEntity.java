package cn.structured.mybatis.plus.starter.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;


@Getter
@Setter
public class DataScopeEntity<ID> extends BaseEntity<ID> {

    /**
     * 部门ID
     */
    @TableField(value = "create_dept_id", fill = FieldFill.INSERT)
    protected ID createDeptId;

}
