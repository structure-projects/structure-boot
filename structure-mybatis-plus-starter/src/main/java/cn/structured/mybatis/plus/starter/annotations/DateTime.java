package cn.structured.mybatis.plus.starter.annotations;

import java.lang.annotation.*;

/**
 * <p>
 * 时间关键字
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/8/5 11:53
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface DateTime {
    String value() default "";
}
