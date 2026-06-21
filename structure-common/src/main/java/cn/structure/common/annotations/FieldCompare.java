package cn.structure.common.annotations;

import cn.structure.common.validator.FieldCompareValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * 字段比较验证注解
 * 验证两个字段的关系，如开始日期必须早于结束日期
 *
 * <p><b>设计原则：</b>此注解不支持单独使用，必须配合其他字段级验证注解（如 @NotNull、@NotBlank）一起使用。
 * 因为交叉字段验证仅校验字段间的逻辑关系，不检查字段本身是否为空。</p>
 *
 * @author chuck
 * @since 2024-01-01
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FieldCompareValidator.class)
public @interface FieldCompare {

    /**
     * 配置组，支持多组字段比较验证
     * 当需要验证多个字段对时，使用此属性
     */
    CompareConfig[] value();

    /**
     * 单个字段比较配置（当只需验证一对字段时使用）
     */
    String firstField() default "";

    /**
     * 单个字段比较配置的第二个字段（当只需验证一对字段时使用）
     */
    String secondField() default "";

    /**
     * 比较类型：LESS_THAN（第一个小于第二个）、EQUALS（相等）、NOT_EQUALS（不相等）
     */
    CompareType compareType() default CompareType.LESS_THAN;

    /**
     * 错误消息
     */
    String message() default "字段比较验证失败";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 字段比较配置（支持多组配置）
     */
    @Documented
    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface CompareConfig {
        /**
         * 第一个字段名
         */
        String firstField();

        /**
         * 第二个字段名
         */
        String secondField();

        /**
         * 比较类型
         */
        CompareType compareType();

        /**
         * 错误消息
         */
        String message() default "字段比较验证失败";
    }

    enum CompareType {
        LESS_THAN,      // 第一个字段值 < 第二个字段值
        LESS_THAN_OR_EQUALS,  // 第一个字段值 <= 第二个字段值
        GREATER_THAN,   // 第一个字段值 > 第二个字段值
        EQUALS,         // 相等
        NOT_EQUALS      // 不相等
    }
}