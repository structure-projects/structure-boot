package cn.structured.ribbon.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;
import java.util.List;

/**
 * 服务列表
 *
 * @author cqliut
 * @version 2022.1227
 * @since 1.0.1
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "structure.ribbon")
public class ServerProperties {

    /**
     * 是否启用自定义
     */
    private Boolean enable;
    /**
     * 服务列表
     */
    private List<StaticServer> server;

    @Data
    public static class StaticServer implements Serializable {

        private String host;

        private int port;

        private String scheme;

        private String zone;

        private String id;

        private String appName;

        private String serverGroup;

        private String serviceIdForDiscovery;

        private String instanceId;
    }
}

