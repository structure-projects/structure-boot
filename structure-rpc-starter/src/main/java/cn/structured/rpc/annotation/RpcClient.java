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

import cn.structured.rpc.emuns.AuthType;

import java.lang.annotation.*;

/**
 * RPC客户端注解
 * 标记一个类或接口作为远程服务调用的客户端，类似FeignClient
 * 支持配置文件配置和注解配置两种方式
 * 
 * 需要配合@EnableRpcClients注解使用
 *
 * @author chuck
 * @version 2024/07/17 下午3:55
 * @since 1.8
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface RpcClient {

    /**
     * 服务名称，对应配置中的服务标识
     */
    String value() default "";

}
