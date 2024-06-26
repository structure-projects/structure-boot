package cn.structure.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 *     数字枚举类
 * </p>
 * @author CHUCK
 * @since 2020-12-26
 * @version 1.0.1
 */
@Getter
@AllArgsConstructor
public enum NumberEnum {

    /**
     *  数字枚举
     */
    ZERO(0),
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9);

    private int value;

}
