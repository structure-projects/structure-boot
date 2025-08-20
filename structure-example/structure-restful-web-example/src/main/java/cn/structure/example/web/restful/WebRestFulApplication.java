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
package cn.structure.example.web.restful;

import cn.structure.starter.web.restful.annotation.EnableFastJsonHttpConverters;
import cn.structure.starter.web.restful.annotation.EnableSwagger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * webRestful 启动器
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/1/3 21:25
 */
@EnableSwagger
@EnableFastJsonHttpConverters
@SpringBootApplication
public class WebRestFulApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebRestFulApplication.class, args);
    }
}
