package cn.structure.common.annotations;

import cn.structure.common.validator.ConditionalRequiredValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * 条件必填验证注解
 * 当满足指定条件时，字段才必填
 *
 * @author chuck
 * @since 2024-01-01
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ConditionalRequiredValidator.class)
public @interface ConditionalRequired {

    /**
     * 条件字段名
     */
    String conditionField();

    /**
     * 条件字段的期望值
     */
    String[] conditionValues();

    /**
     * 错误消息
     */
    String message() default "该字段不能为空";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}