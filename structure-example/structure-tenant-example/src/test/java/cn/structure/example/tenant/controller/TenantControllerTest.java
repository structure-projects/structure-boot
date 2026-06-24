package cn.structure.example.tenant.controller;

import cn.structure.example.tenant.config.AbstractIntegrationTest;
import cn.structure.example.tenant.service.TenantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class TenantControllerTest extends AbstractIntegrationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        assertNotNull(applicationContext);
    }

    @Test
    void tenantControllerBeanExists() {
        TenantController tenantController = applicationContext.getBean(TenantController.class);
        assertNotNull(tenantController);
    }

    @Test
    void tenantServiceBeanExists() {
        TenantService tenantService = applicationContext.getBean(TenantService.class);
        assertNotNull(tenantService);
    }
}
