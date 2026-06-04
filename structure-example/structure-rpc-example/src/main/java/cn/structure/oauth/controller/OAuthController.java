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

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 模拟OAuth2授权服务器控制器
 * 提供Token获取和刷新功能，用于演示Bearer认证流程
 *
 * @author chuck
 */
@Slf4j
@RestController
@RequestMapping("/oauth")
public class OAuthController {

    /**
     * 存储有效tokens
     */
    private static final Map<String, TokenData> tokenStore = new ConcurrentHashMap<>();

    /**
     * 存储refresh tokens
     */
    private static final Map<String, TokenData> refreshTokenStore = new ConcurrentHashMap<>();

    /**
     * 模拟客户端配置
     */
    private static final Map<String, String> clients = new ConcurrentHashMap<>();
    static {
        clients.put("client_id", "client_secret");
        clients.put("admin", "password");
        clients.put("test_client", "test_secret");
    }

    /**
     * 存储Token数据的内部类
     */
    static class TokenData {
        String accessToken;
        String refreshToken;
        String clientId;
        long expiresAt;

        TokenData(String accessToken, String refreshToken, String clientId, long expiresAt) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
            this.clientId = clientId;
            this.expiresAt = expiresAt;
        }
    }

    /**
     * 获取Token接口（参数方式传递client_id/client_secret）
     * POST /oauth/token
     *
     * @param grantType 授权类型（client_credentials / refresh_token）
     * @param clientId 客户端ID
     * @param clientSecret 客户端密钥
     * @param refreshToken refresh token（用于refresh_token授权类型）
     * @return Token响应
     */
    @PostMapping("/token")
    public ResponseEntity<Map<String, Object>> getToken(
            @RequestParam("grant_type") String grantType,
            @RequestParam(value = "client_id", required = false) String clientId,
            @RequestParam(value = "client_secret", required = false) String clientSecret,
            @RequestParam(value = "refresh_token", required = false) String refreshToken,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {

        log.info("[OAuthController] 获取Token请求 - grant_type: {}, client_id: {}", grantType, clientId);

        Map<String, Object> response = new HashMap<>();

        // 验证客户端
        String validClientId = null;
        
        // 优先检查Basic Auth
        if (authorizationHeader != null && authorizationHeader.startsWith("Basic ")) {
            try {
                String base64Credentials = authorizationHeader.substring(6);
                String credentials = new String(Base64.getDecoder().decode(base64Credentials), StandardCharsets.UTF_8);
                String[] parts = credentials.split(":", 2);
                if (parts.length == 2) {
                    String authClientId = parts[0];
                    String authClientSecret = parts[1];
                    if (clients.containsKey(authClientId) && clients.get(authClientId).equals(authClientSecret)) {
                        validClientId = authClientId;
                    }
                }
            } catch (Exception e) {
                log.warn("[OAuthController] Basic Auth解析失败: {}", e.getMessage());
            }
        }

        // 如果Basic Auth验证失败，尝试参数方式
        if (validClientId == null && clientId != null && clientSecret != null) {
            if (clients.containsKey(clientId) && clients.get(clientId).equals(clientSecret)) {
                validClientId = clientId;
            }
        }

        if (validClientId == null) {
            response.put("error", "invalid_client");
            response.put("error_description", "客户端认证失败");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // 处理授权类型
        if ("client_credentials".equals(grantType)) {
            return generateTokenResponse(validClientId);
        } else if ("refresh_token".equals(grantType)) {
            return refreshTokenResponse(refreshToken);
        } else {
            response.put("error", "unsupported_grant_type");
            response.put("error_description", "不支持的授权类型");
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 生成Token响应
     */
    private ResponseEntity<Map<String, Object>> generateTokenResponse(String clientId) {
        String accessToken = UUID.randomUUID().toString().replace("-", "");
        String refreshToken = UUID.randomUUID().toString().replace("-", "");
        long expiresIn = 3600L; // 1小时
        long expiresAt = System.currentTimeMillis() + expiresIn * 1000;

        TokenData tokenData = new TokenData(accessToken, refreshToken, clientId, expiresAt);
        tokenStore.put(accessToken, tokenData);
        refreshTokenStore.put(refreshToken, tokenData);

        log.info("[OAuthController] Token生成成功 - client_id: {}, expires_in: {}s", clientId, expiresIn);

        Map<String, Object> response = new HashMap<>();
        response.put("access_token", accessToken);
        response.put("token_type", "Bearer");
        response.put("expires_in", expiresIn);
        response.put("refresh_token", refreshToken);
        response.put("scope", "read write");

        return ResponseEntity.ok(response);
    }

    /**
     * 刷新Token响应
     */
    private ResponseEntity<Map<String, Object>> refreshTokenResponse(String refreshToken) {
        Map<String, Object> response = new HashMap<>();

        if (refreshToken == null || refreshToken.isEmpty()) {
            response.put("error", "invalid_request");
            response.put("error_description", "refresh_token不能为空");
            return ResponseEntity.badRequest().body(response);
        }

        TokenData tokenData = refreshTokenStore.get(refreshToken);
        if (tokenData == null) {
            response.put("error", "invalid_grant");
            response.put("error_description", "无效的refresh_token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // 删除旧token
        tokenStore.remove(tokenData.accessToken);
        refreshTokenStore.remove(refreshToken);

        // 生成新token
        return generateTokenResponse(tokenData.clientId);
    }

    /**
     * 验证Token接口
     * GET /oauth/validate
     */
    @GetMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateToken(
            @RequestHeader("Authorization") String authorizationHeader) {

        Map<String, Object> response = new HashMap<>();

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            response.put("valid", false);
            response.put("error", "invalid_token");
            response.put("error_description", "无效的Token格式");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        String accessToken = authorizationHeader.substring(7);
        TokenData tokenData = tokenStore.get(accessToken);

        if (tokenData == null) {
            response.put("valid", false);
            response.put("error", "invalid_token");
            response.put("error_description", "无效的access_token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        if (System.currentTimeMillis() > tokenData.expiresAt) {
            tokenStore.remove(accessToken);
            response.put("valid", false);
            response.put("error", "token_expired");
            response.put("error_description", "Token已过期");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        response.put("valid", true);
        response.put("client_id", tokenData.clientId);
        response.put("expires_at", tokenData.expiresAt);

        return ResponseEntity.ok(response);
    }

    /**
     * 健康检查
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "OAuth2 Authorization Server (Mock)");
        response.put("description", "模拟OAuth2授权服务器，用于演示Bearer认证流程");
        response.put("endpoints", new String[]{"/oauth/token", "/oauth/validate"});
        return ResponseEntity.ok(response);
    }

    /**
     * 获取Token信息（演示用）
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getInfo() {
        Map<String, Object> response = new HashMap<>();
        response.put("service", "OAuth2 Authorization Server (Mock)");
        response.put("description", "模拟OAuth2授权服务器");
        response.put("supported_grant_types", new String[]{"client_credentials", "refresh_token"});
        response.put("supported_auth_methods", new String[]{"Basic Auth", "Query Parameters"});
        response.put("registered_clients", clients.keySet());
        response.put("active_tokens", tokenStore.size());
        return ResponseEntity.ok(response);
    }
}