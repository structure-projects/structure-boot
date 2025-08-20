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
package cn.structured.mybatis.plus.starter.base;

import java.io.Serializable;

/**
 * 带启用停用的接口
 *
 * @author cqliut
 * @version 2023.0713
 * @since 1.0.1
 */
public interface IEnableService {


    /**
     * 启用
     *
     * @param id 主键ID
     */
    void enable(Serializable id);

    /**
     * 停用
     *
     * @param id 主键ID
     */
    void disable(Serializable id);

}
