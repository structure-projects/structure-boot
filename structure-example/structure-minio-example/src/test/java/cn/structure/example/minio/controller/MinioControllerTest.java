package cn.structure.example.minio.controller;

import cn.structure.example.minio.config.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class MinioControllerTest extends AbstractIntegrationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        assertNotNull(applicationContext);
    }

    @Test
    void minioControllerBeanExists() {
        MinioController minioController = applicationContext.getBean(MinioController.class);
        assertNotNull(minioController);
    }
}
