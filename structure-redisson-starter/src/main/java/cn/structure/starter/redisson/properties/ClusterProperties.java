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
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 集群配置
 * </p>
 */
@Getter
@Setter
@ToString
public class ClusterProperties extends MultipleServerProperties {
    /**
     * nat的映射配置
     */
    private Map<String, String> natMap = Collections.emptyMap();
    /**
     * 集群redis节点地址配置
     */
    private List<String> nodeAddresses = new ArrayList();
}
