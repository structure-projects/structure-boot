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
package cn.structure.example.mybatisplus.mapper;

import cn.structure.example.mybatisplus.pojo.po.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author chuck
 * @since 2020-12-27
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 插入用户 XML 仅仅展示用
     *
     * @param user
     * @return
     */
    int insertUser(User user);

    /**
     * 修改用户 XML 仅仅展示用
     *
     * @param user
     * @return
     */
    int updateUser(User user);

    /**
     * 通过用户名获取用户
     *
     * @param username
     * @return
     */
    User getUserByUsername(@Param("username") String username);

    /**
     * 通过用户实体获取用户分页列表
     *
     * @param user
     * @param pageSize
     * @param offset
     * @return
     */
    List<User> listUserPage(@Param("user") User user, @Param("pageSize") int pageSize, @Param("offset") int offset);

}
