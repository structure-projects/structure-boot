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
package cn.structured.mybatis.plus.starter.configuration;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * 自动装配
 *
 * @author cqliut
 * @version 2022.1024
 * @since 1.0.1
 */
@Slf4j
@Configuration
@ImportAutoConfiguration(value = {SnowflakeProperties.class})
public class AutoSnowflakeConfiguration {

    @Resource
    private SnowflakeProperties snowflakeProperties;

    @Bean
    public Snowflake snowflake() {
        long workerId = 0L;
        long datacenterId = 0L;
        try {
            String localhostStr = NetUtil.getLocalhostStr();
            workerId = Long.parseLong(localhostStr.substring(localhostStr.lastIndexOf(".") + 1));
            datacenterId = workerId / 32;
            workerId = workerId % 32;
            log.info("当前机器的 ipAddress -> {},workerId ->{} ,datacenterId -> {}", localhostStr, workerId, datacenterId);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("当前机器的workerId获取失败 ---> " + e);
            datacenterId = snowflakeProperties.getDatacenterId();
            workerId = snowflakeProperties.getWorkerId();
        }
        return IdUtil.createSnowflake(workerId, datacenterId);
    }

}
