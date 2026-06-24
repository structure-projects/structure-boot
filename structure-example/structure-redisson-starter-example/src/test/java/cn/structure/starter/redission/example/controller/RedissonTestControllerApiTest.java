package cn.structure.starter.redission.example.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.redisson.api.RedissonClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * <p>
 * RedissonTestController API接口调用测试
 * </p>
 * <p>
 * 覆盖范围: health检查、Bucket操作、Map操作、Object操作、Keys操作、删除操作等
 * </p>
 *
 * @author chuck
 * @since 2025-06-24
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RedissonTestControllerApiTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        RedissonClient mockRedissonClient = mock(RedissonClient.class);
        RedissonTestController controller = new RedissonTestController();
        // 通过反射注入mock的RedissonClient
        try {
            java.lang.reflect.Field field = RedissonTestController.class.getDeclaredField("redissonClient");
            field.setAccessible(true);
            field.set(controller, mockRedissonClient);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject mock RedissonClient", e);
        }
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @Order(1)
    @DisplayName("GET /redisson/health - 健康检查")
    void health_shouldReturnStatusUp() throws Exception {
        mockMvc.perform(get("/redisson/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").exists());
    }

    @Test
    @Order(2)
    @DisplayName("POST /redisson/test/bucket - Bucket操作")
    void testBucket_shouldReturnSuccess() throws Exception {
        mockMvc.perform(post("/redisson/test/bucket")
                        .param("key", "test-key")
                        .param("value", "test-value"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").exists())
                .andExpect(jsonPath("$.operation").value("bucket_set_get"));
    }

    @Test
    @Order(3)
    @DisplayName("GET /redisson/test/bucket/get - Bucket读取")
    void testBucketGet_shouldReturnSuccess() throws Exception {
        mockMvc.perform(get("/redisson/test/bucket/get")
                        .param("key", "test-key"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("bucket_get"));
    }

    @Test
    @Order(4)
    @DisplayName("POST /redisson/test/map - Map操作")
    void testMap_shouldReturnSuccess() throws Exception {
        mockMvc.perform(post("/redisson/test/map")
                        .param("field", "testField")
                        .param("value", "testValue"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").exists())
                .andExpect(jsonPath("$.operation").value("map_put_get"));
    }

    @Test
    @Order(5)
    @DisplayName("POST /redisson/test/map - Map操作缺少参数")
    void testMap_withMissingParams_shouldReturnError() throws Exception {
        mockMvc.perform(post("/redisson/test/map"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("field and value are required"));
    }

    @Test
    @Order(6)
    @DisplayName("GET /redisson/test/map/get - Map读取")
    void testMapGet_shouldReturnSuccess() throws Exception {
        mockMvc.perform(get("/redisson/test/map/get")
                        .param("field", "testField"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("map_get"));
    }

    @Test
    @Order(7)
    @DisplayName("POST /redisson/test/object - Object操作")
    void testObject_shouldReturnSuccess() throws Exception {
        mockMvc.perform(post("/redisson/test/object")
                        .param("id", "1")
                        .param("name", "testName"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").exists())
                .andExpect(jsonPath("$.operation").value("object_bucket_set_get"));
    }

    @Test
    @Order(8)
    @DisplayName("GET /redisson/test/keys - Keys操作")
    void testKeys_shouldReturnSuccess() throws Exception {
        mockMvc.perform(get("/redisson/test/keys"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("keys_get"));
    }

    @Test
    @Order(9)
    @DisplayName("DELETE /redisson/test/delete - 删除操作")
    void testDelete_shouldReturnSuccess() throws Exception {
        mockMvc.perform(delete("/redisson/test/delete")
                        .param("key", "test-key"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").exists())
                .andExpect(jsonPath("$.operation").value("key_delete"));
    }

    @Test
    @Order(10)
    @DisplayName("GET /redisson/test/all - 完整功能测试")
    void testAll_shouldReturnSuccess() throws Exception {
        mockMvc.perform(get("/redisson/test/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").exists())
                .andExpect(jsonPath("$.totalTests").exists())
                .andExpect(jsonPath("$.passedTests").exists());
    }

    @Test
    @Order(11)
    @DisplayName("POST /redisson/test/batch - 批量操作")
    void testBatch_shouldReturnSuccess() throws Exception {
        mockMvc.perform(post("/redisson/test/batch")
                        .param("count", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation").value("batch_insert"));
    }
}
