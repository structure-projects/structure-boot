package cn.structure.example.web.restful.controller;

import cn.structure.example.web.restful.pojo.vo.ReqTestVO;
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
 * RestTestController API接口调用测试
 * </p>
 * <p>
 * 覆盖范围: POST/GET/PUT/DELETE请求、参数校验、异常处理等
 * </p>
 *
 * @author chuck
 * @since 2025-06-24
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RestTestControllerApiTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        RestTestController controller = new RestTestController();
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    @DisplayName("POST /test/post - 创建测试数据")
    void postTest_shouldReturnSuccess() throws Exception {
        ReqTestVO request = new ReqTestVO();
        request.setId("1");
        request.setName("testName");

        mockMvc.perform(post("/test/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data.id").value("1"))
                .andExpect(jsonPath("$.data.name").value("testName"));
    }

    @Test
    @Order(2)
    @DisplayName("GET /test/get - 获取测试数据")
    void getTest_shouldReturnSuccess() throws Exception {
        mockMvc.perform(get("/test/get")
                        .param("id", "testId"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data.id").value("testId"))
                .andExpect(jsonPath("$.data.name").value("testId"));
    }

    @Test
    @Order(3)
    @DisplayName("DELETE /test/delete/{id} - 删除测试数据")
    void deleteTest_shouldReturnSuccess() throws Exception {
        mockMvc.perform(delete("/test/delete/{id}", "deleteId"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value("SUCCESS"))
                .andExpect(jsonPath("$.data.id").value("deleteId"));
    }

    @Test
    @Order(4)
    @DisplayName("DELETE /test/delete/{id} - id为空字符串")
    void deleteTest_withEmptyId_shouldReturnSuccess() throws Exception {
        mockMvc.perform(delete("/test/delete/{id}", "emptyId"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @Order(5)
    @DisplayName("GET /test/exception - 测试异常返回")
    void exceptionTest_shouldReturnFail() throws Exception {
        mockMvc.perform(get("/test/exception"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value("12001"))
                .andExpect(jsonPath("$.message").value("测试异常"));
    }

    @Test
    @Order(6)
    @DisplayName("GET /test/fail - 参数id不为null时成功")
    void failTest_withValidId_shouldReturnSuccess() throws Exception {
        mockMvc.perform(get("/test/fail")
                        .param("id", "validId"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value("SUCCESS"));
    }

    @Test
    @Order(7)
    @DisplayName("POST /test/post - 参数校验失败（缺少必填字段）")
    void postTest_withMissingFields_shouldReturnValidationError() throws Exception {
        mockMvc.perform(post("/test/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}
