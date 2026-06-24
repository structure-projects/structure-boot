package cn.structure.example.mybatis.controller;

import cn.structure.example.mybatis.entity.User;
import cn.structure.example.mybatis.service.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * <p>
 * UserController API接口调用测试
 * </p>
 * <p>
 * 覆盖范围: 用户CRUD操作、分页查询等
 * </p>
 *
 * @author chuck
 * @since 2025-06-24
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerApiTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        UserController controller = new UserController();
        IUserService mockUserService = mock(IUserService.class);
        
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testUser");
        
        when(mockUserService.getUserById(1L)).thenReturn(mockUser);
        when(mockUserService.getUserByUsername("test")).thenReturn(mockUser);
        
        List<User> userList = new ArrayList<>();
        userList.add(mockUser);
        when(mockUserService.listUserPage("test", 10, 0)).thenReturn(userList);
        
        try {
            java.lang.reflect.Field serviceField = UserController.class.getDeclaredField("iUserService");
            serviceField.setAccessible(true);
            serviceField.set(controller, mockUserService);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject mock service", e);
        }
        
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @Order(1)
    @DisplayName("GET /user/getUserById - 根据ID获取用户")
    void getUserById_shouldReturnUser() throws Exception {
        mockMvc.perform(get("/user/getUserById")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("testUser"));
    }

    @Test
    @Order(2)
    @DisplayName("GET /user/getUserByUsername - 根据用户名获取用户")
    void getUserByUsername_shouldReturnUser() throws Exception {
        mockMvc.perform(get("/user/getUserByUsername")
                        .param("username", "test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testUser"));
    }

    @Test
    @Order(3)
    @DisplayName("GET /user/list - 用户列表分页查询")
    void listUserPage_shouldReturnUserList() throws Exception {
        mockMvc.perform(get("/user/list")
                        .param("username", "test")
                        .param("pageSize", "10")
                        .param("offset", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @Order(4)
    @DisplayName("POST /user/add - 添加用户")
    void insertUser_shouldReturnSuccess() throws Exception {
        String userJson = "{\"username\":\"testUser\",\"password\":\"password\",\"email\":\"test@example.com\"}";

        mockMvc.perform(post("/user/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("成功！"));
    }

    @Test
    @Order(5)
    @DisplayName("PUT /user/update/{id} - 更新用户")
    void update_shouldReturnSuccess() throws Exception {
        String userJson = "{\"username\":\"updatedUser\",\"password\":\"newPassword\"}";

        mockMvc.perform(put("/user/update/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));
    }

    @Test
    @Order(6)
    @DisplayName("DELETE /user/delete/{id} - 删除用户")
    void delete_shouldReturnSuccess() throws Exception {
        mockMvc.perform(delete("/user/delete/{id}", 999))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SUCCESS"));
    }
}
