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
package cn.structured.rpc.token;

import cn.structured.rpc.entity.TokenInfo;

/**
 * Token提供者接口
 * 用于从远程OAuth2服务器获取和刷新Token
 * 实现类负责处理与认证服务器的HTTP通信
 *
 * @author chuck
 */
public interface TokenProvider {

    /**
     * 获取Token（使用client_credentials模式）
     * 直接向认证服务器请求新的Token
     *
     * @return TokenInfo对象，包含access_token、refresh_token等信息
     */
    TokenInfo fetchToken();

    /**
     * 使用refresh_token刷新Token
     * 通过已有的refresh_token获取新的access_token
     * 如果刷新失败，应回退到fetchToken()方法
     *
     * @param refreshToken 已有的refresh token
     * @return 新的TokenInfo对象
     */
    TokenInfo refreshToken(String refreshToken);
}