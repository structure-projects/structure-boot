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

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 请求参数租户识别器
 * </p>
 *
 * @author chuck
 * @version 1.0.0
 * @since 2026-04-27
 */
@Slf4j
public class ParamTenantResolver implements TenantResolver {

    public static final String NAME = "param";

    private final TenantProperties.Param paramConfig;

    public ParamTenantResolver(TenantProperties.Param paramConfig) {
        this.paramConfig = paramConfig;
        log.debug("[ParamTenantResolver] 初始化 - paramName: {}, enabled: {}", paramConfig.getName(), paramConfig.isEnabled());
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String resolve() {
        if (!paramConfig.isEnabled()) {
            log.debug("[ParamTenantResolver] 识别器未启用");
            return null;
        }

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            log.debug("[ParamTenantResolver] 无法获取请求上下文");
            return null;
        }

        HttpServletRequest request = attributes.getRequest();
        String tenantId = request.getParameter(paramConfig.getName());
        log.debug("[ParamTenantResolver] 从请求参数获取租户ID - paramName: {}, tenantId: {}", paramConfig.getName(), tenantId);
        return tenantId;
    }

    @Override
    public boolean isEnabled() {
        return paramConfig.isEnabled();
    }
}
