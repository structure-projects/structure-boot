package cn.structure.common.validator;

import cn.structure.common.annotations.EnumValue;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.List;

/**
 * 枚举值验证器实现
 *
 * @author chuck
 * @since 2024-01-01
 */
public class EnumValueValidator implements ConstraintValidator<EnumValue, Object> {

    private List<Enum<?>> enumConstants;
    private boolean ignoreCase;

    @Override
    public void initialize(EnumValue annotation) {
        this.ignoreCase = annotation.ignoreCase();
        this.enumConstants = new ArrayList<>();
        int length = java.lang.reflect.Array.getLength(annotation.value());
        for (int i = 0; i < length; i++) {
            Object item = java.lang.reflect.Array.get(annotation.value(), i);
            if (item != null) {
                @SuppressWarnings("unchecked")
                Class<? extends Enum<?>> enumClass = (Class<? extends Enum<?>>) item;
                Enum<?>[] constants = enumClass.getEnumConstants();
                if (constants != null) {
                    this.enumConstants.addAll(java.util.Arrays.asList(constants));
                }
            }
        }
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // 使用 @NotNull 来验证非空
        }

        if (value instanceof String) {
            String strValue = (String) value;
            return enumConstants.stream()
                    .anyMatch(e -> {
                        String name = e.name();
                        return ignoreCase ? name.equalsIgnoreCase(strValue) : name.equals(strValue);
                    });
        }

        return enumConstants.contains(value);
    }
}