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
package cn.structure.example.mybatis.service;

import cn.structure.example.mybatis.entity.User;
import cn.structure.example.mybatis.mapper.IUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 用户service实现
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2020/12/27 16:53
 */
@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private IUserMapper iUserMapper;

    @Override
    public User getUserById(Long id) {
        log.info("UserServiceImpl------> getUserById -------> id ={}", id);
        User user = iUserMapper.getUserById(id);
        log.info("UserServiceImpl------> getUserById ------->{}", ((user == null) ? "this user is null" : user.getUsername()));
        return user;
    }

    @Override
    public User getUserByUsername(String username) {
        log.info("UserServiceImpl------> getUserByUsername -------> username ={}", username);
        User user = iUserMapper.listUser(username);
        log.info("UserServiceImpl------> getUserById ------->{}", ((user == null) ? "this user is null" : user.getUsername()));
        return user;
    }

    @Override
    public List<User> listUserPage(String username, int pageSize, int offset) {
        log.info("UserServiceImpl------> listUserPage -------> username ={},pageSize ={},offset = {}", username, pageSize, offset);
        List<User> userList = iUserMapper.listUserPage(new User() {{
            setUsername(username);
        }}, pageSize, offset);
        log.info("UserServiceImpl------> listUserPage -------> userListSize = {}", userList.size());
        return userList;
    }

    @Override
    public int insertUser(User user) {
        log.info("UserServiceImpl----->insertUser-----> username ={}", user.getUsername());
        int row = iUserMapper.insertUser(user);
        log.info("UserServiceImpl----->insertUser-----> id ={}", user.getId());
        return row;
    }

    @Override
    public int updateUserById(User user) {
        log.info("UserServiceImpl----->updateUserById-----> id ={}", user.getId());
        int i = iUserMapper.updateUserById(user);
        log.info("UserServiceImpl----->updateUserById-----> rows = {}", i);
        return i;
    }

    @Override
    public int deleteById(Long id) {
        log.info("UserServiceImpl----->deleteById-----> id = {}", id);
        int delete = iUserMapper.delete(id);
        log.info("UserServiceImpl----->deleteById-----> rows = {}", delete);
        return delete;
    }
}
