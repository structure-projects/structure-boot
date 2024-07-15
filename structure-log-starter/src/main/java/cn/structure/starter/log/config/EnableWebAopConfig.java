package cn.structure.starter.log.config;

import org.springframework.context.annotation.ImportResource;


/**
 * <p>
 * aop 配置
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2020/6/3 12:05
 */
@ImportResource(locations = {"classpath:structure-boot-aop.xml"})

public class EnableWebAopConfig {

}
