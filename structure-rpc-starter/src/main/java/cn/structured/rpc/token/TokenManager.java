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
 * Token管理器接口
 * 负责Token的获取、刷新、失效和有效性检查
 * 实现类需保证线程安全，支持并发访问
 *
 * @author chuck
 */
public interface TokenManager {

    /**
     * 获取有效的Token
     * 如果当前Token不存在或即将过期，会自动刷新
     *
     * @return 有效的TokenInfo对象，如果无法获取则返回null
     */
    TokenInfo getToken();

    /**
     * 强制刷新Token
     * 无论当前Token是否有效，都会从服务端获取新的Token
     *
     * @return 新的TokenInfo对象
     */
    TokenInfo refreshToken();

    /**
     * 手动失效当前Token
     * 调用此方法后，下次getToken()会重新获取Token
     */
    void invalidateToken();

    /**
     * 检查当前Token是否有效
     * 有效指Token存在且未过期
     *
     * @return true表示Token有效，false表示无效或不存在
     */
    boolean isValid();
}