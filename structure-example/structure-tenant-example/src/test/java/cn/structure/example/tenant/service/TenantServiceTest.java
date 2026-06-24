package cn.structure.example.tenant.service;

import cn.structure.starter.tenant.TenantContextHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TenantServiceTest {

    private TenantService tenantService;

    @BeforeEach
    void setUp() {
        tenantService = new TenantService();
    }

    @AfterEach
    void tearDown() {
        TenantContextHolder.clear();
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
