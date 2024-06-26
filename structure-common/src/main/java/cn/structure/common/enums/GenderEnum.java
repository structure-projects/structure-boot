package cn.structure.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/***
 * <p>
 *     性别枚举
 * </p>
 * @author chuck
 * @since 2020-12-26
 * @version 1.0.1
 */
@AllArgsConstructor
@Getter
public enum GenderEnum {
    /**
     * N 未知0  M 男1 F 女2
     */
    N(0),
    M(1),
    F(2);
    private int num;


}
