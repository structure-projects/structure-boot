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
package cn.structured.mybatisplus.generate.example.service.impl;

import cn.structured.mybatis.plus.starter.base.BaseServiceImpl;
import cn.structured.mybatis.plus.starter.core.JoinHelper;
import cn.structured.mybatis.plus.starter.core.QueryJoinPageListWrapper;
import cn.structured.mybatis.plus.starter.vo.ReqPage;
import cn.structured.mybatisplus.generate.example.dao.StaffMapper;
import cn.structured.mybatisplus.generate.example.entity.Staff;
import cn.structured.mybatisplus.generate.example.group.PostGroup;
import cn.structured.mybatisplus.generate.example.group.StaffPostGroup;
import cn.structured.mybatisplus.generate.example.service.IStaffService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 人员表 服务实现类
 * </p>
 *
 * @author chuck
 * @since 2021-07-30
 */
@Service
public class StaffServiceImpl extends BaseServiceImpl<StaffMapper, Staff> implements IStaffService {

    @Override
    public IPage page(ReqPage reqPage) {
        Staff staff = new Staff();
        QueryJoinPageListWrapper<Staff> queryJoinPageListWrapper = new QueryJoinPageListWrapper<>(staff);
        queryJoinPageListWrapper.setSearch(reqPage.getKeyword());
        queryJoinPageListWrapper.addSearch("name");
        queryJoinPageListWrapper.setJoinGroup(PostGroup.class);
        IPage<HashMap<String, Object>> hashMapIPage = baseMapper.selectJoinPageList(new Page(reqPage.getCurrentPage(), reqPage.getPageSize()), queryJoinPageListWrapper);
        List<Staff> list = JoinHelper.getList(hashMapIPage.getRecords(), Staff.class);
        IPage iPage = new Page(hashMapIPage.getCurrent(), hashMapIPage.getSize());
        iPage.setTotal(hashMapIPage.getTotal());
        iPage.setRecords(list);
        return iPage;
    }

    @Override
    public Staff getStaffById(Integer id) {
        Staff staff = new Staff();
        staff.setId(id);
        QueryJoinPageListWrapper<Staff> queryJoinPageListWrapper = new QueryJoinPageListWrapper<>(staff);
        queryJoinPageListWrapper.setJoinGroup(StaffPostGroup.class);
        queryJoinPageListWrapper.setIsJoin(true);
        List<HashMap<String, Object>> hashMapIPage = baseMapper.selectJoinList(queryJoinPageListWrapper);
        List<Staff> list = JoinHelper.getList(hashMapIPage, Staff.class);
        Optional<Staff> first = list.stream().findFirst();
        return first.isPresent() ? first.get() : new Staff() {{
            setOrgPost(new ArrayList<>());
        }};
    }
}
