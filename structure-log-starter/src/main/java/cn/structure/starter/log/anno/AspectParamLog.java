package cn.structure.starter.log.anno;

import java.lang.annotation.*;

/**
 * <p>
 * 参数记录切面注解
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021-01-01
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AspectParamLog {

}
