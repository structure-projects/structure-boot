package cn.structured.mybatis.plus.starter.annotations;


import cn.structured.mybatis.plus.starter.enums.JoinTypeEnum;

import java.lang.annotation.*;

/**
 * <p>
 * 字段需要关联
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/8/2 14:39
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface Join {

    boolean result() default false;

    /**
     * 联表类型
     *
     * @return {@link JoinTypeEnum}
     */
    JoinTypeEnum joinType() default JoinTypeEnum.JOIN;

    /**
     * 联表的目标表
     *
     * @return {@link Class}
     */
    Class<?> joinTarget() default Object.class;

    /**
     * 联表条件
     *
     * @return {@link JoinCondition}
     */
    JoinCondition[] value() default {};

    /**
     * 别名
     *
     * @return {@link String}
     */
    String aliasName() default "";

    /**
     * 返回结果
     *
     * @return {@link String}
     */
    String[] columns() default {};

    /**
     * 组
     *
     * @return {@link Class}
     */
    Class<?>[] group() default {};

}
