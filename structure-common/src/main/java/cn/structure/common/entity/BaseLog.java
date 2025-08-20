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
package cn.structure.common.entity;

import cn.structure.common.enums.LogEnums;
import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 日志实体基类
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2020-12-26
 */
@Getter
@Setter
@ToString
public class BaseLog {

    /**
     * 日志类型
     */
    private LogEnums type;

    /**
     * 目标方法
     */
    private String targetMethod;

    /**
     * 目标参数
     */
    private Object args;

    /**
     * 开始时间
     */
    private String beginTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 耗时
     */
    private Long timeDiff;

    /**
     * 将日志转换为JSON的字符串
     *
     * @return java.lang.String
     */
    public String toJSONString() {
        return JSON.toJSONString(this);
    }
}
