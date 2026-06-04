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
package cn.structured.rpc.proxy;

import cn.structured.rpc.annotation.RpcClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Set;

/**
 * RPC接口扫描器
 * 扫描带有@RpcClient注解的接口并注册为Spring Bean
 * 对于接口类型，创建动态代理实例
 * 对于类类型，保持原有的处理方式
 *
 * @author chuck
 */
@Slf4j
public class RpcInterfaceScanner extends ClassPathBeanDefinitionScanner {

    public RpcInterfaceScanner(BeanDefinitionRegistry registry) {
        super(registry, false);
        registerFilters();
    }

    private void registerFilters() {
        addIncludeFilter(new AnnotationTypeFilter(RpcClient.class));
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
        
        if (beanDefinitions.isEmpty()) {
            log.warn("[RpcInterfaceScanner] 未找到带有@RpcClient注解的接口");
        } else {
            processBeanDefinitions(beanDefinitions);
        }
        
        return beanDefinitions;
    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        for (BeanDefinitionHolder holder : beanDefinitions) {
            AnnotatedBeanDefinition definition = (AnnotatedBeanDefinition) holder.getBeanDefinition();
            String beanClassName = definition.getMetadata().getClassName();
            String beanName = holder.getBeanName();
            
            if (definition.getMetadata().isInterface()) {
                log.info("[RpcInterfaceScanner] 发现RPC接口 - {}", beanClassName);
                
                GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
                beanDefinition.setBeanClass(RpcInterfaceFactoryBean.class);
                beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(beanClassName);
                
                getRegistry().removeBeanDefinition(beanName);
                
                getRegistry().registerBeanDefinition(beanName, beanDefinition);
                
                log.info("[RpcInterfaceScanner] 注册RPC代理Bean - name: {}, class: {}", beanName, beanClassName);
            } else {
                log.info("[RpcInterfaceScanner] 发现RPC类 - {}", beanClassName);
            }
        }
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isIndependent();
    }
}
