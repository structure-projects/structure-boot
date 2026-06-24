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
package cn.structure.starter.web.restful.filter;

import cn.structure.starter.web.restful.annotation.EnableFastJsonHttpConverters;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * <p>
 * FastJson消息转换器注册器
 * 用于将 EnableFastJsonHttpConverters 注解属性注册为 Bean
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2025-06-25
 */
public class FastJsonHttpMessageConvertersRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<String, Object> attributes = importingClassMetadata.getAnnotationAttributes(EnableFastJsonHttpConverters.class.getName());
        if (attributes != null) {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(FastJsonHttpMessageConvertersProperties.class);
            builder.addPropertyValue("longToString", attributes.get("longToString"));
            builder.addPropertyValue("nullShowValue", attributes.get("nullShowValue"));
            registry.registerBeanDefinition("fastJsonHttpMessageConvertersProperties", builder.getBeanDefinition());
        }
    }
}