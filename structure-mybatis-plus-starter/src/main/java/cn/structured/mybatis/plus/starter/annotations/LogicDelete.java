package cn.structured.mybatis.plus.starter.annotations;

import java.lang.annotation.*;

/**
 * <p>
 * 逻辑删除注解
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/8/10 17:15
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface LogicDelete {
    /**
     * 字段名
     *
     * @return {@link String}
     */
    String value() default "";

    /**
     * 删除的值
     *
     * @return {@link Integer}
     */
    int deleteValue() default 1;


}
