package cn.structure.starter.web.restful.annotation;

import cn.structure.starter.web.restful.filter.FastJsonHttpMessageConverters;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * <p>
 * 开启fastJson出参转换
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021-01-03
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(FastJsonHttpMessageConverters.class)
public @interface EnableFastJsonHttpConverters {

    /**
     * 将long转换为String字符串 js没有long类型会有经度丢失
     */
    boolean longToString() default false;

    /**
     * 是否展示值为null的key
     */
    boolean nullShowValue() default false;


}
