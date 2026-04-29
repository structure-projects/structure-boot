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

package cn.structure.example.tenant.resolver;

import cn.structure.starter.tenant.resolver.TenantResolver;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 自定义租户识别器示例 - 这里演示如何通过Session获取租户信息
 * </p>
 *
 * @author chuck
 * @version 1.0.0
 * @since 2026-04-27
 */
@Component
public class CustomTenantResolver implements TenantResolver {

    public static final String NAME = "custom";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String resolve() {
        // 自定义识别逻辑，这里演示可以通过多种方式获取租户
        // 例如：从Session中获取、从Token中解析、从数据库配置中获取等
        return null; // 如果无法识别返回null，会继续使用下一个识别器
    }

    @Override
    public boolean isEnabled() {
        return false; // 默认禁用，你可以根据需要启用
    }
}
