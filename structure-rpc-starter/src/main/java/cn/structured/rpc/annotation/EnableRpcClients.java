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
package cn.structured.rpc.annotation;

import cn.structured.rpc.configuration.AutoRpcConfiguration;
import cn.structured.rpc.proxy.RpcInterfaceScanner;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用RPC客户端注解
 * 扫描带有@RpcClient注解的接口并创建动态代理实例
 *
 * @author chuck
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({AutoRpcConfiguration.class, EnableRpcClients.RpcClientsRegistrar.class})
public @interface EnableRpcClients {

    /**
     * 要扫描的包路径
     */
    String[] basePackages() default {};

    /**
     * RPC客户端注册器
     */
    class RpcClientsRegistrar implements org.springframework.context.annotation.ImportBeanDefinitionRegistrar {

        @Override
        public void registerBeanDefinitions(org.springframework.core.type.AnnotationMetadata importingClassMetadata, 
                org.springframework.beans.factory.support.BeanDefinitionRegistry registry) {
            
            org.springframework.core.annotation.AnnotationAttributes attributes = 
                    org.springframework.core.annotation.AnnotationAttributes.fromMap(
                            importingClassMetadata.getAnnotationAttributes(EnableRpcClients.class.getName()));
            
            String[] basePackages = attributes.getStringArray("basePackages");
            
            if (basePackages.length == 0) {
                basePackages = new String[]{importingClassMetadata.getClassName().substring(0, 
                        importingClassMetadata.getClassName().lastIndexOf('.'))};
            }
            
            RpcInterfaceScanner scanner = new RpcInterfaceScanner(registry);
            scanner.scan(basePackages);
        }
    }
}
