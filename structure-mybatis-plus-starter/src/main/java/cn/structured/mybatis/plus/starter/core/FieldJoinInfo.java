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
package cn.structured.mybatis.plus.starter.core;

import cn.structured.mybatis.plus.starter.enums.JoinResultEnum;
import cn.structured.mybatis.plus.starter.enums.JoinTypeEnum;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 字段联表信息
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/8/5 14:33
 */
@Data
public class FieldJoinInfo {

    /**
     * 联表类型
     *
     * @return
     */
    private JoinTypeEnum joinType;

    /**
     * 联表返回的类型 如果是集合则表示泛型的值
     *
     * @return
     */
    private Class<?> result;

    /**
     * 联表结果类型
     *
     * @return
     */
    private JoinResultEnum type;

    /**
     * 要链的表
     *
     * @return
     */
    private List<JoinInfo> joinInfoList;

    /**
     * 实体属性名
     */
    private String resultName;

}
