package cn.structure.example.redis.controller;

import cn.structure.example.redis.service.RedisLockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * <p>
 * RedisController API接口调用测试
 * </p>
 * <p>
 * 覆盖范围: 获取key、Redis锁操作等
 * </p>
 *
 * @author chuck
 * @since 2025-06-24
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RedisControllerApiTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        RedisController controller = new RedisController();
        
        RedisTemplate<String, String> mockRedisTemplate = mock(RedisTemplate.class);
        RedisLockService mockRedisLockService = mock(RedisLockService.class);
        ValueOperations<String, String> mockValueOps = mock(ValueOperations.class);
        
        when(mockRedisTemplate.opsForValue()).thenReturn(mockValueOps);
        when(mockValueOps.get("testKey")).thenReturn("testKey");
        when(mockValueOps.get("")).thenReturn(null);
        
        try {
            java.lang.reflect.Field redisTemplateField = RedisController.class.getDeclaredField("redisTemplate");
            redisTemplateField.setAccessible(true);
            redisTemplateField.set(controller, mockRedisTemplate);
            
            java.lang.reflect.Field redisLockServiceField = RedisController.class.getDeclaredField("redisLockService");
            redisLockServiceField.setAccessible(true);
            redisLockServiceField.set(controller, mockRedisLockService);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject mock dependencies", e);
        }
        
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @Order(1)
    @DisplayName("GET /redis/getKey - 获取key")
    void getKey_shouldReturnSuccess() throws Exception {
        mockMvc.perform(get("/redis/getKey")
                        .param("key", "testKey"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    @DisplayName("GET /redis/getKey - key为空")
    void getKey_withEmptyKey_shouldReturnSuccess() throws Exception {
        mockMvc.perform(get("/redis/getKey")
                        .param("key", ""))
                .andExpect(status().isOk());
    }

    @Test
    @Order(3)
    @DisplayName("GET /redis/redisLock - Redis锁操作")
    void redisLock_shouldReturnSuccess() throws Exception {
        mockMvc.perform(get("/redis/redisLock")
                        .param("key", "lockKey"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(4)
    @DisplayName("GET /redis/redisLock1 - Redis锁操作(对象参数)")
    void redisLock1_shouldReturnSuccess() throws Exception {
        mockMvc.perform(get("/redis/redisLock1")
                        .param("key", "lockKey1"))
                .andExpect(status().isOk());
    }
}
