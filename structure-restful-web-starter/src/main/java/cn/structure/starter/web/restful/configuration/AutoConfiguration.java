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

import cn.structure.common.utils.IResultUtil;
import cn.structure.common.utils.ResultUtilSimpleImpl;
import cn.structure.starter.web.restful.filter.GlobalBadRequestExceptionHandler;
import cn.structure.starter.web.restful.filter.GlobalControllerAdvice;
import cn.structure.starter.web.restful.properties.RestfulWebConfigProperties;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * 自动装载配置类
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021-01-03
 */
@Configuration
@Import(value = {GlobalBadRequestExceptionHandler.class, GlobalControllerAdvice.class})
@ConditionalOnClass(value = {RestfulWebConfigProperties.class})
public class AutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(IResultUtil.class)
    public IResultUtil iResultUtil() {
        return new ResultUtilSimpleImpl();
    }


    @Bean
    @ConditionalOnMissingBean(HttpMessageConverters.class)
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        //1、定义一个convert转换消息的对象
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        //2、添加fastjson的配置信息
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        //3、空字段显示
        fastJsonConfig.setSerializerFeatures(SerializerFeature.WriteMapNullValue);
        //4、 获取全局序列化配置
        SerializeConfig serializeConfig = SerializeConfig.globalInstance;
        //5、设置long转string
        serializeConfig.put(Long.class, ToStringSerializer.instance);
        serializeConfig.put(Long.TYPE, ToStringSerializer.instance);
        fastJsonConfig.setSerializeConfig(serializeConfig);
        //6、处理中文乱码问题
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        fastConverter.setSupportedMediaTypes(fastMediaTypes);
        //7、在convert中添加配置信息
        fastConverter.setFastJsonConfig(fastJsonConfig);
        //8、将convert添加到converters中
        HttpMessageConverter<?> converter = fastConverter;
        return new HttpMessageConverters(converter);
    }

}
