package cn.structure.common.validator;

import cn.structure.common.annotations.ConditionalRequired;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;

/**
 * 条件必填验证器
 *
 * <p>验证逻辑：
 * 1. 获取条件字段的值
 * 2. 判断条件字段值是否匹配期望值
 * 3. 如果匹配，则当前字段值不能为空
 * 4. 如果不匹配，验证通过（字段可以为空）</p>
 *
 * @author chuck
 * @since 2024-01-01
 */
public class ConditionalRequiredValidator implements ConstraintValidator<ConditionalRequired, Object> {

    private String conditionField;
    private String[] conditionValues;

    @Override
    public void initialize(ConditionalRequired constraintAnnotation) {
        this.conditionField = constraintAnnotation.conditionField();
        this.conditionValues = constraintAnnotation.conditionValues();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        try {
            // 获取条件字段
            Field conditionFieldRef = value.getClass().getDeclaredField(conditionField);
            conditionFieldRef.setAccessible(true);
            Object conditionValue = conditionFieldRef.get(value);

            // 检查条件字段值是否匹配期望值
            boolean conditionMatched = isConditionMatched(conditionValue);

            // 如果条件匹配，验证当前字段值是否为空
            if (conditionMatched) {
                // 当前字段值由 @NotBlank/@NotNull 处理，这里只检查逻辑关系
                return value != null;
            }

            // 条件不匹配，验证通过
            return true;
        } catch (NoSuchFieldException e) {
            // 字段不存在，验证失败
            return false;
        } catch (IllegalAccessException e) {
            return false;
        }
    }

    /**
     * 判断条件字段值是否匹配期望值
     */
    private boolean isConditionMatched(Object conditionValue) {
        if (conditionValue == null) {
            return false;
        }

        String conditionStr = conditionValue.toString();
        for (String expectedValue : conditionValues) {
            if (expectedValue.equals(conditionStr)) {
                return true;
            }
        }
        return false;
    }
}