package cn.structure.starter.log.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ImportResource;

/**
 * <p>
 * WebAop配置入口
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/1/3 15:41
 */
@ImportResource(locations = {"classpath:structure-boot-aop.xml"})
@ConditionalOnProperty(value = "structure.log.aop.enable", matchIfMissing = true)
public class WebAopConfig {

}
