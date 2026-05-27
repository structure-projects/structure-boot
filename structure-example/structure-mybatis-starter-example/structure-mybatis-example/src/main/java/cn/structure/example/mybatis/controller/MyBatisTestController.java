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
package cn.structure.example.mybatis.controller;

import cn.structure.example.mybatis.entity.User;
import cn.structure.example.mybatis.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * <p>
 * MyBatis 测试控制器 - 完整功能测试
 * </p>
 *
 * @author chuck
 * @version 1.0.0
 * @since 2025-05-24
 */
@RestController
@RequestMapping("/mybatis")
public class MyBatisTestController {

    @Autowired
    private IUserService iUserService;

    /**
     * 健康检查
     */
    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "UP");
        result.put("framework", "MyBatis");
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }

    /**
     * 测试1: 查询用户ById
     */
    @GetMapping("/test/getUserById")
    public Map<String, Object> testGetUserById(@RequestParam(value = "id", defaultValue = "1") Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            User user = iUserService.getUserById(id);
            result.put("success", true);
            result.put("operation", "getUserById");
            result.put("data", user);
            result.put("message", user != null ? "User found" : "User not found");
        } catch (Exception e) {
            result.put("success", false);
            result.put("operation", "getUserById");
            result.put("message", "Error: " + e.getMessage());
        }
        return result;
    }

    /**
     * 测试2: 查询用户ByUsername
     */
    @GetMapping("/test/getUserByUsername")
    public Map<String, Object> testGetUserByUsername(@RequestParam(value = "username", defaultValue = "admin") String username) {
        Map<String, Object> result = new HashMap<>();
        try {
            User user = iUserService.getUserByUsername(username);
            result.put("success", true);
            result.put("operation", "getUserByUsername");
            result.put("data", user);
            result.put("message", user != null ? "User found" : "User not found");
        } catch (Exception e) {
            result.put("success", false);
            result.put("operation", "getUserByUsername");
            result.put("message", "Error: " + e.getMessage());
        }
        return result;
    }

    /**
     * 测试3: 插入用户
     */
    @PostMapping("/test/insertUser")
    public Map<String, Object> testInsertUser(@RequestBody(required = false) Map<String, Object> userData) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (userData == null) {
                userData = new HashMap<>();
            }
            User user = new User();
            user.setUsername(userData.getOrDefault("username", "mybatis_user_" + System.currentTimeMillis()).toString());
            user.setPassword(userData.getOrDefault("password", "password123").toString());
            user.setUnexpired(true);
            user.setEnabled(true);
            user.setUnlocked(true);
            user.setDeleted(false);
            user.setCreateTime(new Date());
            user.setUpdateTime(new Date());

            int id = iUserService.insertUser(user);
            result.put("success", true);
            result.put("operation", "insertUser");
            result.put("insertedId", id);
            result.put("data", user);
            result.put("message", "User inserted successfully with ID: " + id);
        } catch (Exception e) {
            result.put("success", false);
            result.put("operation", "insertUser");
            result.put("message", "Error: " + e.getMessage());
        }
        return result;
    }

    /**
     * 测试4: 更新用户
     */
    @PutMapping("/test/updateUser")
    public Map<String, Object> testUpdateUser(@RequestParam Long id, @RequestBody(required = false) Map<String, Object> userData) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (userData == null) {
                userData = new HashMap<>();
            }
            User user = new User();
            user.setId(id);
            if (userData.containsKey("username")) {
                user.setUsername(userData.get("username").toString());
            }
            if (userData.containsKey("password")) {
                user.setPassword(userData.get("password").toString());
            }
            if (userData.containsKey("enabled")) {
                user.setEnabled(Boolean.parseBoolean(userData.get("enabled").toString()));
            }
            user.setUpdateTime(new Date());

            int affectedRows = iUserService.updateUserById(user);
            result.put("success", affectedRows > 0);
            result.put("operation", "updateUserById");
            result.put("affectedRows", affectedRows);
            result.put("message", affectedRows > 0 ? "User updated successfully" : "No user updated");
        } catch (Exception e) {
            result.put("success", false);
            result.put("operation", "updateUserById");
            result.put("message", "Error: " + e.getMessage());
        }
        return result;
    }

    /**
     * 测试5: 删除用户
     */
    @DeleteMapping("/test/deleteUser")
    public Map<String, Object> testDeleteUser(@RequestParam Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            int affectedRows = iUserService.deleteById(id);
            result.put("success", affectedRows > 0);
            result.put("operation", "deleteById");
            result.put("affectedRows", affectedRows);
            result.put("message", affectedRows > 0 ? "User deleted successfully" : "No user deleted");
        } catch (Exception e) {
            result.put("success", false);
            result.put("operation", "deleteById");
            result.put("message", "Error: " + e.getMessage());
        }
        return result;
    }

    /**
     * 测试6: 分页查询
     */
    @GetMapping("/test/listUserPage")
    public Map<String, Object> testListUserPage(
            @RequestParam(value = "username", defaultValue = "") String username,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "offset", defaultValue = "0") Integer offset) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<User> users = iUserService.listUserPage(username, pageSize, offset);
            result.put("success", true);
            result.put("operation", "listUserPage");
            result.put("data", users);
            result.put("count", users != null ? users.size() : 0);
            
            Map<String, Object> params = new HashMap<>();
            params.put("username", username);
            params.put("pageSize", pageSize);
            params.put("offset", offset);
            result.put("params", params);
            
            result.put("message", "Query returned " + (users != null ? users.size() : 0) + " users");
        } catch (Exception e) {
            result.put("success", false);
            result.put("operation", "listUserPage");
            result.put("message", "Error: " + e.getMessage());
        }
        return result;
    }

    /**
     * 测试7: 完整CRUD流程测试
     */
    @GetMapping("/test/fullCrud")
    public Map<String, Object> testFullCrudFlow() {
        Map<String, Object> result = new HashMap<>();
        int passed = 0;
        int failed = 0;
        String timestamp = String.valueOf(System.currentTimeMillis());

        // Step 1: Insert
        try {
            User newUser = new User();
            newUser.setUsername("mybatis_crud_" + timestamp);
            newUser.setPassword("password123");
            newUser.setUnexpired(true);
            newUser.setEnabled(true);
            newUser.setUnlocked(true);
            newUser.setDeleted(false);
            newUser.setCreateTime(new Date());
            newUser.setUpdateTime(new Date());

            int insertedId = iUserService.insertUser(newUser);
            newUser.setId((long) insertedId);
            
            Map<String, Object> step1Result = new HashMap<>();
            step1Result.put("success", true);
            step1Result.put("id", insertedId);
            result.put("step1_insert", step1Result);
            passed++;

            // Step 2: Query by ID
            User fetchedUser = iUserService.getUserById((long) insertedId);
            if (fetchedUser != null && fetchedUser.getUsername().equals("mybatis_crud_" + timestamp)) {
                Map<String, Object> step2Result = new HashMap<>();
                step2Result.put("success", true);
                step2Result.put("data", fetchedUser);
                result.put("step2_select", step2Result);
                passed++;
            } else {
                Map<String, Object> step2Result = new HashMap<>();
                step2Result.put("success", false);
                step2Result.put("message", "User not found or mismatch");
                result.put("step2_select", step2Result);
                failed++;
            }

            // Step 3: Update
            newUser.setEnabled(false);
            int updated = iUserService.updateUserById(newUser);
            if (updated > 0) {
                User updatedUser = iUserService.getUserById((long) insertedId);
                if (updatedUser != null && updatedUser.getEnabled() != null && !updatedUser.getEnabled()) {
                    Map<String, Object> step3Result = new HashMap<>();
                    step3Result.put("success", true);
                    step3Result.put("message", "Update verified");
                    result.put("step3_update", step3Result);
                    passed++;
                } else {
                    Map<String, Object> step3Result = new HashMap<>();
                    step3Result.put("success", false);
                    step3Result.put("message", "Update not reflected");
                    result.put("step3_update", step3Result);
                    failed++;
                }
            } else {
                Map<String, Object> step3Result = new HashMap<>();
                step3Result.put("success", false);
                step3Result.put("message", "No rows affected");
                result.put("step3_update", step3Result);
                failed++;
            }

            // Step 4: Delete
            int deleted = iUserService.deleteById((long) insertedId);
            if (deleted > 0) {
                User deletedUser = iUserService.getUserById((long) insertedId);
                if (deletedUser == null) {
                    Map<String, Object> step4Result = new HashMap<>();
                    step4Result.put("success", true);
                    step4Result.put("message", "Delete verified");
                    result.put("step4_delete", step4Result);
                    passed++;
                } else {
                    Map<String, Object> step4Result = new HashMap<>();
                    step4Result.put("success", false);
                    step4Result.put("message", "User still exists after delete");
                    result.put("step4_delete", step4Result);
                    failed++;
                }
            } else {
                Map<String, Object> step4Result = new HashMap<>();
                step4Result.put("success", false);
                step4Result.put("message", "No rows affected");
                result.put("step4_delete", step4Result);
                failed++;
            }

        } catch (Exception e) {
            result.put("error", "CRUD flow interrupted: " + e.getMessage());
            failed++;
        }

        Map<String, Object> summary = new HashMap<>();
        summary.put("total", passed + failed);
        summary.put("passed", passed);
        summary.put("failed", failed);
        result.put("summary", summary);
        result.put("crudSuccess", failed == 0);
        return result;
    }

    /**
     * 测试8: 所有功能测试
     */
    @GetMapping("/test/all")
    public Map<String, Object> testAll() {
        Map<String, Object> result = new HashMap<>();
        int totalTests = 0;
        int passedTests = 0;

        // Test 1: Get user by ID
        try {
            User user = iUserService.getUserById(1L);
            result.put("test1_getUserById", user != null);
            totalTests++;
            if (user != null) passedTests++;
        } catch (Exception e) {
            result.put("test1_getUserById", "FAILED: " + e.getMessage());
            totalTests++;
        }

        // Test 2: Insert user
        try {
            User newUser = new User();
            newUser.setUsername("mybatis_test_" + System.currentTimeMillis());
            newUser.setPassword("pass");
            newUser.setUnexpired(true);
            newUser.setEnabled(true);
            newUser.setUnlocked(true);
            newUser.setDeleted(false);
            newUser.setCreateTime(new Date());
            newUser.setUpdateTime(new Date());
            int id = iUserService.insertUser(newUser);
            result.put("test2_insertUser", id > 0);
            totalTests++;
            if (id > 0) passedTests++;
        } catch (Exception e) {
            result.put("test2_insertUser", "FAILED: " + e.getMessage());
            totalTests++;
        }

        // Test 3: Get user by username
        try {
            User user = iUserService.getUserByUsername("admin");
            result.put("test3_getUserByUsername", user != null);
            totalTests++;
            if (user != null) passedTests++;
        } catch (Exception e) {
            result.put("test3_getUserByUsername", "FAILED: " + e.getMessage());
            totalTests++;
        }

        // Test 4: List with pagination
        try {
            List<User> users = iUserService.listUserPage("", 10, 0);
            result.put("test4_listUserPage", users != null && users.size() > 0);
            totalTests++;
            if (users != null && users.size() > 0) passedTests++;
        } catch (Exception e) {
            result.put("test4_listUserPage", "FAILED: " + e.getMessage());
            totalTests++;
        }

        result.put("totalTests", totalTests);
        result.put("passedTests", passedTests);
        result.put("failedTests", totalTests - passedTests);
        result.put("success", passedTests == totalTests);

        return result;
    }
}
