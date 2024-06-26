package cn.structured.mybatis.plus.starter.annotations;

import cn.structured.mybatis.plus.starter.enums.JoinResultEnum;
import cn.structured.mybatis.plus.starter.enums.JoinTypeEnum;

import java.lang.annotation.*;

/**
 * <p>
 *
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/8/5 14:28
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface FieldJoin {

    /**
     * 联表类型
     *
     * @return {@link JoinTypeEnum}
     */
    JoinTypeEnum joinType() default JoinTypeEnum.JOIN;

    /**
     * 联表返回的类型 如果是集合则表示泛型的值
     *
     * @return {@link Class}
     */
    Class<?> result() default Object.class;


    /**
     * 联表结果类型
     *
     * @return {@link Class}
     */
    JoinResultEnum type() default JoinResultEnum.ONE;

    /**
     * 要链的表
     *
     * @return {@link Join}
     */
    Join[] value() default {};
}
