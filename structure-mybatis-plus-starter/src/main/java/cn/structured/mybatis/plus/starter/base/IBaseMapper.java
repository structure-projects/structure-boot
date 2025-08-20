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
package cn.structured.mybatis.plus.starter.base;

import cn.structured.mybatis.plus.starter.core.QueryJoinPageListWrapper;
import cn.structured.mybatis.plus.starter.provider.InsertList;
import cn.structured.mybatis.plus.starter.provider.SelectJoinList;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 基础mapper
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/8/2 14:30
 */
public interface IBaseMapper<T> extends BaseMapper<T> {

    /**
     * 批量插入
     *
     * @param entityList 批量插入的数据实体
     * @return 返回执行条数
     */
    @InsertProvider(type = InsertList.class, method = "insertList")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertList(@Param("list") List<T> entityList);

    /**
     * 列表的分页查询
     *
     * @param page    分页参数
     * @param wrapper 查询条件
     * @return 带分页的结果
     */
    @SelectProvider(type = SelectJoinList.class, method = "selectJoinList")
    IPage<HashMap<String, Object>> selectJoinPageList(@Param("page") IPage<T> page, @Param("wrapper") QueryJoinPageListWrapper<T> wrapper);

    /**
     * 自定义查询 使用hashMap接收数据可以在上层组装数据
     *
     * @param wrapper 查询条件
     * @return 查询结果
     */
    @SelectProvider(type = SelectJoinList.class, method = "selectJoinList")
    List<HashMap<String, Object>> selectJoinList(@Param("wrapper") QueryJoinPageListWrapper<T> wrapper);


    /**
     * 通过条件查询结果该方法不支持链表查询（链表的结果不会被组装）
     *
     * @param wrapper 查询条件
     * @return 返回单表的数据
     */
    @SelectProvider(type = SelectJoinList.class, method = "selectJoinList")
    List<T> selectJoin(@Param("wrapper") QueryJoinPageListWrapper<T> wrapper);

    /**
     * 启用
     *
     * @param id 主键ID
     * @return 返回执行条数
     */
    int enable(Serializable id);

    /**
     * 停用
     *
     * @param id 主键ID
     * @return 返回执行条数
     */
    int disable(Serializable id);

}
