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
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class TenantResolverChain {

    private final List<TenantResolver> resolvers = new ArrayList<>();
    private final TenantProperties properties;

    public TenantResolverChain(TenantProperties properties) {
        this.properties = properties;
        log.debug("[TenantResolverChain] 初始化租户识别器链");
    }

    /**
     * 添加识别器
     *
     * @param resolver 识别器
     */
    public void addResolver(TenantResolver resolver) {
        log.debug("[TenantResolverChain] 添加租户识别器 - name: {}", resolver.getName());
        resolvers.add(resolver);
    }

    /**
     * 按配置顺序排序识别器
     */
    public void sortResolvers() {
        List<String> order = properties.getResolverOrder();
        if (CollectionUtils.isEmpty(order)) {
            log.debug("[TenantResolverChain] 未配置识别器顺序，跳过排序");
            return;
        }

        log.debug("[TenantResolverChain] 按配置排序识别器 - order: {}", order);
        resolvers.sort(Comparator.comparingInt(r -> {
            int index = order.indexOf(r.getName());
            return index >= 0 ? index : Integer.MAX_VALUE;
        }));
        log.debug("[TenantResolverChain] 识别器排序完成");
    }

    /**
     * 从链中识别租户ID
     *
     * @return 租户ID
     */
    public String resolveTenantId() {
        log.debug("[TenantResolverChain] 开始从识别器链获取租户ID - resolverCount: {}", resolvers.size());
        for (TenantResolver resolver : resolvers) {
            if (!resolver.isEnabled()) {
                log.debug("[TenantResolverChain] 识别器未启用，跳过 - name: {}", resolver.getName());
                continue;
            }

            log.debug("[TenantResolverChain] 使用识别器尝试获取租户ID - name: {}", resolver.getName());
            String tenantId = resolver.resolve();
            if (tenantId != null && !tenantId.isEmpty()) {
                log.info("[TenantResolverChain] 从识别器获取租户ID成功 - resolver: {}, tenantId: {}", resolver.getName(), tenantId);
                return tenantId;
            }
        }

        String defaultTenantId = properties.getDefaultTenantId();
        log.debug("[TenantResolverChain] 所有识别器未获取到租户ID，使用默认租户ID - defaultTenantId: {}", defaultTenantId);
        return defaultTenantId;
    }

    /**
     * 清理所有识别器
     */
    public void cleanup() {
        log.debug("[TenantResolverChain] 清理所有识别器");
        for (TenantResolver resolver : resolvers) {
            resolver.cleanup();
        }
    }
}
