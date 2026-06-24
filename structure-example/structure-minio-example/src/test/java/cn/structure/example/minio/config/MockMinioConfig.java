package cn.structure.example.minio.config;

import cn.structure.starter.minio.service.MinioTemplate;
import io.minio.MinioClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class MockMinioConfig {

    @Bean
    @Primary
    public MinioTemplate minioTemplate() {
        return mock(MinioTemplate.class);
    }

    @Bean
    @Primary
    public MinioClient minioClient() {
        return mock(MinioClient.class);
    }
}
