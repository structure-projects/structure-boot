/*
 *    Copyright (c) 2018-2025, freelance All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the freelance.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author:
 */

package cn.structure.starter.minio.configuration;

import cn.structure.starter.minio.http.MinioEndpoint;
import cn.structure.starter.minio.properties.MinioProperties;
import cn.structure.starter.minio.service.v1.MinioTemplate;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * <p>
 * minio 自动配置类
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/7/17 14:06
 */
@EnableConfigurationProperties({MinioProperties.class})
public class MinioAutoConfiguration {

    @Autowired
    private MinioProperties minioProperties;

    @Autowired
    private MinioClient minioClient;

    @Primary
    @Bean
    @ConditionalOnMissingBean(MinioClient.class)
    @ConditionalOnProperty(name = "structure.minio.url")
    public MinioClient minioClient() throws Exception {
        return new MinioClient(minioProperties.getUrl(),minioProperties.getAccessKey(),minioProperties.getSecretKey());
    }


    @Bean
    @ConditionalOnMissingBean(MinioTemplate.class)
    @ConditionalOnProperty(name = "structure.minio.url")
    public MinioTemplate minioTemplate() {
        return new MinioTemplate(minioClient);
    }


    @Bean
    @ConditionalOnMissingBean(MinioEndpoint.class)
    @ConditionalOnProperty(name = "structure.minio.endpoint-enable", havingValue = "true")
    public MinioEndpoint minioEndpoint(MinioTemplate template) {
        return new MinioEndpoint(template);
    }

}
