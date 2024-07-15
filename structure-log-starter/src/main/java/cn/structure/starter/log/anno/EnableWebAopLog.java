package cn.structure.starter.log.anno;

import cn.structure.starter.log.aop.ParamLogsAspect;
import cn.structure.starter.log.config.EnableWebAopConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * <p>
 * 开启webAop的log需要配置路径
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2020/6/3 12:05
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({EnableWebAopConfig.class, ParamLogsAspect.class})
public @interface EnableWebAopLog {

}
