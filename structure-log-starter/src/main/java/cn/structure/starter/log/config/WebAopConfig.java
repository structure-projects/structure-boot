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
package cn.structure.starter.log.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.ImportResource;

/**
 * <p>
 * WebAop配置入口
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/1/3 15:41
 */
@ImportResource(locations = {"classpath:structure-boot-aop.xml"})
@ConditionalOnProperty(value = "structure.log.aop.enable", matchIfMissing = true)
public class WebAopConfig {

}
