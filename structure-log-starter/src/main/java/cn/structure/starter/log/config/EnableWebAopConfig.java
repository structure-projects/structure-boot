package cn.structure.starter.log.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ImportResource;


/**
 * <p>
 *     aop 配置
 * </p>
 * @author chuck
 * @since 2020/6/3 12:05
 * @version 1.0.1
 */
@ImportResource(locations = {"classpath:structure-boot-aop.xml"})

public class EnableWebAopConfig {

}
