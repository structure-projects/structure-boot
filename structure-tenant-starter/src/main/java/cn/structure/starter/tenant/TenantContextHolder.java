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

package cn.structure.starter.tenant;

import cn.structure.starter.tenant.resolver.TenantResolverChain;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 租户上下文持有类
 * </p>
 *
 * @author chuck
 * @version 1.0.0
 * @since 2026-04-27
 */
@Slf4j
public class TenantContextHolder {

    /**
     * 线程本地变量，存储租户信息
     */
    private static final ThreadLocal<Map<String, Object>> TENANT_CONTEXT = new ThreadLocal<Map<String, Object>>() {
        @Override
        protected Map<String, Object> initialValue() {
            return new HashMap<>();
        }
    };

    /**
     * 租户识别器链
     */
    private static TenantResolverChain resolverChain;

    /**
     * 设置租户识别器链
     *
     * @param chain 租户识别器链
     */
    public static void setResolverChain(TenantResolverChain chain) {
        log.debug("[TenantContextHolder] 设置租户识别器链");
        resolverChain = chain;
    }

    /**
     * 获取租户识别器链
     *
     * @return 租户识别器链
     */
    public static TenantResolverChain getResolverChain() {
        return resolverChain;
    }

    /**
     * 设置租户ID
     *
     * @param tenantId 租户ID
     */
    public static void setTenantId(String tenantId) {
        log.debug("[TenantContextHolder] 设置租户ID - tenantId: {}", tenantId);
        TENANT_CONTEXT.get().put("tenantId", tenantId);
    }

    /**
     * 获取租户ID
     *
     * @return 租户ID
     */
    public static String getTenantId() {
        String tenantId = (String) TENANT_CONTEXT.get().get("tenantId");
        log.debug("[TenantContextHolder] 获取租户ID - fromContext: {}", tenantId);
        if (tenantId == null && resolverChain != null) {
            log.debug("[TenantContextHolder] 从识别器链获取租户ID");
            tenantId = resolverChain.resolveTenantId();
            if (tenantId != null) {
                log.info("[TenantContextHolder] 从识别器链获取租户ID成功 - tenantId: {}", tenantId);
                setTenantId(tenantId);
            } else {
                log.debug("[TenantContextHolder] 从识别器链未获取到租户ID");
            }
        }
        return tenantId;
    }

    /**
     * 设置租户信息
     *
     * @param key   键
     * @param value 值
     */
    public static void set(String key, Object value) {
        log.debug("[TenantContextHolder] 设置租户信息 - key: {}, value: {}", key, value);
        TENANT_CONTEXT.get().put(key, value);
    }

    /**
     * 获取租户信息
     *
     * @param key 键
     * @return 值
     */
    public static Object get(String key) {
        Object value = TENANT_CONTEXT.get().get(key);
        log.debug("[TenantContextHolder] 获取租户信息 - key: {}, value: {}", key, value);
        return value;
    }

    /**
     * 清理租户上下文
     */
    public static void clear() {
        log.debug("[TenantContextHolder] 清理租户上下文");
        if (resolverChain != null) {
            resolverChain.cleanup();
        }
        TENANT_CONTEXT.remove();
    }

    /**
     * 获取所有租户信息
     *
     * @return 租户信息
     */
    public static Map<String, Object> getAll() {
        Map<String, Object> all = TENANT_CONTEXT.get();
        log.debug("[TenantContextHolder] 获取所有租户信息 - size: {}", all.size());
        return all;
    }

    /**
     * 检查是否存在租户信息
     *
     * @return 是否存在租户信息
     */
    public static boolean hasTenant() {
        boolean hasTenant = getTenantId() != null;
        log.debug("[TenantContextHolder] 检查是否存在租户信息 - hasTenant: {}", hasTenant);
        return hasTenant;
    }
}
