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

package cn.structure.example.tenant.controller;

import cn.structure.example.tenant.service.TenantService;
import cn.structure.starter.tenant.TenantContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 多租户控制器示例
 * </p>
 *
 * @author chuck
 * @version 1.0.0
 * @since 2026-04-27
 */
@RestController
public class TenantController {

    private final TenantService tenantService;

    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    /**
     * 获取当前租户信息 - HTTP场景
     * 可以通过请求头 X-Tenant-Id 或请求参数 tenantId 传递
     *
     * @return 租户信息
     */
    @GetMapping("/tenant/info")
    public String getTenantInfo() {
        String tenantId = TenantContextHolder.getTenantId();
        return "HTTP请求 - 当前租户ID: " + tenantId;
    }

    /**
     * 测试XXL-Job场景
     *
     * @param tenantId 租户ID
     * @return 处理结果
     */
    @GetMapping("/tenant/test-xxljob")
    public String testXxlJob(@RequestParam String tenantId) {
        return tenantService.handleXxlJob(tenantId);
    }

    /**
     * 测试消息队列场景
     *
     * @param tenantId 租户ID
     * @return 处理结果
     */
    @GetMapping("/tenant/test-messagequeue")
    public String testMessageQueue(@RequestParam String tenantId) {
        return tenantService.handleMessageQueue(tenantId);
    }
}
