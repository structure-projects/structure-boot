package cn.structured.mybatis.plus.starter.annotations;

import java.lang.annotation.*;

/**
 * where过滤条件对比
 *
 * @author cqliut
 * @version 2023.0713
 * @since 1.0.1
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface Where {

    /**
     * 条件
     *
     * @return {@link Condition}
     */
    Condition[] value() default {};
}
