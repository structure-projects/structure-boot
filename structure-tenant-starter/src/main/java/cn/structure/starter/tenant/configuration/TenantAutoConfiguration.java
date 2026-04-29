/*
 * Copyright (c) 2025 Structure Boot
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.structure.starter.tenant.configuration;

import cn.structure.starter.tenant.TenantContextHolder;
import cn.structure.starter.tenant.properties.TenantProperties;
import cn.structure.starter.tenant.resolver.HeaderTenantResolver;
import cn.structure.starter.tenant.resolver.ParamTenantResolver;
import cn.structure.starter.tenant.resolver.TenantResolver;
import cn.structure.starter.tenant.resolver.TenantResolverChain;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * <p>
 * 多租户自动配置类
 * </p>
 *
 * @author chuck
 * @version 1.0.0
 * @since 2026-04-27
 */
@Configuration
@EnableConfigurationProperties(TenantProperties.class)
@ConditionalOnProperty(prefix = "structure.tenant", name = "enabled", havingValue = "true", matchIfMissing = true)
public class TenantAutoConfiguration {

    /**
     * 创建HTTP请求头租户识别器
     *
     * @param properties 配置属性
     * @return HTTP请求头识别器
     */
    @Bean
    @ConditionalOnWebApplication
    @ConditionalOnMissingBean(name = "headerTenantResolver")
    public TenantResolver headerTenantResolver(TenantProperties properties) {
        return new HeaderTenantResolver(properties.getHeader());
    }

    /**
     * 创建请求参数租户识别器
     *
     * @param properties 配置属性
     * @return 请求参数识别器
     */
    @Bean
    @ConditionalOnWebApplication
    @ConditionalOnMissingBean(name = "paramTenantResolver")
    public TenantResolver paramTenantResolver(TenantProperties properties) {
        return new ParamTenantResolver(properties.getParam());
    }

    /**
     * 创建租户识别器链
     *
     * @param properties 配置属性
     * @param resolvers  所有可用的识别器
     * @return 识别器链
     */
    @Bean
    @ConditionalOnMissingBean
    public TenantResolverChain tenantResolverChain(TenantProperties properties, List<TenantResolver> resolvers) {
        TenantResolverChain chain = new TenantResolverChain(properties);
        for (TenantResolver resolver : resolvers) {
            chain.addResolver(resolver);
        }
        chain.sortResolvers();
        return chain;
    }

    /**
     * 初始化租户上下文
     *
     * @param chain 识别器链
     */
    @Bean
    @ConditionalOnBean(TenantResolverChain.class)
    public Object tenantContextInitializer(TenantResolverChain chain) {
        TenantContextHolder.setResolverChain(chain);
        return new Object();
    }
}
