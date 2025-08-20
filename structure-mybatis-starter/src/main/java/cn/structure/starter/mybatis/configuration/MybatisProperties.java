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
package cn.structure.starter.mybatis.configuration;

import cn.structure.starter.mybatis.enums.GenerateIdType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;


/**
 * <p>
 * mybatis 配置
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2020/12/26 23:47
 */
@Getter
@Setter
@ToString
@Configuration
@ConfigurationProperties(prefix = "structure.mybatis.plugin")
@ImportAutoConfiguration(value = PageHelperProperties.class)
public class MybatisProperties {

    /**
     * 分页配置
     */
    @NestedConfigurationProperty
    private PageHelperProperties page = new PageHelperProperties();

    /**
     * 生成id类型
     */
    private GenerateIdType generateIdType = GenerateIdType.NONE;

    /**
     * 数据中心码
     */
    private Integer dataCenter = 0;

    /**
     * 机器码
     */
    private Integer machine = 0;

    /**
     * 按照时间分表的分表长度
     */
    private Integer splitLength = 2;

    /**
     * 是否开启分表插件
     */
    private Boolean split = false;

    /**
     * 是否开启sql重写
     */
    private Boolean overWrite = false;

}