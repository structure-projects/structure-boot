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
package cn.structured.rpc.handler;

import cn.structure.common.utils.BasicAuthGenerator;
import cn.structure.common.utils.HttpClientUtil;
import cn.structured.rpc.emuns.AuthType;
import cn.structured.rpc.entity.RemoteService;
import cn.structured.rpc.entity.TokenInfo;
import cn.structured.rpc.token.DefaultTokenManager;
import cn.structured.rpc.token.DefaultTokenProvider;
import cn.structured.rpc.token.TokenManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * client处理器
 * 支持完整的RESTful风格HTTP调用
 *
 * @author chuck
 * @version 2024/07/17 下午4:47
 * @since 1.8
 */
@Slf4j
public class BaseHttpClient implements IRpcHandler {

    private HttpHost httpHost;
    private HttpClient httpClient;
    private AuthType authType = AuthType.NONE;
    private String accessKey;
    private String secretKey;
    private TokenManager tokenManager;

    @Override
    public void init(String host, Integer port) {
        log.info("[RpcClient] 初始化RPC客户端 - host: {}, port: {}", host, port);
        this.httpHost = new HttpHost(host, port);
        this.httpClient = HttpClientUtil.getHttpClient();
        log.info("[RpcClient] RPC客户端初始化完成 - httpHost: {}", httpHost);
    }

    public void init(String host, Integer port, RemoteService remoteService) {
        init(host, port);
        if (remoteService != null) {
            this.authType = remoteService.getAuthType() != null ? remoteService.getAuthType() : AuthType.NONE;
            this.accessKey = remoteService.getAccessKey();
            this.secretKey = remoteService.getSecretKey();
            
            if (authType == AuthType.BEARER) {
                this.tokenManager = new DefaultTokenManager(new DefaultTokenProvider(remoteService));
                log.info("[RpcClient] Bearer认证模式已初始化");
            } else if (authType == AuthType.BASIC) {
                log.info("[RpcClient] Basic认证模式已初始化");
            }
        }
    }

    @Override
    public void preposition(HttpRequest httpRequest) {
        switch (authType) {
            case BASIC:
                addBasicAuthHeader(httpRequest);
                break;
            case BEARER:
                addBearerAuthHeader(httpRequest);
                break;
            case NONE:
            default:
                break;
        }
    }

    private void addBasicAuthHeader(HttpRequest httpRequest) {
        if (accessKey != null && !accessKey.isEmpty() && secretKey != null && !secretKey.isEmpty()) {
            String authHeader = BasicAuthGenerator.generate(accessKey, secretKey);
            httpRequest.setHeader("Authorization", authHeader);
            log.debug("[RpcClient] 添加Basic认证头");
        }
    }

    private void addBearerAuthHeader(HttpRequest httpRequest) {
        if (tokenManager != null) {
            TokenInfo tokenInfo = tokenManager.getToken();
            if (tokenInfo != null && tokenInfo.getAccessToken() != null) {
                String authHeader = "Bearer " + tokenInfo.getAccessToken();
                httpRequest.setHeader("Authorization", authHeader);
                log.debug("[RpcClient] 添加Bearer认证头");
            }
        }
    }

    private void addCustomHeaders(HttpRequest httpRequest, Map<String, String> headers) {
        if (headers != null && !headers.isEmpty()) {
            headers.forEach((key, value) -> {
                httpRequest.setHeader(key, value);
                log.debug("[RpcClient] 添加自定义头 - {}: {}", key, value);
            });
        }
    }

    @Override
    public HttpResponse handler(HttpRequest httpRequest) {
        try {
            preposition(httpRequest);
            log.debug("[RpcClient] 发送RPC请求 - target: {}, requestLine: {}", httpHost, httpRequest.getRequestLine());
            HttpResponse execute = httpClient.execute(httpHost, httpRequest);
            postposition(execute);
            log.info("[RpcClient] RPC请求成功 - target: {}, statusLine: {}", httpHost, execute.getStatusLine());
            return execute;
        } catch (IOException e) {
            log.error("[RpcClient] RPC请求失败 - target: {}, error: {}", httpHost, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void postposition(HttpResponse httpResponse) {
        if (authType == AuthType.BEARER && tokenManager != null) {
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_UNAUTHORIZED || statusCode == HttpStatus.SC_FORBIDDEN) {
                log.warn("[RpcClient] 请求被拒绝，尝试刷新Token - statusCode: {}", statusCode);
                tokenManager.invalidateToken();
                tokenManager.refreshToken();
            }
        }
    }

    public AuthType getAuthType() {
        return authType;
    }

    public void setAuthType(AuthType authType) {
        this.authType = authType;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public TokenManager getTokenManager() {
        return tokenManager;
    }

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public <T> T get(String path, Class<T> responseType) {
        return get(path, null, responseType);
    }

    public <T> T get(String path, Map<String, String> headers, Class<T> responseType) {
        try {
            HttpGet httpGet = new HttpGet(path);
            addCustomHeaders(httpGet, headers);
            HttpResponse response = handler(httpGet);
            return parseResponse(response, responseType);
        } catch (Exception e) {
            log.error("[RpcClient] GET请求失败 - path: {}, error: {}", path, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public <T> T get(String path, TypeReference<T> responseType) {
        return get(path, null, responseType);
    }

    public <T> T get(String path, Map<String, String> headers, TypeReference<T> responseType) {
        try {
            HttpGet httpGet = new HttpGet(path);
            addCustomHeaders(httpGet, headers);
            HttpResponse response = handler(httpGet);
            return parseResponse(response, responseType);
        } catch (Exception e) {
            log.error("[RpcClient] GET请求失败 - path: {}, error: {}", path, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public <T> T post(String path, Object body, Class<T> responseType) {
        return post(path, body, null, responseType);
    }

    public <T> T post(String path, Object body, Map<String, String> headers, Class<T> responseType) {
        try {
            HttpPost httpPost = new HttpPost(path);
            if (body != null) {
                String jsonBody = objectMapper.writeValueAsString(body);
                httpPost.setEntity(new StringEntity(jsonBody, StandardCharsets.UTF_8));
                httpPost.setHeader("Content-Type", "application/json");
            }
            addCustomHeaders(httpPost, headers);
            HttpResponse response = handler(httpPost);
            return parseResponse(response, responseType);
        } catch (Exception e) {
            log.error("[RpcClient] POST请求失败 - path: {}, error: {}", path, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public <T> T post(String path, Object body, TypeReference<T> responseType) {
        return post(path, body, null, responseType);
    }

    public <T> T post(String path, Object body, Map<String, String> headers, TypeReference<T> responseType) {
        try {
            HttpPost httpPost = new HttpPost(path);
            if (body != null) {
                String jsonBody = objectMapper.writeValueAsString(body);
                httpPost.setEntity(new StringEntity(jsonBody, StandardCharsets.UTF_8));
                httpPost.setHeader("Content-Type", "application/json");
            }
            addCustomHeaders(httpPost, headers);
            HttpResponse response = handler(httpPost);
            return parseResponse(response, responseType);
        } catch (Exception e) {
            log.error("[RpcClient] POST请求失败 - path: {}, error: {}", path, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public <T> T put(String path, Object body, Class<T> responseType) {
        return put(path, body, null, responseType);
    }

    public <T> T put(String path, Object body, Map<String, String> headers, Class<T> responseType) {
        try {
            HttpPut httpPut = new HttpPut(path);
            if (body != null) {
                String jsonBody = objectMapper.writeValueAsString(body);
                httpPut.setEntity(new StringEntity(jsonBody, StandardCharsets.UTF_8));
                httpPut.setHeader("Content-Type", "application/json");
            }
            addCustomHeaders(httpPut, headers);
            HttpResponse response = handler(httpPut);
            return parseResponse(response, responseType);
        } catch (Exception e) {
            log.error("[RpcClient] PUT请求失败 - path: {}, error: {}", path, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public <T> T put(String path, Object body, TypeReference<T> responseType) {
        return put(path, body, null, responseType);
    }

    public <T> T put(String path, Object body, Map<String, String> headers, TypeReference<T> responseType) {
        try {
            HttpPut httpPut = new HttpPut(path);
            if (body != null) {
                String jsonBody = objectMapper.writeValueAsString(body);
                httpPut.setEntity(new StringEntity(jsonBody, StandardCharsets.UTF_8));
                httpPut.setHeader("Content-Type", "application/json");
            }
            addCustomHeaders(httpPut, headers);
            HttpResponse response = handler(httpPut);
            return parseResponse(response, responseType);
        } catch (Exception e) {
            log.error("[RpcClient] PUT请求失败 - path: {}, error: {}", path, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public <T> T patch(String path, Object body, Class<T> responseType) {
        return patch(path, body, null, responseType);
    }

    public <T> T patch(String path, Object body, Map<String, String> headers, Class<T> responseType) {
        try {
            HttpPatch httpPatch = new HttpPatch(path);
            if (body != null) {
                String jsonBody = objectMapper.writeValueAsString(body);
                httpPatch.setEntity(new StringEntity(jsonBody, StandardCharsets.UTF_8));
                httpPatch.setHeader("Content-Type", "application/json");
            }
            addCustomHeaders(httpPatch, headers);
            HttpResponse response = handler(httpPatch);
            return parseResponse(response, responseType);
        } catch (Exception e) {
            log.error("[RpcClient] PATCH请求失败 - path: {}, error: {}", path, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public <T> T patch(String path, Object body, TypeReference<T> responseType) {
        return patch(path, body, null, responseType);
    }

    public <T> T patch(String path, Object body, Map<String, String> headers, TypeReference<T> responseType) {
        try {
            HttpPatch httpPatch = new HttpPatch(path);
            if (body != null) {
                String jsonBody = objectMapper.writeValueAsString(body);
                httpPatch.setEntity(new StringEntity(jsonBody, StandardCharsets.UTF_8));
                httpPatch.setHeader("Content-Type", "application/json");
            }
            addCustomHeaders(httpPatch, headers);
            HttpResponse response = handler(httpPatch);
            return parseResponse(response, responseType);
        } catch (Exception e) {
            log.error("[RpcClient] PATCH请求失败 - path: {}, error: {}", path, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public void delete(String path) {
        delete(path, null);
    }

    public void delete(String path, Map<String, String> headers) {
        try {
            HttpDelete httpDelete = new HttpDelete(path);
            addCustomHeaders(httpDelete, headers);
            handler(httpDelete);
        } catch (Exception e) {
            log.error("[RpcClient] DELETE请求失败 - path: {}, error: {}", path, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public <T> T delete(String path, Map<String, String> headers, Class<T> responseType) {
        try {
            HttpDelete httpDelete = new HttpDelete(path);
            addCustomHeaders(httpDelete, headers);
            HttpResponse response = handler(httpDelete);
            return parseResponse(response, responseType);
        } catch (Exception e) {
            log.error("[RpcClient] DELETE请求失败 - path: {}, error: {}", path, e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private <T> T parseResponse(HttpResponse response, Class<T> responseType) throws IOException {
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode >= 200 && statusCode < 300) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String responseBody = new String(entity.getContent().readAllBytes(), StandardCharsets.UTF_8);
                log.debug("[RpcClient] 响应内容: {}", responseBody);
                if (responseType == String.class) {
                    return (T) responseBody;
                }
                return objectMapper.readValue(responseBody, responseType);
            }
            return null;
        } else {
            throw new RuntimeException("RPC请求失败，状态码: " + statusCode);
        }
    }

    private <T> T parseResponse(HttpResponse response, TypeReference<T> responseType) throws IOException {
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode >= 200 && statusCode < 300) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String responseBody = new String(entity.getContent().readAllBytes(), StandardCharsets.UTF_8);
                log.debug("[RpcClient] 响应内容: {}", responseBody);
                return objectMapper.readValue(responseBody, responseType);
            }
            return null;
        } else {
            throw new RuntimeException("RPC请求失败，状态码: " + statusCode);
        }
    }
}
