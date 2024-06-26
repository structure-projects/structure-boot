package cn.structure.starter.log.anno;

import java.lang.annotation.*;

/**
 * <p>
 *     参数记录切面注解
 * </p>
 * @author chuck
 * @since 2021-01-01
 * @version 1.0.1
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AspectParamLog  {

}
