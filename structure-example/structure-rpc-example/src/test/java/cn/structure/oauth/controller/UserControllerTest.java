package cn.structure.oauth.controller;

import cn.structure.oauth.config.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserControllerTest extends AbstractIntegrationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        assertNotNull(applicationContext);
    }

    @Test
    void userControllerBeanExists() {
        UserController userController = applicationContext.getBean(UserController.class);
        assertNotNull(userController);
    }

    @Test
    void oAuthControllerBeanExists() {
        OAuthController oAuthController = applicationContext.getBean(OAuthController.class);
        assertNotNull(oAuthController);
    }

    @Test
    void rpcTestControllerBeanExists() {
        RpcTestController rpcTestController = applicationContext.getBean(RpcTestController.class);
        assertNotNull(rpcTestController);
    }
}
