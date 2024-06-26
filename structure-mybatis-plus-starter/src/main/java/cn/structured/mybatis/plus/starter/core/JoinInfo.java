package cn.structured.mybatis.plus.starter.core;

import cn.structured.mybatis.plus.starter.annotations.JoinCondition;
import cn.structured.mybatis.plus.starter.enums.JoinTypeEnum;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 联表信息
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/8/3 11:45
 */
@Data
public class JoinInfo {

    /**
     * 是否需要返回
     */
    private Boolean isResult;

    /**
     * 联表类型
     *
     * @return
     */
    private JoinTypeEnum joinType;

    /**
     * 联表的目标表
     *
     * @return
     */
    private Class<?> joinTarget;

    /**
     * 联表用的组
     */
    private List<Class<?>> groups;

    /**
     * 联表条件
     *
     * @return
     */
    private List<JoinCondition> joinConditionInfo;

    /**
     * 别名
     */
    private String aliasName;

    /**
     * 返回结果
     *
     * @return
     */
    private List<String> columns;

}
