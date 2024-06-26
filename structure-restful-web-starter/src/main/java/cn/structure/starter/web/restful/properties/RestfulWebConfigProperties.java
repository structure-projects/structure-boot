package cn.structure.starter.web.restful.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * restful 风格的web配置
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/1/3 20:48
 */
@Getter
@Setter
@ToString
@Configuration
@ConfigurationProperties(prefix = "structure.web.restful")
public class RestfulWebConfigProperties {

}
