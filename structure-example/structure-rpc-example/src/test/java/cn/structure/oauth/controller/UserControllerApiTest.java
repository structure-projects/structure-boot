package cn.structure.oauth.controller;

import cn.structure.oauth.dto.UserCreateRequest;
import cn.structure.oauth.dto.UserUpdateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * <p>
 * UserController API接口调用测试
 * </p>
 * <p>
 * 覆盖范围: 用户CRUD接口、RESTful接口、权限接口等
 * </p>
 *
 * @author chuck
 * @since 2025-06-24
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerApiTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        UserController userController = new UserController();
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    @DisplayName("GET /api/user/getById/{id} - 根据ID获取用户")
    void getUserById_shouldReturnUser() throws Exception {
        mockMvc.perform(get("/api/user/getById/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("user_1"))
                .andExpect(jsonPath("$.email").value("user1@example.com"))
                .andExpect(jsonPath("$.status").value(1))
                .andExpect(jsonPath("$.createTime").exists())
                .andExpect(jsonPath("$.updateTime").exists());
    }

    @Test
    @Order(2)
    @DisplayName("GET /api/user/getById/{id} - ID为0")
    void getUserById_withZeroId_shouldReturnUser() throws Exception {
        mockMvc.perform(get("/api/user/getById/{id}", 0))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(0))
                .andExpect(jsonPath("$.username").value("user_0"));
    }

    @Test
    @Order(3)
    @DisplayName("GET /api/user/getByUsername - 根据用户名获取用户")
    void getUserByUsername_shouldReturnUser() throws Exception {
        mockMvc.perform(get("/api/user/getByUsername")
                        .param("username", "testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("testuser@example.com"))
                .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    @Order(4)
    @DisplayName("GET /api/user/getByUsername - 用户名为空")
    void getUserByUsername_withEmptyUsername_shouldReturnUser() throws Exception {
        mockMvc.perform(get("/api/user/getByUsername")
                        .param("username", ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(""))
                .andExpect(jsonPath("$.email").value("@example.com"));
    }

    @Test
    @Order(5)
    @DisplayName("POST /api/user/create - 创建用户")
    void createUser_shouldReturnCreatedUser() throws Exception {
        UserCreateRequest request = new UserCreateRequest();
        request.setUsername("newuser");
        request.setEmail("newuser@example.com");
        request.setPhone("13800138000");

        mockMvc.perform(post("/api/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.username").value("newuser"))
                .andExpect(jsonPath("$.email").value("newuser@example.com"))
                .andExpect(jsonPath("$.phone").value("13800138000"))
                .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    @Order(6)
    @DisplayName("POST /api/user/create - 请求体为空")
    void createUser_withEmptyRequest_shouldReturnUser() throws Exception {
        mockMvc.perform(post("/api/user/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @Order(7)
    @DisplayName("PUT /api/user/update/{id} - 更新用户信息")
    void updateUser_shouldReturnUpdatedUser() throws Exception {
        UserUpdateRequest request = new UserUpdateRequest();
        request.setEmail("updated@example.com");
        request.setPhone("13900139000");
        request.setStatus(0);

        mockMvc.perform(put("/api/user/update/{id}", 100)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.email").value("updated@example.com"))
                .andExpect(jsonPath("$.phone").value("13900139000"))
                .andExpect(jsonPath("$.status").value(0));
    }

    @Test
    @Order(8)
    @DisplayName("DELETE /api/user/delete/{id} - 删除用户")
    void deleteUserById_shouldReturnSuccess() throws Exception {
        mockMvc.perform(delete("/api/user/delete/{id}", 100))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.message").value("User deleted successfully"));
    }

    @Test
    @Order(9)
    @DisplayName("GET /api/user/{userId} - RESTful获取用户")
    void getUserByIdRest_shouldReturnUser() throws Exception {
        mockMvc.perform(get("/api/user/{userId}", 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.username").value("user_2"))
                .andExpect(jsonPath("$.email").value("user2@example.com"));
    }

    @Test
    @Order(10)
    @DisplayName("POST /api/user - RESTful创建用户")
    void createUserRest_shouldReturnCreatedUser() throws Exception {
        UserCreateRequest request = new UserCreateRequest();
        request.setUsername("restuser");
        request.setEmail("restuser@example.com");

        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.username").value("restuser"))
                .andExpect(jsonPath("$.email").value("restuser@example.com"))
                .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    @Order(11)
    @DisplayName("PUT /api/user/{userId} - RESTful更新用户")
    void updateUserRest_shouldReturnUpdatedUser() throws Exception {
        UserUpdateRequest request = new UserUpdateRequest();
        request.setEmail("rest_updated@example.com");
        request.setStatus(0);

        mockMvc.perform(put("/api/user/{userId}", 200)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(200))
                .andExpect(jsonPath("$.email").value("rest_updated@example.com"))
                .andExpect(jsonPath("$.status").value(0));
    }

    @Test
    @Order(12)
    @DisplayName("PATCH /api/user/{userId} - 部分更新用户")
    void partialUpdateUser_shouldReturnUpdatedUser() throws Exception {
        UserUpdateRequest request = new UserUpdateRequest();
        request.setPhone("13700137000");

        mockMvc.perform(patch("/api/user/{userId}", 300)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(300))
                .andExpect(jsonPath("$.phone").value("13700137000"));
    }

    @Test
    @Order(13)
    @DisplayName("DELETE /api/user/{userId} - RESTful删除用户")
    void deleteUserRest_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/user/{userId}", 400))
                .andExpect(status().isOk());
    }

    @Test
    @Order(14)
    @DisplayName("GET /api/user/me - 获取当前用户")
    void getCurrentUser_shouldReturnAdminUser() throws Exception {
        mockMvc.perform(get("/api/user/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("admin"))
                .andExpect(jsonPath("$.email").value("admin@example.com"))
                .andExpect(jsonPath("$.phone").value("13800138000"));
    }

    @Test
    @Order(15)
    @DisplayName("POST /api/user/{userId}/lock - 锁定用户")
    void lockUser_shouldReturnLockedUser() throws Exception {
        mockMvc.perform(post("/api/user/{userId}/lock", 500))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(500))
                .andExpect(jsonPath("$.status").value(0));
    }

    @Test
    @Order(16)
    @DisplayName("POST /api/user/{userId}/unlock - 解锁用户")
    void unlockUser_shouldReturnUnlockedUser() throws Exception {
        mockMvc.perform(post("/api/user/{userId}/unlock", 500))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(500))
                .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    @Order(17)
    @DisplayName("DELETE /api/user/{userId}/roles/{roleId} - 撤销角色")
    void revokeRole_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/user/{userId}/roles/{roleId}", 1, 1))
                .andExpect(status().isOk());
    }

    @Test
    @Order(18)
    @DisplayName("DELETE /api/role/{roleId} - 删除角色")
    void deleteRole_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/role/{roleId}", 1))
                .andExpect(status().isOk());
    }
}
