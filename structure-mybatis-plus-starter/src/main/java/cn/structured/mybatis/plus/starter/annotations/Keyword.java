package cn.structured.mybatis.plus.starter.annotations;

import java.lang.annotation.*;

/**
 * <p>
 * 搜索关键字
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/8/5 11:46
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface Keyword {
    /**
     * 关键字段列名
     *
     * @return {@link String}
     */
    String value() default "";
}
