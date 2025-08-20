/*
 * Copyright (c) 2025 Structure Boot
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.structure.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 星期枚举
 * </p>
 *
 * @author CHUCK
 * @version 1.0.1
 * @since 2020-12-26
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
