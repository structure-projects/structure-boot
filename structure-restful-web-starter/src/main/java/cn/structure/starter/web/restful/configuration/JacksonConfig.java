/*
 * Copyright (c) 2025 Structure Boot
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.structure.starter.web.restful.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.module.SimpleModule;
import tools.jackson.databind.ser.std.ToStringSerializer;

/**
 * <p>
 * Jackson 序列化配置（基于 Spring Boot 4 / Jackson 3）
 * </p>
 * <p>
 * 通过 {@link JsonMapperBuilderCustomizer} 自定义 {@link tools.jackson.databind.json.JsonMapper} 构建：
 * <ul>
 *   <li>将 Long/long 序列化为 String 字符串，避免 JavaScript 精度丢失</li>
 *   <li>空字段也输出（{@link JsonInclude.Include#ALWAYS}）</li>
 * </ul>
 * 替代原 FastJson 消息转换器实现。
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2025-08-01
 */
@Slf4j
@Configuration
public class JacksonConfig {

    /**
     * 自定义 JsonMapper 构建器
     * 替代原 FastJson 消息转换器实现，统一使用 Jackson 3 序列化
     *
     * @return JsonMapperBuilderCustomizer
     */
    @Bean
    public JsonMapperBuilderCustomizer jacksonCustomizer() {
        return builder -> {
            log.info("[JacksonConfig] 初始化JsonMapperBuilderCustomizer - longToString: true, nullShowValue: true");
            // Long/long -> String，避免 JavaScript 精度丢失
            SimpleModule longToStringModule = new SimpleModule();
            longToStringModule.addSerializer(Long.class, ToStringSerializer.instance);
            longToStringModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
            builder.addModule(longToStringModule);
            // 空字段也输出
            builder.changeDefaultPropertyInclusion(value -> value.withValueInclusion(JsonInclude.Include.ALWAYS));
        };
    }
}
