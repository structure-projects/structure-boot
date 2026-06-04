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
import cn.structure.oauth.client.UserClientBasic;
import cn.structure.oauth.client.UserClientBearer;
import cn.structure.oauth.client.UserClientBearerBasic;
import cn.structure.oauth.dto.UserCreateRequest;
import cn.structure.oauth.dto.UserInfo;
import cn.structure.oauth.dto.UserUpdateRequest;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * RPC 测试控制器 - 演示使用@RpcClient进行远程调用（类似FeignClient）
 * </p>
 *
 * @author chuck
 * @version 1.0.0
 * @since 2025-05-24
 */
@Slf4j
@RestController
@RequestMapping("/rpc")
public class RpcTestController {

    @Resource
    private UserClient userClient;

    @Resource
    private UserClientBasic userClientBasic;

    @Resource
    private UserClientBearer userClientBearer;

    @Resource
    private UserClientBearerBasic userClientBearerBasic;

    @Value("${server.port:18002}")
    private String serverPort;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "UP");
        result.put("message", "RPC Example is working");
        result.put("serverPort", serverPort);
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }

    @GetMapping("/auth/clients")
    public Map<String, Object> getAuthClients() {
        Map<String, Object> result = new HashMap<>();
        
        Map<String, Object> noneClient = new HashMap<>();
        noneClient.put("name", "UserClient");
        noneClient.put("authType", "NONE");
        noneClient.put("description", "无认证方式");
        noneClient.put("interfaceClass", UserClient.class.getName());
        result.put("none", noneClient);
        
        Map<String, Object> basicClient = new HashMap<>();
        basicClient.put("name", "UserClientBasic");
        basicClient.put("authType", "BASIC");
        basicClient.put("description", "Basic认证：使用BasicAuthGenerator生成认证头");
        basicClient.put("interfaceClass", UserClientBasic.class.getName());
        result.put("basic", basicClient);
        
        Map<String, Object> bearerClient = new HashMap<>();
        bearerClient.put("name", "UserClientBearer");
        bearerClient.put("authType", "BEARER");
        bearerClient.put("description", "Bearer认证：参数方式传递client_id/client_secret");
        bearerClient.put("interfaceClass", UserClientBearer.class.getName());
        result.put("bearer", bearerClient);
        
        Map<String, Object> bearerBasicClient = new HashMap<>();
        bearerBasicClient.put("name", "UserClientBearerBasic");
        bearerBasicClient.put("authType", "BEARER");
        bearerBasicClient.put("description", "Bearer认证：Basic Auth方式获取Token");
        bearerBasicClient.put("interfaceClass", UserClientBearerBasic.class.getName());
        result.put("bearerBasic", bearerBasicClient);
        
        return result;
    }

    @GetMapping("/test/none")
    public Map<String, Object> testNoneAuth(@RequestParam(defaultValue = "1") Long userId) {
        return executeUserCall(userClient, "none", "UserClient", userId);
    }

    @GetMapping("/test/basic")
    public Map<String, Object> testBasicAuth(@RequestParam(defaultValue = "1") Long userId) {
        return executeUserCall(userClientBasic, "basic", "UserClientBasic", userId);
    }

    @GetMapping("/test/bearer")
    public Map<String, Object> testBearerAuth(@RequestParam(defaultValue = "1") Long userId) {
        return executeUserCall(userClientBearer, "bearer", "UserClientBearer", userId);
    }

    @GetMapping("/test/bearer-basic")
    public Map<String, Object> testBearerBasicAuth(@RequestParam(defaultValue = "1") Long userId) {
        return executeUserCall(userClientBearerBasic, "bearer-basic", "UserClientBearerBasic", userId);
    }

    private Map<String, Object> executeUserCall(Object client, String authType, String clientName, Long userId) {
        Map<String, Object> result = new HashMap<>();
        result.put("authType", authType);
        result.put("clientName", clientName);
        result.put("userId", userId);
        
        try {
            Object user = null;
            if (client instanceof UserClientBasic) {
                user = ((UserClientBasic) client).getUserById(userId);
            } else if (client instanceof UserClientBearer) {
                user = ((UserClientBearer) client).getUserById(userId);
            } else if (client instanceof UserClientBearerBasic) {
                user = ((UserClientBearerBasic) client).getUserById(userId);
            } else if (client instanceof UserClient) {
                user = ((UserClient) client).getUserById(userId);
            }
            
            result.put("success", true);
            result.put("message", "RPC调用成功");
            result.put("user", user);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "RPC调用失败: " + e.getMessage());
            log.error("[RpcTestController] RPC调用失败 - authType: {}, client: {}, userId: {}, error: {}", authType, clientName, userId, e.getMessage(), e);
        }
        
        return result;
    }

    @GetMapping("/test/all-auth")
    public Map<String, Object> testAllAuthMethods(@RequestParam(defaultValue = "1") Long userId) {
        Map<String, Object> result = new HashMap<>();
        int passed = 0;
        int failed = 0;
        
        Map<String, Object> noneResult = testNoneAuth(userId);
        result.put("none", noneResult);
        if ((Boolean) noneResult.get("success")) passed++;
        else failed++;
        
        Map<String, Object> basicResult = testBasicAuth(userId);
        result.put("basic", basicResult);
        if ((Boolean) basicResult.get("success")) passed++;
        else failed++;
        
        Map<String, Object> bearerResult = testBearerAuth(userId);
        result.put("bearer", bearerResult);
        if ((Boolean) bearerResult.get("success")) passed++;
        else failed++;
        
        Map<String, Object> bearerBasicResult = testBearerBasicAuth(userId);
        result.put("bearerBasic", bearerBasicResult);
        if ((Boolean) bearerBasicResult.get("success")) passed++;
        else failed++;
        
        result.put("totalTests", passed + failed);
        result.put("passed", passed);
        result.put("failed", failed);
        result.put("success", failed == 0);
        
        return result;
    }

    @PostMapping("/user/create")
    public Map<String, Object> createUser(@RequestBody UserCreateRequest request) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            UserInfo user = userClient.createUser(request);
            
            result.put("success", true);
            result.put("message", "用户创建成功");
            result.put("user", user);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "用户创建失败: " + e.getMessage());
        }
        
        return result;
    }

    @PutMapping("/user/update/{userId}")
    public Map<String, Object> updateUser(@PathVariable Long userId, @RequestBody UserUpdateRequest request) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            UserInfo user = userClient.updateUser(userId, request);
            
            result.put("success", true);
            result.put("message", "用户更新成功");
            result.put("user", user);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "用户更新失败: " + e.getMessage());
        }
        
        return result;
    }

    @DeleteMapping("/user/delete/{userId}")
    public Map<String, Object> deleteUser(@PathVariable Long userId) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            userClient.deleteUser(userId);
            
            result.put("success", true);
            result.put("message", "用户删除成功");
            result.put("userId", userId);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "用户删除失败: " + e.getMessage());
        }
        
        return result;
    }

    @GetMapping("/user/getById/{userId}")
    public Map<String, Object> getUserById(@PathVariable Long userId) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            UserInfo user = userClient.getUserById(userId);
            
            result.put("success", true);
            result.put("user", user);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取用户信息失败: " + e.getMessage());
        }
        
        return result;
    }

    @GetMapping("/user/getByUsername")
    public Map<String, Object> getUserByUsername(@RequestParam String username) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            UserInfo user = userClient.getUserByUsername(username);
            
            result.put("success", true);
            result.put("user", user);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取用户信息失败: " + e.getMessage());
        }
        
        return result;
    }

    @GetMapping("/oauth/token/param")
    public Map<String, Object> testGetTokenByParams() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            String url = "http://localhost:" + serverPort + "/oauth/token";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            
            Map<String, String> params = new HashMap<>();
            params.put("grant_type", "client_credentials");
            params.put("client_id", "client_id");
            params.put("client_secret", "client_secret");
            
            org.springframework.http.HttpEntity<Map<String, String>> request = new org.springframework.http.HttpEntity<>(params, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            
            result.put("success", true);
            result.put("method", "POST (参数方式)");
            result.put("endpoint", url);
            result.put("statusCode", response.getStatusCode().value());
            result.put("response", response.getBody());
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取Token失败: " + e.getMessage());
        }
        
        return result;
    }

    @GetMapping("/oauth/token/basic")
    public Map<String, Object> testGetTokenByBasicAuth() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            String url = "http://localhost:" + serverPort + "/oauth/token";
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setBasicAuth("client_id", "client_secret");
            
            Map<String, String> params = new HashMap<>();
            params.put("grant_type", "client_credentials");
            
            org.springframework.http.HttpEntity<Map<String, String>> request = new org.springframework.http.HttpEntity<>(params, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            
            result.put("success", true);
            result.put("method", "POST (Basic Auth方式)");
            result.put("endpoint", url);
            result.put("statusCode", response.getStatusCode().value());
            result.put("response", response.getBody());
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取Token失败: " + e.getMessage());
        }
        
        return result;
    }

    @GetMapping("/oauth/validate")
    public Map<String, Object> testValidateToken(@RequestParam String accessToken) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            String url = "http://localhost:" + serverPort + "/oauth/validate";
            
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            
            org.springframework.http.HttpEntity<Void> request = new org.springframework.http.HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, request, Map.class);
            
            result.put("success", true);
            result.put("method", "GET");
            result.put("endpoint", url);
            result.put("statusCode", response.getStatusCode().value());
            result.put("response", response.getBody());
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "验证Token失败: " + e.getMessage());
        }
        
        return result;
    }

    @GetMapping("/oauth/info")
    public Map<String, Object> getOAuthInfo() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            String url = "http://localhost:" + serverPort + "/oauth/info";
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            
            result.put("success", true);
            result.put("oauthServer", response);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取OAuth服务信息失败: " + e.getMessage());
        }
        
        return result;
    }

    @GetMapping("/oauth/demo")
    public Map<String, Object> demoBearerAuth() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            String tokenUrl = "http://localhost:" + serverPort + "/oauth/token";
            String validateUrl = "http://localhost:" + serverPort + "/oauth/validate";
            
            HttpHeaders tokenHeaders = new HttpHeaders();
            tokenHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            
            Map<String, String> tokenParams = new HashMap<>();
            tokenParams.put("grant_type", "client_credentials");
            tokenParams.put("client_id", "client_id");
            tokenParams.put("client_secret", "client_secret");
            
            org.springframework.http.HttpEntity<Map<String, String>> tokenRequest = new org.springframework.http.HttpEntity<>(tokenParams, tokenHeaders);
            ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(tokenUrl, tokenRequest, Map.class);
            
            if (tokenResponse.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> tokenData = tokenResponse.getBody();
                String accessToken = (String) tokenData.get("access_token");
                String tokenType = (String) tokenData.get("token_type");
                Long expiresIn = (Long) tokenData.get("expires_in");
                
                result.put("step1_getToken", Map.of(
                    "success", true,
                    "endpoint", tokenUrl,
                    "tokenType", tokenType,
                    "expiresIn", expiresIn + "s",
                    "hasRefreshToken", tokenData.get("refresh_token") != null
                ));
                
                HttpHeaders validateHeaders = new HttpHeaders();
                validateHeaders.set("Authorization", tokenType + " " + accessToken);
                
                org.springframework.http.HttpEntity<Void> validateRequest = new org.springframework.http.HttpEntity<>(validateHeaders);
                ResponseEntity<Map> validateResponse = restTemplate.exchange(validateUrl, HttpMethod.GET, validateRequest, Map.class);
                
                result.put("step2_validateToken", Map.of(
                    "success", validateResponse.getStatusCode().is2xxSuccessful(),
                    "endpoint", validateUrl,
                    "statusCode", validateResponse.getStatusCode().value(),
                    "response", validateResponse.getBody()
                ));
                
                result.put("success", true);
                result.put("message", "Bearer认证流程演示成功");
            } else {
                result.put("success", false);
                result.put("message", "获取Token失败");
                result.put("step1_getToken", Map.of(
                    "success", false,
                    "statusCode", tokenResponse.getStatusCode().value(),
                    "response", tokenResponse.getBody()
                ));
            }
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Bearer认证流程演示失败: " + e.getMessage());
        }
        
        return result;
    }

    @GetMapping("/demo/endpoints")
    public Map<String, Object> getDemoEndpoints() {
        Map<String, Object> result = new HashMap<>();
        
        result.put("health", Map.of("method", "GET", "endpoint", "/rpc/health", "description", "健康检查"));
        result.put("authClients", Map.of("method", "GET", "endpoint", "/rpc/auth/clients", "description", "获取所有认证客户端配置"));
        result.put("getUserById", Map.of("method", "GET", "endpoint", "/rpc/user/getById/{userId}", "description", "获取用户信息"));
        result.put("getUserByUsername", Map.of("method", "GET", "endpoint", "/rpc/user/getByUsername?username=xxx", "description", "根据用户名获取用户信息"));
        result.put("createUser", Map.of("method", "POST", "endpoint", "/rpc/user/create", "description", "创建用户"));
        result.put("updateUser", Map.of("method", "PUT", "endpoint", "/rpc/user/update/{userId}", "description", "更新用户信息"));
        result.put("deleteUser", Map.of("method", "DELETE", "endpoint", "/rpc/user/delete/{userId}", "description", "删除用户"));
        result.put("testNone", Map.of("method", "GET", "endpoint", "/rpc/test/none?userId=1", "description", "测试无认证RPC调用"));
        result.put("testBasic", Map.of("method", "GET", "endpoint", "/rpc/test/basic?userId=1", "description", "测试Basic认证RPC调用"));
        result.put("testBearer", Map.of("method", "GET", "endpoint", "/rpc/test/bearer?userId=1", "description", "测试Bearer认证RPC调用（参数方式）"));
        result.put("testBearerBasic", Map.of("method", "GET", "endpoint", "/rpc/test/bearer-basic?userId=1", "description", "测试Bearer认证RPC调用（Basic Auth方式）"));
        result.put("testAllAuth", Map.of("method", "GET", "endpoint", "/rpc/test/all-auth?userId=1", "description", "测试所有认证方式"));
        result.put("oauthInfo", Map.of("method", "GET", "endpoint", "/rpc/oauth/info", "description", "获取OAuth服务信息"));
        result.put("getTokenByParams", Map.of("method", "GET", "endpoint", "/rpc/oauth/token/param", "description", "测试获取Token（参数方式）"));
        result.put("getTokenByBasic", Map.of("method", "GET", "endpoint", "/rpc/oauth/token/basic", "description", "测试获取Token（Basic Auth方式）"));
        result.put("bearerDemo", Map.of("method", "GET", "endpoint", "/rpc/oauth/demo", "description", "完整的Bearer认证流程演示"));
        
        return result;
    }

    private String maskValue(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        if (value.length() <= 4) {
            return "****";
        }
        return value.substring(0, 2) + "****" + value.substring(value.length() - 2);
    }
}
