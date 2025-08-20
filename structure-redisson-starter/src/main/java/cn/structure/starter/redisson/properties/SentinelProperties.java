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
package cn.structure.starter.redisson.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 哨兵模式
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2020-12-23
 */
@Getter
@Setter
@ToString
public class SentinelProperties extends MultipleServerProperties {
    /**
     * 哨兵模式的地址
     */
    private List<String> sentinelAddresses = new ArrayList();
    /**
     * 哨兵模式的名字
     */
    private String masterName;
    /**
     * 节点变化扫描间隔时间
     */
    private int scanInterval = 1000;
}
