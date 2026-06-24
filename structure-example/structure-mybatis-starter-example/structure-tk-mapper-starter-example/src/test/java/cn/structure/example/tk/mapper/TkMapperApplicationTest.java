package cn.structure.example.tk.mapper;

import cn.structure.example.tk.mapper.config.AbstractIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>
 * Tk-Mapper 应用上下文集成测试
 * </p>
 * <p>
 * 覆盖范围: Spring应用上下文加载、Tk-Mapper自动配置验证、数据源连接
 * </p>
 *
 * @author chuck
 * @since 2025-06-24
 */
class TkMapperApplicationTest extends AbstractIntegrationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private DataSource dataSource;

    @Test
    @DisplayName("Spring应用上下文加载成功")
    void contextLoads() {
        assertNotNull(applicationContext);
    }

    @Test
    @DisplayName("DataSource Bean加载成功")
    void dataSourceBeanExists() {
        assertNotNull(dataSource);
        assertTrue(applicationContext.containsBean("dataSource"));
    }

    @Test
    @DisplayName("DataSource可以获取连接")
    void dataSource_canGetConnection() throws SQLException {
        assertNotNull(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            assertNotNull(connection);
            assertFalse(connection.isClosed());
        }
    }

    @Test
    @DisplayName("主启动类加载成功且有@MapperScan注解")
    void mainApplicationClass_withMapperScanAnnotation() {
        TkMapperApplication app = applicationContext.getBean(TkMapperApplication.class);
        assertNotNull(app);

        MapperScan mapperScan = TkMapperApplication.class.getAnnotation(MapperScan.class);
        assertNotNull(mapperScan, "@MapperScan should be present on TkMapperApplication");
        assertEquals("cn.structure.example.tk.mapper.dao.**", mapperScan.value()[0]);
    }

    @Test
    @DisplayName("应用包含tk-mapper相关Bean")
    void application_containsTkMapperBeans() {
        // 验证 tk-mapper 自动配置的 MapperFactoryBean 等被加载
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        boolean hasMapperScanner = false;
        for (String name : beanNames) {
            if (name.toLowerCase().contains("mapper") || name.toLowerCase().contains("sqlsession")) {
                hasMapperScanner = true;
                break;
            }
        }
        assertTrue(hasMapperScanner, "Application should contain tk-mapper related beans");
    }
}
