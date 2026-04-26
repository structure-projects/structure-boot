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

package cn.structure.example.tenant.service;

import cn.structure.starter.tenant.TenantContextHolder;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 多租户服务示例
 * </p>
 *
 * @author chuck
 * @version 1.0.0
 * @since 2026-04-27
 */
@Service
public class TenantService {

    /**
     * 处理业务逻辑的通用方法
     *
     * @return 处理结果
     */
    public String processBusiness() {
        String tenantId = TenantContextHolder.getTenantId();
        return String.format("业务处理完成，当前租户ID: %s", tenantId);
    }

    /**
     * 模拟XXL-Job任务处理
     *
     * @param tenantId 租户ID
     * @return 处理结果
     */
    public String handleXxlJob(String tenantId) {
        try {
            TenantContextHolder.setTenantId(tenantId);
            return processBusiness();
        } finally {
            TenantContextHolder.clear();
        }
    }

    /**
     * 模拟消息队列处理
     *
     * @param tenantId 租户ID
     * @return 处理结果
     */
    public String handleMessageQueue(String tenantId) {
        try {
            TenantContextHolder.setTenantId(tenantId);
            return processBusiness();
        } finally {
            TenantContextHolder.clear();
        }
    }
}
