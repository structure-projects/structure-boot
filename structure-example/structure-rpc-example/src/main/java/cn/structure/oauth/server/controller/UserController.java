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
package cn.structure.oauth.server.controller;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * RPC 服务端控制器 - 提供用户服务
 * </p>
 *
 * @author chuck
 * @version 1.0.0
 * @since 2025-05-24
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    /**
     * 根据ID获取用户信息
     */
    @GetMapping("/getById/{id}")
    public Map<String, Object> getUserById(@PathVariable Long id) {
        Map<String, Object> user = new HashMap<>();
        user.put("id", id);
        user.put("username", "user_" + id);
        user.put("email", "user" + id + "@example.com");
        user.put("status", "active");
        user.put("message", "User found");
        return user;
    }

    /**
     * 根据用户名获取用户信息
     */
    @GetMapping("/getByUsername")
    public Map<String, Object> getUserByUsername(@RequestParam String username) {
        Map<String, Object> user = new HashMap<>();
        user.put("id", 1001L);
        user.put("username", username);
        user.put("email", username + "@example.com");
        user.put("status", "active");
        user.put("message", "User found by username");
        return user;
    }

    /**
     * 创建用户
     */
    @PostMapping("/create")
    public Map<String, Object> createUser(@RequestBody Map<String, Object> userData) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("id", 999L);
        result.put("username", userData.get("username"));
        result.put("message", "User created successfully");
        return result;
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/update/{id}")
    public Map<String, Object> updateUser(@PathVariable Long id, @RequestBody Map<String, Object> userData) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("id", id);
        result.put("message", "User updated successfully");
        return result;
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/delete/{id}")
    public Map<String, Object> deleteUser(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("id", id);
        result.put("message", "User deleted successfully");
        return result;
    }
}
