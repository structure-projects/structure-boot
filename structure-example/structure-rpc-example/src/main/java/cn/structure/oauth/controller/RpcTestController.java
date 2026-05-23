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
package cn.structure.oauth.controller;

import cn.structure.oauth.client.UserClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * RPC 测试控制器 - 提供自我调用测试功能
 * </p>
 *
 * @author chuck
 * @version 1.0.0
 * @since 2025-05-24
 */
@RestController
@RequestMapping("/rpc")
public class RpcTestController {

    private final UserClient userClient;
    private final RestTemplate restTemplate;

    @Value("${server.port:16010}")
    private String serverPort;

    public RpcTestController(UserClient userClient) {
        this.userClient = userClient;
        this.restTemplate = new RestTemplate();
    }

    /**
     * 健康检查
     */
    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "UP");
        result.put("message", "RPC Example is working");
        result.put("serverPort", serverPort);
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }

    /**
     * 测试RPC调用 - 通过RestTemplate进行自我调用
     */
    @GetMapping("/test")
    public Map<String, Object> test() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 调用本地的用户服务接口（自我调用）
            String url = "http://localhost:" + serverPort + "/api/user/getById/1";
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            
            result.put("success", true);
            result.put("message", "RPC self-call test passed");
            result.put("response", response);
            result.put("endpoint", url);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "RPC self-call test failed: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 测试获取用户信息 - 通过RestTemplate调用
     */
    @GetMapping("/user/getById/{id}")
    public Map<String, Object> getUserById(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            String url = "http://localhost:" + serverPort + "/api/user/getById/" + id;
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            
            result.put("success", true);
            result.put("method", "GET");
            result.put("endpoint", url);
            result.put("data", response);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Failed to get user: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 测试根据用户名获取用户信息
     */
    @GetMapping("/user/getByUsername")
    public Map<String, Object> getUserByUsername(@RequestParam String username) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            String url = "http://localhost:" + serverPort + "/api/user/getByUsername?username=" + username;
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            
            result.put("success", true);
            result.put("method", "GET");
            result.put("endpoint", url);
            result.put("data", response);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Failed to get user by username: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 测试创建用户 - POST请求
     */
    @PostMapping("/user/create")
    public Map<String, Object> createUser(@RequestBody Map<String, Object> userData) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            String url = "http://localhost:" + serverPort + "/api/user/create";
            Map<String, Object> response = restTemplate.postForObject(url, userData, Map.class);
            
            result.put("success", true);
            result.put("method", "POST");
            result.put("endpoint", url);
            result.put("data", response);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Failed to create user: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 测试更新用户 - PUT请求
     */
    @PutMapping("/user/update/{id}")
    public Map<String, Object> updateUser(@PathVariable Long id, @RequestBody Map<String, Object> userData) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            String url = "http://localhost:" + serverPort + "/api/user/update/" + id;
            restTemplate.put(url, userData);
            
            result.put("success", true);
            result.put("method", "PUT");
            result.put("endpoint", url);
            result.put("message", "User updated successfully");
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Failed to update user: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 测试删除用户 - DELETE请求
     */
    @DeleteMapping("/user/delete/{id}")
    public Map<String, Object> deleteUser(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            String url = "http://localhost:" + serverPort + "/api/user/delete/" + id;
            restTemplate.delete(url);
            
            result.put("success", true);
            result.put("method", "DELETE");
            result.put("endpoint", url);
            result.put("message", "User deleted successfully");
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Failed to delete user: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * 测试所有RPC功能
     */
    @GetMapping("/test/all")
    public Map<String, Object> testAll() {
        Map<String, Object> result = new HashMap<>();
        int passed = 0;
        int failed = 0;
        
        // Test 1: Get user by id
        try {
            Map<String, Object> user = restTemplate.getForObject(
                "http://localhost:" + serverPort + "/api/user/getById/1", Map.class);
            result.put("test1_getUserById", user);
            passed++;
        } catch (Exception e) {
            result.put("test1_getUserById", "FAILED: " + e.getMessage());
            failed++;
        }
        
        // Test 2: Get user by username
        try {
            Map<String, Object> user = restTemplate.getForObject(
                "http://localhost:" + serverPort + "/api/user/getByUsername?username=testuser", Map.class);
            result.put("test2_getUserByUsername", user);
            passed++;
        } catch (Exception e) {
            result.put("test2_getUserByUsername", "FAILED: " + e.getMessage());
            failed++;
        }
        
        // Test 3: Create user
        try {
            Map<String, Object> userData = new HashMap<>();
            userData.put("username", "newuser");
            Map<String, Object> createResult = restTemplate.postForObject(
                "http://localhost:" + serverPort + "/api/user/create", userData, Map.class);
            result.put("test3_createUser", createResult);
            passed++;
        } catch (Exception e) {
            result.put("test3_createUser", "FAILED: " + e.getMessage());
            failed++;
        }
        
        result.put("totalTests", passed + failed);
        result.put("passed", passed);
        result.put("failed", failed);
        result.put("success", failed == 0);
        
        return result;
    }
}
