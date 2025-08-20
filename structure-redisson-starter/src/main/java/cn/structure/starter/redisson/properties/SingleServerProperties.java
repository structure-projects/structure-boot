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
package cn.structure.starter.redisson.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 单节点配置属性
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2020-12-23
 */
@Setter
@Getter
@ToString
public class SingleServerProperties {
    /**
     * 单节点redis的地址
     */
    private String address;
    /**
     * 订阅连接的最小空闲连接数
     */
    private Integer subscriptionConnectionMinimumIdleSize = 1;
    /**
     * 订阅连接池大小
     */
    private Integer subscriptionConnectionPoolSize = 50;
    /**
     * 最小空闲连接数
     */
    private Integer connectionMinimumIdleSize = 32;
    /**
     * 连接池大小
     */
    private Integer connectionPoolSize = 64;
    /**
     * DNS监控间隔，单位：毫秒
     */
    private Long dnsMonitoringInterval = 5000L;
}
