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
package cn.structure.oauth.client;

import cn.structure.oauth.dto.UserCreateRequest;
import cn.structure.oauth.dto.UserInfo;
import cn.structure.oauth.dto.UserUpdateRequest;
import cn.structured.rpc.annotation.RpcClient;
import org.springframework.web.bind.annotation.*;

/**
 * 用户服务客户端接口（Bearer认证-参数方式）
 * 使用@RpcClient注解标记，使用Bearer Token进行身份认证
 * 
 * Bearer认证方式：通过远程认证服务器获取token，在请求头中添加 Authorization: Bearer token
 * 此客户端使用参数方式获取token（client_id/client_secret放在请求体中）
 *
 * @author chuck
 */
@RpcClient(value = "user-center-bearer")
public interface UserClientBearer extends UserClient {

    /**
     * 根据用户ID获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    @GetMapping("/api/user/getById/{userId}")
    UserInfo getUserById(@PathVariable("userId") Long userId);

    /**
     * 根据用户名获取用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    @GetMapping("/api/user/getByUsername")
   UserInfo getUserByUsername(@RequestParam("username") String username);

    /**
     * 创建用户
     *
     * @param userCreateRequest 用户创建请求
     * @return 创建的用户信息
     */
    @PostMapping("/api/user/create")
    UserInfo createUser(@RequestBody UserCreateRequest userCreateRequest);

    /**
     * 更新用户信息
     *
     * @param userId 用户ID
     * @param userUpdateRequest 用户更新请求
     * @return 更新后的用户信息
     */
    @PutMapping("/api/user/update/{userId}")
    UserInfo updateUser(@PathVariable("userId") Long userId, @RequestBody UserUpdateRequest userUpdateRequest);

    /**
     * 删除用户
     *
     * @param userId 用户ID
     */
    @DeleteMapping("/api/user/delete/{userId}")
    void deleteUser(@PathVariable("userId") Long userId);


}
