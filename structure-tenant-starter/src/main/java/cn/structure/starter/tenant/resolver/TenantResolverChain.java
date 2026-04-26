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

import cn.structure.starter.tenant.properties.TenantProperties;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * <p>
 * 租户识别器链管理器
 * </p>
 *
 * @author chuck
 * @version 1.0.0
 * @since 2026-04-27
 */
public class TenantResolverChain {

    private final List<TenantResolver> resolvers = new ArrayList<>();
    private final TenantProperties properties;

    public TenantResolverChain(TenantProperties properties) {
        this.properties = properties;
    }

    /**
     * 添加识别器
     *
     * @param resolver 识别器
     */
    public void addResolver(TenantResolver resolver) {
        resolvers.add(resolver);
    }

    /**
     * 按配置顺序排序识别器
     */
    public void sortResolvers() {
        List<String> order = properties.getResolverOrder();
        if (CollectionUtils.isEmpty(order)) {
            return;
        }

        resolvers.sort(Comparator.comparingInt(r -> {
            int index = order.indexOf(r.getName());
            return index >= 0 ? index : Integer.MAX_VALUE;
        }));
    }

    /**
     * 从链中识别租户ID
     *
     * @return 租户ID
     */
    public String resolveTenantId() {
        for (TenantResolver resolver : resolvers) {
            if (!resolver.isEnabled()) {
                continue;
            }

            String tenantId = resolver.resolve();
            if (tenantId != null && !tenantId.isEmpty()) {
                return tenantId;
            }
        }

        return properties.getDefaultTenantId();
    }

    /**
     * 清理所有识别器
     */
    public void cleanup() {
        for (TenantResolver resolver : resolvers) {
            resolver.cleanup();
        }
    }
}
