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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * HTTP请求头租户识别器
 * </p>
 *
 * @author chuck
 * @version 1.0.0
 * @since 2026-04-27
 */
public class HeaderTenantResolver implements TenantResolver {

    public static final String NAME = "header";

    private final TenantProperties.Header headerConfig;

    public HeaderTenantResolver(TenantProperties.Header headerConfig) {
        this.headerConfig = headerConfig;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String resolve() {
        if (!headerConfig.isEnabled()) {
            return null;
        }

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }

        HttpServletRequest request = attributes.getRequest();
        return request.getHeader(headerConfig.getName());
    }

    @Override
    public boolean isEnabled() {
        return headerConfig.isEnabled();
    }
}
