package cn.structure.common.group;

/**
 * 验证组定义
 * 用于针对不同业务场景应用不同的验证规则
 *
 * @author chuck
 * @since 2024-01-01
 */
public class ValidationGroups {

    /**
     * 添加分组
     */
    public interface Add {
    }

    /**
     * 更新分组
     */
    public interface Update {
    }

    /**
     * 查询分组
     */
    public interface Query {
    }

    /**
     * 删除分组
     */
    public interface Delete {
    }
}