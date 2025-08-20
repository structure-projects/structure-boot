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
package cn.structured.mybatisplus.generate.example.service;

import cn.structured.mybatis.plus.starter.base.IBaseService;
import cn.structured.mybatis.plus.starter.vo.ReqPage;
import cn.structured.mybatisplus.generate.example.entity.Staff;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 * 人员表 服务类
 * </p>
 *
 * @author chuck
 * @since 2021-07-30
 */
public interface IStaffService extends IBaseService<Staff> {

    /**
     * 分页查询职工列表
     *
     * @param reqPageStaffVO
     * @return
     */
    @Override
    IPage<Staff> page(ReqPage reqPageStaffVO);

    /**
     * 通过用户ID查询详情
     *
     * @param id
     * @return
     */
    Staff getStaffById(Integer id);

}
