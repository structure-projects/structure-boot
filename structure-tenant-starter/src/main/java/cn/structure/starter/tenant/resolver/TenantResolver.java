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

package cn.structure.starter.tenant.resolver;

/**
 * <p>
 * 租户识别器接口
 * </p>
 *
 * @author chuck
 * @version 1.0.0
 * @since 2026-04-27
 */
public interface TenantResolver {

    /**
     * 获取识别器名称，用于排序
     *
     * @return 识别器名称
     */
    String getName();

    /**
     * 识别租户ID
     *
     * @return 租户ID，无法识别则返回null
     */
    String resolve();

    /**
     * 是否启用
     *
     * @return true表示启用
     */
    default boolean isEnabled() {
        return true;
    }

    /**
     * 清理资源
     */
    default void cleanup() {
    }
}
