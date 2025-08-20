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
package cn.structure.example.mybatisplus.service.impl;

import cn.structure.example.mybatisplus.mapper.UserMapper;
import cn.structure.example.mybatisplus.pojo.po.User;
import cn.structure.example.mybatisplus.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author chuck
 * @since 2020-12-27
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public int update(User user) {
        return this.baseMapper.updateUser(user);
    }

    @Override
    public int insert(User user) {
        return this.baseMapper.insertUser(user);
    }

    @Override
    public User getUserByUsername(String username) {
        return this.baseMapper.getUserByUsername(username);
    }

    @Override
    public List<User> listUserPage(String username, int pageSize, int offset) {
        return this.baseMapper.listUserPage(new User() {{
            setUsername(username);
        }}, pageSize, offset);
    }

    @Override
    public List<User> listUserPage(User user, int pageSize, int offset) {
        return this.baseMapper.listUserPage(user, pageSize, offset);
    }
}
