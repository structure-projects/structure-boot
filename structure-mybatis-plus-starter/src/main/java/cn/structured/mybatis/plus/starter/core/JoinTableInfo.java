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

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 表缓存
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/8/3 11:36
 */
@Data
public class JoinTableInfo {

    /**
     * 表class
     */
    private Class<?> tableClass;

    /**
     * 表名字
     */
    private String tableName;

    /**
     * 表主键
     */
    private String primaryKey;

    /**
     * 搜索关键字
     */
    private List<String> keyword;


    /**
     * 时间字段
     */
    private List<String> timeList;

    /**
     * 表字段信息列表
     */
    private List<JoinTableFieldInfo> fieldList;

    /**
     * 联表信息 k 属性名，v 属性的联表信息
     */
    private Map<String, FieldJoinInfo> joinInfo;

    /**
     * 是否拥有逻辑删除
     */
    private Boolean isDelete = false;

    /**
     * 逻辑删除
     */
    private String logicDelete;

    /**
     * 删除的值
     */
    private String deleteValue;

}
