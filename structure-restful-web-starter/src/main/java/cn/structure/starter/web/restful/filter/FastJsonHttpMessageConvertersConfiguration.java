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
package cn.structure.starter.web.restful.filter;

import cn.structure.starter.web.restful.annotation.EnableFastJsonHttpConverters;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * FastJson消息转换器配置
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021-01-03
 */
@Slf4j
@Configuration
public class FastJsonHttpMessageConvertersConfiguration {

    /**
     * 创建FastJsonHttpMessageConverter
     *
     * @param enableFastJsonHttpConverters 注解配置
     * @return FastJsonHttpMessageConverter
     */
    @Bean
    @ConditionalOnMissingBean(FastJsonHttpMessageConverter.class)
    public FastJsonHttpMessageConverter fastJsonHttpMessageConverter(EnableFastJsonHttpConverters enableFastJsonHttpConverters) {
        log.info("[FastJsonHttpMessageConverters] 初始化FastJsonHttpMessageConverter - longToString: {}, nullShowValue: {}",
                enableFastJsonHttpConverters.longToString(), enableFastJsonHttpConverters.nullShowValue());

        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();

        if (enableFastJsonHttpConverters.nullShowValue()) {
            fastJsonConfig.setSerializerFeatures(SerializerFeature.WriteMapNullValue);
        }

        SerializeConfig serializeConfig = SerializeConfig.globalInstance;
        if (enableFastJsonHttpConverters.longToString()) {
            serializeConfig.put(Long.class, ToStringSerializer.instance);
            serializeConfig.put(Long.TYPE, ToStringSerializer.instance);
        }
        fastJsonConfig.setSerializeConfig(serializeConfig);

        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON);
        fastConverter.setSupportedMediaTypes(fastMediaTypes);
        fastConverter.setFastJsonConfig(fastJsonConfig);

        return fastConverter;
    }
}