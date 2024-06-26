package cn.structured.mybatis.plus.starter.annotations;

import cn.structured.mybatis.plus.starter.enums.ConditionEnum;

import java.lang.annotation.*;

/**
 * <p>
 * 联表条件
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/8/3 10:44
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface JoinCondition {

    /**
     * 联表的目标表列名
     *
     * @return {@link String}
     */
    String targetColumn() default "";

    /**
     * 当前表列名
     *
     * @return {@link String}
     */
    String currentColumn() default "";

    /**
     * 联表关键字
     *
     * @return {@link ConditionEnum}
     */
    ConditionEnum joinKeyword() default ConditionEnum.EQ;

    /**
     * 联表参数 value = value + targetProperty + joinKeyword + currentProperty
     *
     * @return {@link String}
     */
    String value() default "";

    /**
     * 联表条件 value = condition
     *
     * @return {@link String}
     */
    String condition() default "";

}
