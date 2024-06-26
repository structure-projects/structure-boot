package cn.structure.starter.log.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 *     aop 配置
 * </p>
 * @author chuck
 * @since 2020/6/3 12:05
 * @version 1.0.1
 */
@Getter
@Setter
@ToString
@Configuration
@ConfigurationProperties("structure.log.aop")
public class WebAopConfigProperties {

    /**
     * 公共配置
     */
    private String expression;

    /**
     * web Aop 开关
     */
    private Boolean enable = true;

}
