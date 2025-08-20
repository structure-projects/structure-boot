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

}
