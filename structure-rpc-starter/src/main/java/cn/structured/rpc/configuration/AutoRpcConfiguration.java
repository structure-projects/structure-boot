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
package cn.structured.rpc.configuration;

import cn.hutool.core.util.StrUtil;
import cn.structured.rpc.annotation.RpcClient;
import cn.structured.rpc.entity.RemoteService;
import cn.structured.rpc.handler.IRpcHandler;
import cn.structured.rpc.properties.RpcProperties;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.util.ClassUtils;

import java.util.Map;
import java.util.Optional;

/**
 * rpc自动装配
 *
 * @author chuck
 * @version 2024/07/17 下午4:01
 * @since 1.8
 */
@Configuration
@ImportAutoConfiguration(RpcProperties.class)
@ConditionalOnClass(value = {RpcProperties.class})
public class AutoRpcConfiguration implements ApplicationListener<ApplicationStartedEvent>, ResourceLoaderAware {

    @javax.annotation.Resource
    private RpcProperties rpcProperties;

    private MetadataReaderFactory metadataReaderFactory;

    @SneakyThrows
    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        Map<String, RemoteService> serviceList = rpcProperties.getServiceList();
        //获取启动类注解
        Map<String, Object> beansWithAnnotation = event.getApplicationContext().getBeansWithAnnotation(SpringBootApplication.class);
        Optional<Object> any = beansWithAnnotation.values().stream().findAny();
        String packageName = "";
        if (any.isPresent()) {
            Object declaringClass = any.get();
            packageName = ClassUtils.getPackageName(declaringClass.getClass());
        }
        if (!StrUtil.isBlank(packageName)) {
            Resource[] classResources = event.getApplicationContext().getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                    + ClassUtils.convertClassNameToResourcePath(packageName) + "/**/*.class");
            for (Resource classResource : classResources) {
                MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(classResource);
                String className = metadataReader.getClassMetadata().getClassName();
                Class<?> clazz = ClassUtils.forName(className, event.getApplicationContext().getClassLoader());
                RpcClient rpcClient = clazz.getAnnotation(RpcClient.class);
                if (null != rpcClient) {
                    String host = rpcClient.host();
                    String value = rpcClient.value();
                    int port = rpcClient.port();
                    RemoteService remoteService = serviceList.get(value);
                    if (null != remoteService) {
                        host = (remoteService.getHost().isEmpty()) ? host : remoteService.getHost();
                        port = (null == remoteService.getPort()) ? port : remoteService.getPort();
                    }
                    ConfigurableApplicationContext applicationContext = event.getApplicationContext();
                    IRpcHandler handler = (IRpcHandler) applicationContext.getBean(clazz, IRpcHandler.class);
                    handler.init(host, port);
                }
            }
        }
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.metadataReaderFactory = new SimpleMetadataReaderFactory(resourceLoader);
    }
}
