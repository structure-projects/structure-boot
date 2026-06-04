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
package cn.structured.rpc.emuns;

/**
 * 认证类型枚举
 * 定义RPC客户端支持的认证方式
 *
 * @author chuck
 */
public enum AuthType {

    /**
     * 无认证模式
     * 不添加任何认证头
     */
    NONE,

    /**
     * Basic认证模式
     * 使用Base64编码的用户名密码作为Authorization头
     * 格式: Basic base64(username:password)
     */
    BASIC,

    /**
     * Bearer认证模式
     * 使用OAuth2的Access Token作为Authorization头
     * 格式: Bearer <access_token>
     */
    BEARER
}
