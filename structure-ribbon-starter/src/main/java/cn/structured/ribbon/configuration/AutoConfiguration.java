package cn.structured.ribbon.configuration;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.cloud.netflix.ribbon.RibbonClients;

/**
 * 自动装配
 *
 * @author cqliut
 * @version 2022.1227
 * @since 1.0.1
 */
@RibbonClients(defaultConfiguration = StructureRibbonClientConfiguration.class)
@ConditionalOnClass(value = {ServerProperties.class})
@EnableConfigurationProperties({ServerProperties.class})
@AutoConfigureAfter(value = {RibbonAutoConfiguration.class})
@ConditionalOnProperty(value = "structure.ribbon.enable", havingValue = "true")
public class AutoConfiguration {


}
