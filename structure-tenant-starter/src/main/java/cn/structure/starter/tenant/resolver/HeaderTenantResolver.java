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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

/**
 * <p>
 * HTTP请求头租户识别器
 * </p>
 *
 * @author chuck
 * @version 1.0.0
 * @since 2026-04-27
 */
@Slf4j
public class HeaderTenantResolver implements TenantResolver {

    public static final String NAME = "header";

    private final TenantProperties.Header headerConfig;

    public HeaderTenantResolver(TenantProperties.Header headerConfig) {
        this.headerConfig = headerConfig;
        log.debug("[HeaderTenantResolver] 初始化 - headerName: {}, enabled: {}", headerConfig.getName(), headerConfig.isEnabled());
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String resolve() {
        if (!headerConfig.isEnabled()) {
            log.debug("[HeaderTenantResolver] 识别器未启用");
            return null;
        }

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            log.debug("[HeaderTenantResolver] 无法获取请求上下文");
            return null;
        }

        HttpServletRequest request = attributes.getRequest();
        String tenantId = request.getHeader(headerConfig.getName());
        log.debug("[HeaderTenantResolver] 从请求头获取租户ID - headerName: {}, tenantId: {}", headerConfig.getName(), tenantId);
        return tenantId;
    }

    @Override
    public boolean isEnabled() {
        return headerConfig.isEnabled();
    }
}
