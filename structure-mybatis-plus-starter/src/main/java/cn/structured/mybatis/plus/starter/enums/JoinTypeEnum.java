package cn.structured.mybatis.plus.starter.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 联表类型
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/8/3 10:59
 */
@Getter
@AllArgsConstructor
public enum JoinTypeEnum {

    JOIN("JOIN"),
    LEFT_JOIN("LEFT JOIN"),
    RIGHT_JOIN("RIGHT JOIN");
    private String keyword;

}
