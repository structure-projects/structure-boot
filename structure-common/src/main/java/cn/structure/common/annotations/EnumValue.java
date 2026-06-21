package cn.structure.common.annotations;

import cn.structure.common.validator.EnumValueValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * 枚举值验证注解
 * 限制字段值必须在指定的枚举值范围内
 *
 * @author chuck
 * @since 2024-01-01
 */
@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumValueValidator.class)
public @interface EnumValue {

    /**
     * 错误消息
     */
    String message() default "值不在允许的范围内";

    /**
     * 分组
     */
    Class<?>[] groups() default {};

    /**
     * 负载
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * 枚举类
     */
    Class<? extends Enum<?>> value();

    /**
     * 是否忽略大小写（仅对String类型有效）
     */
    boolean ignoreCase() default false;
}