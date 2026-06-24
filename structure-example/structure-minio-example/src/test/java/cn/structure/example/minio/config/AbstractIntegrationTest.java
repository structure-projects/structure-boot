package cn.structure.example.minio.config;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.autoconfigure.exclude=cn.structure.starter.minio.configuration.MinioAutoConfiguration"
        })
@ActiveProfiles("test")
@Import(MockMinioConfig.class)
public abstract class AbstractIntegrationTest {
}
