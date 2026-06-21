package cn.structure.common.validator;

import cn.structure.common.annotations.FieldCompare;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 字段比较验证器实现
 *
 * @author chuck
 * @since 2024-01-01
 */
public class FieldCompareValidator implements ConstraintValidator<FieldCompare, Object> {

    private String firstField;
    private String secondField;
    private FieldCompare.CompareType compareType;

    @Override
    public void initialize(FieldCompare constraintAnnotation) {
        this.firstField = constraintAnnotation.firstField();
        this.secondField = constraintAnnotation.secondField();
        this.compareType = constraintAnnotation.compareType();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Object firstValue = getFieldValue(value, firstField);
            Object secondValue = getFieldValue(value, secondField);

            // 如果两个字段都为空，则通过验证
            if (firstValue == null && secondValue == null) {
                return true;
            }

            // 如果其中一个为空，则使用默认比较逻辑
            if (firstValue == null || secondValue == null) {
                return compareType == FieldCompare.CompareType.NOT_EQUALS;
            }

            // 比较逻辑
            int comparison = compare(firstValue, secondValue);

            switch (compareType) {
                case LESS_THAN:
                    return comparison < 0;
                case LESS_THAN_OR_EQUALS:
                    return comparison <= 0;
                case GREATER_THAN:
                    return comparison > 0;
                case EQUALS:
                    return comparison == 0;
                case NOT_EQUALS:
                    return comparison != 0;
                default:
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    private Object getFieldValue(Object object, String fieldName) throws Exception {
        java.lang.reflect.Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(object);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private int compare(Object first, Object second) {
        if (first instanceof Comparable && second instanceof Comparable) {
            return ((Comparable) first).compareTo(second);
        }
        return first.toString().compareTo(second.toString());
    }
}