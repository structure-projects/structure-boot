package cn.structure.starter.log.config;

import cn.structure.starter.log.filter.WebLogAspect;
import cn.structure.starter.log.properties.WebAopConfigProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * <p>
 *     自动配置
 * </p>
 * @author chuck
 * @since 2020/6/3 12:05
 * @version 1.0.1
 */
@Configuration
@ConditionalOnClass(value = {WebAopConfigProperties.class})
@Import(WebAopConfig.class)
public class AutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(WebLogAspect.class)
    public WebLogAspect webLogAspect(){
        return new WebLogAspect();
    }

}
