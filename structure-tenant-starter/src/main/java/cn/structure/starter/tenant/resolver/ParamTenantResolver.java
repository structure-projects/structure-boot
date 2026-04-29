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

import jakarta.servlet.http.HttpServletRequest;

/**
 * <p>
 * 请求参数租户识别器
 * </p>
 *
 * @author chuck
 * @version 1.0.0
 * @since 2026-04-27
 */
public class ParamTenantResolver implements TenantResolver {

    public static final String NAME = "param";

    private final TenantProperties.Param paramConfig;

    public ParamTenantResolver(TenantProperties.Param paramConfig) {
        this.paramConfig = paramConfig;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String resolve() {
        if (!paramConfig.isEnabled()) {
            return null;
        }

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }

        HttpServletRequest request = attributes.getRequest();
        return request.getParameter(paramConfig.getName());
    }

    @Override
    public boolean isEnabled() {
        return paramConfig.isEnabled();
    }
}
