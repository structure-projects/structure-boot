package cn.structure.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Basic Auth 认证头生成器
 * <p>
 * 用于生成 Base64 编码的 Basic Authentication 请求头
 * </p>
 *
 * <p>使用示例：</p>
 * <pre>{@code
 * String authHeader = BasicAuthGenerator.generate("username", "password");
 * // 结果：Basic dXNlcm5hbWU6cGFzc3dvcmQ=
 * }</pre>
 *
 * @author chuck
 */
@Slf4j
public class BasicAuthGenerator {

    private BasicAuthGenerator() {
    }

    /**
     * 生成 Basic Auth 认证头值
     *
     * @param username 用户名
     * @param password 密码
     * @return 完整的 Basic Auth 认证头值（包含 "Basic " 前缀）
     */
    public static String generate(String username, String password) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        String credentials = username + ":" + password;
        String base64Credentials = Base64.getEncoder()
                .encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
        
        String authHeader = "Basic " + base64Credentials;
        
        log.debug("Generated Basic Auth header for user: {}", username);
        return authHeader;
    }

    /**
     * 解析 Basic Auth 认证头
     *
     * @param authHeader 认证头值（包含 "Basic " 前缀或仅包含 Base64 字符串）
     * @return 包含用户名和密码的数组 [username, password]
     */
    public static String[] parse(String authHeader) {
        if (authHeader == null || authHeader.isEmpty()) {
            throw new IllegalArgumentException("Auth header cannot be null or empty");
        }

        String base64Part = authHeader;
        if (authHeader.startsWith("Basic ")) {
            base64Part = authHeader.substring(6);
        }

        try {
            byte[] decodedBytes = Base64.getDecoder().decode(base64Part);
            String credentials = new String(decodedBytes, StandardCharsets.UTF_8);
            
            int colonIndex = credentials.indexOf(':');
            if (colonIndex == -1) {
                throw new IllegalArgumentException("Invalid Basic Auth format: missing colon separator");
            }
            
            String username = credentials.substring(0, colonIndex);
            String password = credentials.substring(colonIndex + 1);
            
            log.debug("Parsed Basic Auth header for user: {}", username);
            return new String[]{username, password};
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Base64 encoding in Basic Auth header", e);
        }
    }
}
