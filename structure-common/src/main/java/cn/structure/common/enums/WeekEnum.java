package cn.structure.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 *     星期枚举
 * </p>
 * @author CHUCK
 * @since 2020-12-26
 * @version 1.0.1
 */
@Getter
@AllArgsConstructor
public enum WeekEnum {

    /**
     * 周一，周二，周三，周四，周五，周六，周天
     */
    MONDAY("周一", 1, "Mon"),
    TUESDAY("周二", 2, "Tues"),
    WEDNESDAY("周三", 3, "Wed"),
    THURSDAY("周四", 4, "Thur"),
    FRIDAY("周五", 5, "Fri"),
    SATURDAY("周六", 6, "Sat"),
    SUNDAY("周日", 0, "Sun");
    private String week;
    private int num;
    private String ab;
}
