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
import cn.structured.rpc.emuns.AuthType;
import cn.structured.rpc.entity.RemoteService;
import cn.structured.rpc.handler.BaseHttpClient;
import cn.structured.rpc.properties.RpcProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Proxy;

/**
     * RPC接口工厂Bean
     * 创建动态代理实例，实现类似FeignClient的功能
     * 使用现有的RpcClient注解和RemoteService配置
     * 
     * 配置优先级（由低到高）：
     * 1. 配置文件中的配置（RpcProperties.serviceList）- 提供默认值
     * 2. 注解中的配置（@RpcClient）- 可以覆盖配置文件中的配置
     * 
     * 注解中的配置只有在非默认值时才会覆盖配置文件
     * 
     * @author chuck
     */
@Slf4j
public class RpcInterfaceFactoryBean<T> implements FactoryBean<T> {

    private final Class<T> interfaceType;
    private final RpcClient rpcClient;
    
    @Autowired(required = false)
    private RpcProperties rpcProperties;

    public RpcInterfaceFactoryBean(Class<T> interfaceType) {
        this.interfaceType = interfaceType;
        this.rpcClient = interfaceType.getAnnotation(RpcClient.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getObject() throws Exception {
        String serviceName = rpcClient.value();
        log.info("[RpcInterfaceFactoryBean] 创建RPC代理对象 - interface: {}, service: {}", 
                interfaceType.getName(), serviceName);

        RemoteService remoteService = buildRemoteService(serviceName);
        BaseHttpClient httpClient = new BaseHttpClient();
        httpClient.init(remoteService.getHost(), remoteService.getPort(), remoteService);
        
        String baseUrl = buildBaseUrl(remoteService);
        
        RpcProxyHandler handler = new RpcProxyHandler(httpClient, baseUrl);
        
        return (T) Proxy.newProxyInstance(
                interfaceType.getClassLoader(),
                new Class<?>[]{interfaceType},
                handler
        );
    }

    private RemoteService buildRemoteService(String serviceName) {
        RemoteService remoteService = new RemoteService();
        
        RemoteService configService = null;
        if (rpcProperties != null && rpcProperties.getServiceList() != null) {
            configService = rpcProperties.getServiceList().get(serviceName);
        }
        
        if (configService != null) {
            log.info("[RpcInterfaceFactoryBean] 使用配置文件中的配置作为基础 - service: {}", serviceName);
            remoteService = configService;
        }
        
        log.info("[RpcInterfaceFactoryBean] 使用注解中的配置覆盖 - service: {}", serviceName);

        return remoteService;
    }


    private String buildBaseUrl(RemoteService remoteService) {
        StringBuilder url = new StringBuilder();
        url.append("http://").append(remoteService.getHost());
        if (remoteService.getPort() != null && remoteService.getPort() > 0) {
            url.append(":").append(remoteService.getPort());
        }
        return url.toString();
    }

    @Override
    public Class<?> getObjectType() {
        return interfaceType;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
