package cn.structure.starter.web.restful.annotation;

import java.lang.annotation.*;

/**
 * <p>
 * 自动装配父子关系的异常处理 1.2.1 版本后弃用该方式
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021-01-03
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Deprecated
public @interface EnableFatherGlobalException {
}
