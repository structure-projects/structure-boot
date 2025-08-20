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

import cn.structured.mybatis.plus.starter.core.JoinHelper;
import cn.structured.mybatis.plus.starter.core.JoinTableInfo;
import cn.structured.mybatis.plus.starter.core.QueryJoinPageListWrapper;
import cn.structured.mybatis.plus.starter.vo.ReqPage;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * service基类实现
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/8/2 14:29
 */
public class BaseServiceImpl<M extends IBaseMapper<T>, T> extends ServiceImpl<M, T> implements IBaseService<T> {

    @Override
    public IPage<T> page(IPage<T> page, QueryJoinPageListWrapper<T> queryWrapper) {
        //查询数据
        IPage<HashMap<String, Object>> hashMapPage = baseMapper.selectJoinPageList(page, queryWrapper);
        IPage<T> iPage = new Page<>();
        BeanUtils.copyProperties(hashMapPage, iPage);
        List<HashMap<String, Object>> records = hashMapPage.getRecords();
        //设置新的结果集合
        List list = JoinHelper.getList(records, entityClass);
        iPage.setRecords(list);
        return iPage;
    }

    @Override
    public List<T> list(QueryJoinPageListWrapper<T> queryWrapper) {
        Boolean isJoin = queryWrapper.getIsJoin();
        if (Boolean.TRUE.equals(isJoin)) {
            List<HashMap<String, Object>> hashMapList = baseMapper.selectJoinList(queryWrapper);
            return (List<T>) JoinHelper.getList(hashMapList, entityClass);
        } else {
            return baseMapper.selectJoin(queryWrapper);
        }

    }

    @Override
    public IPage<T> page(ReqPage reqPage, boolean isJoin) {

        //设置实体属性
        T entity = null;
        try {
            entity = (T) entityClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        //拷贝属性
        BeanUtils.copyProperties(reqPage, entity);
        //构建查询参数
        QueryJoinPageListWrapper<T> queryJoinPageListWrapper = new QueryJoinPageListWrapper<>(entity);
        queryJoinPageListWrapper.setSearch(reqPage.getKeyword());
        queryJoinPageListWrapper.setBeginTime(reqPage.getBeginTime());
        queryJoinPageListWrapper.setEndTime(reqPage.getEndTime());
        queryJoinPageListWrapper.setIsJoin(isJoin);
        JoinTableInfo tableInfo = JoinHelper.getTableInfo(entityClass);
        List<String> keyword = tableInfo.getKeyword();
        queryJoinPageListWrapper.setSearchList(keyword);
        queryJoinPageListWrapper.addTime("create_time");
        IPage iPage = new Page();
        IPage<HashMap<String, Object>> hashMapPage = baseMapper.selectJoinPageList(new Page(reqPage.getCurrentPage(), reqPage.getPageSize()), queryJoinPageListWrapper);
        BeanUtils.copyProperties(hashMapPage, iPage);
        List<HashMap<String, Object>> records = hashMapPage.getRecords();
        //设置新的结果集合
        List list = JoinHelper.getList(records, this.entityClass);
        iPage.setRecords(list);
        return iPage;
    }

}
