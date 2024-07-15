package cn.structured.mybatis.plus.starter.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 逻辑关键字枚举类
 *
 * @author cqliut
 * @version 2023.0713
 * @since 1.0.1
 */
@Getter
@AllArgsConstructor
public enum LogicKeywordEnum {
    //逻辑关键字
    AND("AND"),
    OR("OR");
    private String keyword;
}
