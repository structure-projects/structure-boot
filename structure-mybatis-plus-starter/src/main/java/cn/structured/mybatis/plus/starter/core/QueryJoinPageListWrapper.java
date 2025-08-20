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

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 查询联表分页列表条件
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/8/3 14:58
 */

@Data
@AllArgsConstructor
public class QueryJoinPageListWrapper<T> {

    public QueryJoinPageListWrapper(T entity) {
        this.isJoin = false;
        this.clazz = entity.getClass();
        this.entity = entity;
    }

    /**
     * 实体中默认为 = 条件 排除搜索和时间
     */
    private T entity;

    /**
     * 实体类型
     */
    private Class<?> clazz;

    /**
     * 是否链表
     */
    private Boolean isJoin;

    /**
     * 搜索参数
     */
    private String search;

    /**
     * 开始时间
     */
    private LocalDateTime beginTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 链表用的组
     */
    private Class<?> joinGroup;

    /**
     * 搜索字段
     */
    private List<String> searchList = new ArrayList<>();

    /**
     * 时间区间
     */
    private List<String> timeList = new ArrayList<>();

    /**
     * 要查询的列
     */
    private List<String> selectColumn = new ArrayList<>();

    /**
     * 自定义条件
     */
    private String condition;

    /**
     * 添加搜索列表
     *
     * @param searchList
     */
    public void addSearch(String... searchList) {
        for (int i = 0; i < searchList.length; i++) {
            this.searchList.add(searchList[i]);
        }
    }

    /**
     * 添加字段列
     *
     * @param selectColumn
     */
    public void addColumn(String... selectColumn) {
        for (int i = 0; i < selectColumn.length; i++) {
            this.selectColumn.add(selectColumn[i]);
        }

    }

    /**
     * 添加时间
     *
     * @param timeList
     */
    public void addTime(String... timeList) {
        for (int i = 0; i < timeList.length; i++) {
            this.timeList.add(timeList[i]);
        }
    }

}
