package cn.structure.starter.minio.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * minio 配置信息
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/7/17 14:06
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "structure.minio")
public class MinioProperties {
    /**
     * minio 服务地址 http://ip:port
     */
    private String url;

    /**
     * 用户名
     */
    private String accessKey;

    /**
     * 密码
     */
    private String secretKey;

    /**
     * 过期时间(默认 60 * 60 * 24 * 7 = 604800 秒)
     */
    private int expiresSecond = 604800;

    /**
     * 是否启用 endpoint
     */
    private Boolean endpointEnable;
}
