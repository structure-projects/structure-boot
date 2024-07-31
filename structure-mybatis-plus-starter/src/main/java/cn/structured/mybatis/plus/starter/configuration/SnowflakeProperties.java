package cn.structured.mybatis.plus.starter.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 雪花算法配置
 *
 * @author cqliut
 * @version 2022.1025
 * @since 1.0.1
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "structure.snowflake")
public class SnowflakeProperties {

    private Long workerId;

    private Long datacenterId;
}
