package cn.structured.mybatis.plus.starter.annotations;

import cn.structured.mybatis.plus.starter.enums.ConditionEnum;
import com.baomidou.mybatisplus.annotation.SqlCondition;

import java.lang.annotation.*;

/**
 * where匹配条件定义
 *
 * @author cqliut
 * @version 2023.0713
 * @since 1.0.1
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface Condition {

    /**
     * 组 在某个组的条件下才生效 默认不配置则全局生效
     *
     * @return {@link Class}
     */
    Class<?>[] group() default {};

    /**
     * 比较条件
     *
     * @return {@link ConditionEnum}
     */
    String sqlCondition() default SqlCondition.EQUAL;

    /**
     * 自定义条件类型 condition
     *
     * @return {@link String}
     */
    String condition() default "";

}
