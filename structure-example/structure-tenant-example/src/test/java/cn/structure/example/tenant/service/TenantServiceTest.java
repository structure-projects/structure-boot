package cn.structure.example.tenant.service;

import cn.structure.starter.tenant.TenantContextHolder;
import cn.structure.starter.tenant.resolver.TenantResolverChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TenantServiceTest {

    private TenantService tenantService;
    private TenantResolverChain originalResolverChain;

    @BeforeEach
    void setUp() {
        // 保存原始的 resolverChain 并清除，确保单元测试不依赖 Spring 容器
        originalResolverChain = TenantContextHolder.getResolverChain();
        TenantContextHolder.setResolverChain(null);
        TenantContextHolder.clear();
        tenantService = new TenantService();
    }

    @AfterEach
    void tearDown() {
        TenantContextHolder.clear();
        // 恢复原始的 resolverChain
        TenantContextHolder.setResolverChain(originalResolverChain);
    }

    @Test
    void processBusiness_withTenantSet_shouldReturnTenantId() {
        String tenantId = "test-tenant-123";
        TenantContextHolder.setTenantId(tenantId);

        String result = tenantService.processBusiness();

        assertNotNull(result);
        assertTrue(result.contains("业务处理完成"));
        assertTrue(result.contains(tenantId));
    }

    @Test
    void processBusiness_withoutTenantSet_shouldReturnNullTenant() {
        String result = tenantService.processBusiness();

        assertNotNull(result);
        assertTrue(result.contains("业务处理完成"));
        assertTrue(result.contains("null"));
    }

    @Test
    void handleXxlJob_shouldSetTenantAndClearAfter() {
        String tenantId = "xxljob-tenant-456";

        String result = tenantService.handleXxlJob(tenantId);

        assertNotNull(result);
        assertTrue(result.contains("业务处理完成"));
        assertTrue(result.contains(tenantId));
        assertNull(TenantContextHolder.getTenantId());
    }

    @Test
    void handleXxlJob_withDifferentTenant_shouldIsolateContext() {
        String tenantId1 = "tenant-A";
        String tenantId2 = "tenant-B";

        String result1 = tenantService.handleXxlJob(tenantId1);
        String result2 = tenantService.handleXxlJob(tenantId2);

        assertTrue(result1.contains(tenantId1));
        assertTrue(result2.contains(tenantId2));
        assertNull(TenantContextHolder.getTenantId());
    }

    @Test
    void handleMessageQueue_shouldSetTenantAndClearAfter() {
        String tenantId = "mq-tenant-789";

        String result = tenantService.handleMessageQueue(tenantId);

        assertNotNull(result);
        assertTrue(result.contains("业务处理完成"));
        assertTrue(result.contains(tenantId));
        assertNull(TenantContextHolder.getTenantId());
    }

    @Test
    void handleMessageQueue_withDifferentTenant_shouldIsolateContext() {
        String tenantId1 = "mq-tenant-C";
        String tenantId2 = "mq-tenant-D";

        String result1 = tenantService.handleMessageQueue(tenantId1);
        String result2 = tenantService.handleMessageQueue(tenantId2);

        assertTrue(result1.contains(tenantId1));
        assertTrue(result2.contains(tenantId2));
        assertNull(TenantContextHolder.getTenantId());
    }
}
