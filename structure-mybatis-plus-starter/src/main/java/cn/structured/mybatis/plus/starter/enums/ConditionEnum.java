package cn.structured.mybatis.plus.starter.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 联表条件枚举
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/8/3 10:48
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ConditionEnum {

    /**
     * 条件关键字
     */
    AND("AND"),
    OR("OR"),
    NOT("NOT"),
    IN("IN"),
    NOT_IN("NOT IN"),
    LIKE("LIKE"),
    NOT_LIKE("NOT LIKE"),
    EQ("="),
    NE("<>"),
    GT(">"),
    GE(">="),
    LT("<"),
    LE("<="),
    IS_NULL("IS NULL"),
    IS_NOT_NULL("IS NOT NULL"),
    ;

    private String keyword;

}
