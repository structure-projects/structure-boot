package cn.structure.example.mybatis.controller;

import cn.structure.example.mybatis.config.AbstractIntegrationTest;
import cn.structure.example.mybatis.entity.User;
import cn.structure.example.mybatis.mapper.IUserMapper;
import cn.structure.example.mybatis.service.IUserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>
 * User Controller 集成测试 - 验证控制器Bean加载和依赖注入
 * </p>
 * <p>
 * 覆盖范围: 控制器组件的加载、Bean注入、业务Bean的可用性
 * </p>
 *
 * @author chuck
 * @since 2025-06-24
 */
class UserControllerTest extends AbstractIntegrationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    @DisplayName("Spring应用上下文加载成功")
    void contextLoads() {
        assertNotNull(applicationContext);
    }

    @Test
    @DisplayName("UserController Bean存在且可用")
    void userControllerBeanExists() {
        assertTrue(applicationContext.containsBean("userController"));
        UserController userController = applicationContext.getBean(UserController.class);
        assertNotNull(userController);
    }

    @Test
    @DisplayName("MyBatisTestController Bean存在且可用")
    void myBatisTestControllerBeanExists() {
        assertTrue(applicationContext.containsBean("myBatisTestController"));
        MyBatisTestController controller = applicationContext.getBean(MyBatisTestController.class);
        assertNotNull(controller);
    }

    @Test
    @DisplayName("UserService Bean存在且可用")
    void userServiceBeanExists() {
        assertTrue(applicationContext.containsBean("userServiceImpl"));
        IUserService userService = applicationContext.getBean(IUserService.class);
        assertNotNull(userService);
    }

    @Test
    @DisplayName("UserMapper Bean存在且可用")
    void userMapperBeanExists() {
        assertTrue(applicationContext.containsBean("IUserMapper"));
        IUserMapper userMapper = applicationContext.getBean(IUserMapper.class);
        assertNotNull(userMapper);
    }
}
