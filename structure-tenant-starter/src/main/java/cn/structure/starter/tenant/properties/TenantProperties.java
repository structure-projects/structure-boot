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

package cn.structure.starter.tenant.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 多租户配置属性类
 * </p>
 *
 * @author chuck
 * @version 1.0.0
 * @since 2026-04-27
 */
@ConfigurationProperties(prefix = "structure.tenant")
public class TenantProperties {

    /**
     * 是否启用多租户功能
     */
    private boolean enabled = true;

    /**
     * 默认租户ID，当所有识别方式都失败时使用
     */
    private String defaultTenantId = "1";

    /**
     * HTTP请求头识别配置
     */
    private Header header = new Header();

    /**
     * 请求参数识别配置
     */
    private Param param = new Param();

    /**
     * 识别器顺序，值越小优先级越高
     */
    private List<String> resolverOrder = new ArrayList<>();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getDefaultTenantId() {
        return defaultTenantId;
    }

    public void setDefaultTenantId(String defaultTenantId) {
        this.defaultTenantId = defaultTenantId;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Param getParam() {
        return param;
    }

    public void setParam(Param param) {
        this.param = param;
    }

    public List<String> getResolverOrder() {
        return resolverOrder;
    }

    public void setResolverOrder(List<String> resolverOrder) {
        this.resolverOrder = resolverOrder;
    }

    public static class Header {
        /**
         * 是否启用HTTP请求头识别
         */
        private boolean enabled = true;

        /**
         * 请求头名称
         */
        private String name = "X-Tenant-Id";

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class Param {
        /**
         * 是否启用请求参数识别
         */
        private boolean enabled = true;

        /**
         * 参数名称
         */
        private String name = "tenantId";

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
