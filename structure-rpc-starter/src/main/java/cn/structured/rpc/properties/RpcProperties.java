package cn.structured.rpc.properties;

import cn.structured.rpc.entity.RemoteService;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * rpc 服务地址配置
 *
 * @author chuck
 * @version 2024/07/17 下午5:37
 * @since 1.8
 */
@Getter
@Setter
@ToString
@Configuration
@ConfigurationProperties(prefix = "structure.rpc")
public class RpcProperties {

    /**
     * 远程服务地址
     */
    private Map<String, RemoteService> serviceList;
}
