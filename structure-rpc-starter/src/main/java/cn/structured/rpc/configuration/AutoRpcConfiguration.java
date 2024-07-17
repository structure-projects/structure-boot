package cn.structured.rpc.configuration;

import cn.structured.rpc.properties.RpcProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

/**
 * rpc自动装配
 *
 * @author chuck
 * @version 2024/07/17 下午4:01
 * @since 1.8
 */
@Configuration
@ConditionalOnClass(value = {RpcProperties.class})
public class AutoRpcConfiguration {
}
