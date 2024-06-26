package cn.structured.mybatis.plus.starter.core;

import lombok.Data;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * <p>
 * 联表字段属性
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/8/3 11:41
 */
@Data
public class JoinTableFieldInfo {

    /**
     * 属性
     *
     * @since 3.3.1
     */
    private Field field;
    /**
     * 字段名
     */
    private String column;
    /**
     * 属性名
     */
    private String property;
    /**
     * 属性类型
     */
    private Class<?> propertyType;

    /**
     * sql条件组
     */
    private Map<Class<?>,String> sqlConditionGroup;
}
