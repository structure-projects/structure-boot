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
package cn.structure.example.tk.mapper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * <p>
 * 启动Tk-Mapper例子入口
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2020/12/27 20:28
 */
@MapperScan("cn.structure.example.tk.mapper.dao.**")
@SpringBootApplication
public class TkMapperApplication {
    public static void main(String[] args) {
        SpringApplication.run(TkMapperApplication.class, args);
    }
}
