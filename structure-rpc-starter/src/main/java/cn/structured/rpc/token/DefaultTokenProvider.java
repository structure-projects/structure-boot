package cn.structured.rpc.token;

import cn.structure.common.utils.BasicAuthGenerator;
import cn.structure.common.utils.HttpClientUtil;
import cn.structured.rpc.entity.RemoteService;
import cn.structured.rpc.entity.TokenInfo;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.HttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 默认Token提供者实现
 *
 * @author chuck
 */
@Slf4j
public class DefaultTokenProvider implements TokenProvider {

    private final RemoteService remoteService;
    private final HttpClient httpClient;

    public DefaultTokenProvider(RemoteService remoteService) {
        this.remoteService = remoteService;
        this.httpClient = HttpClientUtil.getHttpClient();
    }

    @Override
    public TokenInfo fetchToken() {
        log.debug("[TokenProvider] 开始获取Token - authHost: {}, authPort: {}", 
            remoteService.getAuthHost(), remoteService.getAuthPort());
        
        String tokenUrl = buildTokenUrl();
        
        try {
            HttpPost httpPost = new HttpPost(tokenUrl);
            
            if (remoteService.getHeaders() != null) {
                remoteService.getHeaders().forEach((key, values) -> {
                    values.forEach(value -> httpPost.addHeader(key, value));
                });
            }

            Boolean useBasicAuth = remoteService.getTokenBasicAuth();
            if (Boolean.TRUE.equals(useBasicAuth)) {
                log.debug("[TokenProvider] 使用Basic Auth获取Token");
                if (remoteService.getAccessKey() != null && !remoteService.getAccessKey().isEmpty() 
                    && remoteService.getSecretKey() != null && !remoteService.getSecretKey().isEmpty()) {
                    String authHeader = BasicAuthGenerator.generate(remoteService.getAccessKey(), remoteService.getSecretKey());
                    httpPost.setHeader("Authorization", authHeader);
                }
            }
            
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("grant_type", "client_credentials"));
            
            if (!Boolean.TRUE.equals(useBasicAuth)) {
                params.add(new BasicNameValuePair("client_id", remoteService.getAccessKey()));
                params.add(new BasicNameValuePair("client_secret", remoteService.getSecretKey()));
            }
            
            httpPost.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));
            
            HttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            
            if (statusCode >= 200 && statusCode < 300) {
                HttpEntity entity = response.getEntity();
                String responseBody = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                return parseTokenResponse(responseBody);
            } else {
                log.error("[TokenProvider] 获取Token失败 - statusCode: {}", statusCode);
                throw new RuntimeException("Failed to fetch token, status code: " + statusCode);
            }
        } catch (IOException e) {
            log.error("[TokenProvider] 获取Token异常 - error: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch token", e);
        }
    }

    @Override
    public TokenInfo refreshToken(String refreshToken) {
        log.debug("[TokenProvider] 开始刷新Token");
        
        String tokenUrl = buildTokenUrl();
        
        try {
            HttpPost httpPost = new HttpPost(tokenUrl);
            
            if (remoteService.getHeaders() != null) {
                remoteService.getHeaders().forEach((key, values) -> {
                    values.forEach(value -> httpPost.addHeader(key, value));
                });
            }

            Boolean useBasicAuth = remoteService.getTokenBasicAuth();
            if (Boolean.TRUE.equals(useBasicAuth)) {
                log.debug("[TokenProvider] 使用Basic Auth刷新Token");
                if (remoteService.getAccessKey() != null && !remoteService.getAccessKey().isEmpty() 
                    && remoteService.getSecretKey() != null && !remoteService.getSecretKey().isEmpty()) {
                    String authHeader = BasicAuthGenerator.generate(remoteService.getAccessKey(), remoteService.getSecretKey());
                    httpPost.setHeader("Authorization", authHeader);
                }
            }
            
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("grant_type", "refresh_token"));
            params.add(new BasicNameValuePair("refresh_token", refreshToken));
            
            if (!Boolean.TRUE.equals(useBasicAuth)) {
                params.add(new BasicNameValuePair("client_id", remoteService.getAccessKey()));
                params.add(new BasicNameValuePair("client_secret", remoteService.getSecretKey()));
            }
            
            httpPost.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));
            
            HttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            
            if (statusCode >= 200 && statusCode < 300) {
                HttpEntity entity = response.getEntity();
                String responseBody = EntityUtils.toString(entity, StandardCharsets.UTF_8);
                return parseTokenResponse(responseBody);
            } else {
                log.warn("[TokenProvider] 刷新Token失败，尝试重新获取 - statusCode: {}", statusCode);
                return fetchToken();
            }
        } catch (IOException e) {
            log.error("[TokenProvider] 刷新Token异常 - error: {}", e.getMessage(), e);
            return fetchToken();
        }
    }

    private String buildTokenUrl() {
        String authPath = remoteService.getAuthPath();
        if (authPath == null || authPath.isEmpty()) {
            authPath = "/oauth/token";
        }
        if (!authPath.startsWith("/")) {
            authPath = "/" + authPath;
        }
        return String.format("http://%s:%d%s", 
            remoteService.getAuthHost(), 
            remoteService.getAuthPort() != null ? remoteService.getAuthPort() : 80,
            authPath);
    }

    private TokenInfo parseTokenResponse(String responseBody) {
        try {
            TokenInfo tokenInfo = JSON.parseObject(responseBody, TokenInfo.class);
            
            if (tokenInfo.getExpiresIn() != null && tokenInfo.getExpiresIn() > 0) {
                tokenInfo.setExpireTime(LocalDateTime.now().plusSeconds(tokenInfo.getExpiresIn()));
            }
            
            if (tokenInfo.getRefreshToken() == null || tokenInfo.getRefreshToken().isEmpty()) {
                log.debug("[TokenProvider] Token响应中不包含refresh_token，将使用client_credentials模式重新获取");
            }
            
            log.debug("[TokenProvider] Token获取成功 - expiresIn: {}s, hasRefreshToken: {}", 
                tokenInfo.getExpiresIn(), 
                tokenInfo.getRefreshToken() != null && !tokenInfo.getRefreshToken().isEmpty());
            return tokenInfo;
        } catch (Exception e) {
            log.error("[TokenProvider] 解析Token响应失败 - responseBody: {}, error: {}", responseBody, e.getMessage(), e);
            throw new RuntimeException("Failed to parse token response", e);
        }
    }
}