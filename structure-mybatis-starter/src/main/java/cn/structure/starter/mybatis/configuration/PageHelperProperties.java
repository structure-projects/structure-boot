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

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 分页配置
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
public class PageHelperProperties {
    private String helperDialect = "mysql";
    private String reasonable = "true";
    private String pageSizeZero = "true";
    private String supportMethodsArguments = "true";
    private String params = "count=countSql";
}
