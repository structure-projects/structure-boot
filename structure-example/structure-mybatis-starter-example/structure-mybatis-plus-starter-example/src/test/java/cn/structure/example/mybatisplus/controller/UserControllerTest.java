package cn.structure.example.mybatisplus.controller;

import cn.structure.example.mybatisplus.config.AbstractIntegrationTest;
import cn.structure.example.mybatisplus.mapper.UserMapper;
import cn.structure.example.mybatisplus.pojo.po.User;
import cn.structure.example.mybatisplus.service.IUserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>
 * UserController 增强测试 - 验证Controller业务规则的正确执行
 * </p>
 * <p>
 * 覆盖范围: 控制器组件的加载、Controller依赖的业务Bean功能验证、CRUD业务规则
 * </p>
 *
 * @author chuck
 * @since 2025-06-24
 */
class UserControllerTest extends AbstractIntegrationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private IUserService userService;

    @Autowired
    private UserMapper userMapper;

    private User createTestUser(String username) {
        User user = new User();
        user.setUsername(username);
        user.setPassword("password");
        user.setUnexpired(true);
        user.setEnabled(true);
        user.setUnlocked(true);
        user.setDeleted(false);
        return user;
    }

    private String uniqueUsername(String prefix) {
        return prefix + "_" + System.nanoTime() + "_" + UUID.randomUUID().toString().substring(0, 8);
    }

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
    @DisplayName("MyBatisPlusTestController Bean存在且可用")
    void myBatisPlusTestControllerBeanExists() {
        assertTrue(applicationContext.containsBean("myBatisPlusTestController"));
        MyBatisPlusTestController controller = applicationContext.getBean(MyBatisPlusTestController.class);
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
        assertTrue(applicationContext.containsBean("userMapper"));
        UserMapper userMapper = applicationContext.getBean(UserMapper.class);
        assertNotNull(userMapper);
    }

    @Test
    @DisplayName("Controller层依赖 - 业务Bean协作正常")
    void controllerDependencies_workCorrectly() {
        // 验证 Controller 依赖的 Service 和 Mapper 之间协作正常
        String username = uniqueUsername("controllerDep");
        userService.save(createTestUser(username));

        // Controller getUserById 内部调用 service.getById
        User foundByService = userService.getById(userMapper.getUserByUsername(username).getId());
        assertNotNull(foundByService);

        // Controller getUserByUsername 内部调用 service.getUserByUsername
        User foundByMapper = userMapper.getUserByUsername(username);
        assertNotNull(foundByMapper);
        assertEquals(foundByService.getId(), foundByMapper.getId());
    }

    @Test
    @DisplayName("Controller层业务 - 增删改查链路验证")
    void controllerCrudChain_shouldWork() {
        // 模拟 Controller 调用链路
        // 1. POST /user/add -> service.save
        String username = uniqueUsername("controllerCrud");
        User user = createTestUser(username);
        boolean saveResult = userService.save(user);
        assertTrue(saveResult);
        assertNotNull(user.getId());

        // 2. GET /user/getUserById?id=X -> service.getById
        User found = userService.getById(user.getId());
        assertNotNull(found);
        assertEquals(username, found.getUsername());

        // 3. PUT /user/update/{id} -> service.updateById
        found.setEnabled(false);
        boolean updateResult = userService.updateById(found);
        assertTrue(updateResult);

        // 4. DELETE /user/delete/{id} -> service.removeById
        boolean deleteResult = userService.removeById(found.getId());
        assertTrue(deleteResult);

        // 验证删除后查不到
        assertNull(userService.getById(found.getId()));
    }
}
