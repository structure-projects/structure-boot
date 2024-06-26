package cn.structure.starter.web.restful.annotation;

import cn.structure.starter.web.restful.configuration.Swagger2;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * <p>
 *     开启 Swagger2
 * </p>
 * @author chuck
 * @since 2021-01-03
 * @version 1.0.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(Swagger2.class)
public @interface EnableSwagger {
}
