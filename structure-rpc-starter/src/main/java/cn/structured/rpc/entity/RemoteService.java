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
package cn.structured.rpc.entity;

import cn.structured.rpc.emuns.AuthType;
import lombok.Data;
import org.springframework.util.MultiValueMap;

/**
 * 远程服务定义
 *
 * @author chuck
 * @version 2024/07/17 下午5:01
 * @since 1.8
 */
@Data
public class RemoteService {

    /**
     * 远程服务应用ID
     */
    private String accessKey;

    /**
     * 远程服务密钥
     */
    private String secretKey;

    /**
     * 认证方式 Basic Auth or Bearer Auth
     */
    private AuthType authType = AuthType.NONE;

    /**
     * host
     */
    private String host;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 请求头
     */
    private MultiValueMap<String, String> headers;

    /**
     * 认证服务地址（用于Bearer认证获取token，若未设置则使用host）
     */
    private String authHost;

    /**
     * 认证服务端口（用于Bearer认证获取token，若未设置则使用port）
     */
    private Integer authPort;

    /**
     * Token获取路径（默认 /oauth/token）
     */
    private String authPath = "/oauth/token";

    /**
     * 获取Token时是否使用Basic Auth（默认false，使用参数传递）
     */
    private Boolean tokenBasicAuth = false;

    public String getAuthHost() {
        return authHost != null && !authHost.isEmpty() ? authHost : host;
    }

    public Integer getAuthPort() {
        return authPort != null ? authPort : port;
    }

}
