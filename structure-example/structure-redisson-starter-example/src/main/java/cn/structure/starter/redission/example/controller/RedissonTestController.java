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
package cn.structure.starter.redission.example.controller;

import org.redisson.api.RBucket;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Redisson 测试控制器
 * </p>
 *
 * @author chuck
 * @version 1.0.0
 * @since 2025-05-24
 */
@RestController
@RequestMapping("/redisson")
public class RedissonTestController {

    private final RedissonClient redissonClient;

    public RedissonTestController(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    /**
     * 健康检查
     */
    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "UP");
        result.put("message", "Redisson is working");
        return result;
    }

    /**
     * 测试基本的键值操作
     */
    @GetMapping("/bucket/{key}/{value}")
    public Map<String, Object> testBucket(@PathVariable String key, @PathVariable String value) {
        RBucket<String> bucket = redissonClient.getBucket(key);
        bucket.set(value, 60, TimeUnit.SECONDS);
        
        Map<String, Object> result = new HashMap<>();
        result.put("operation", "set");
        result.put("key", key);
        result.put("value", value);
        result.put("ttl", "60 seconds");
        result.put("success", true);
        return result;
    }

    /**
     * 获取键值
     */
    @GetMapping("/bucket/{key}")
    public Map<String, Object> getBucket(@PathVariable String key) {
        RBucket<String> bucket = redissonClient.getBucket(key);
        String value = bucket.get();
        
        Map<String, Object> result = new HashMap<>();
        result.put("key", key);
        result.put("value", value);
        result.put("exists", value != null);
        return result;
    }

    /**
     * 测试Map操作
     */
    @GetMapping("/map/{mapName}/{key}/{value}")
    public Map<String, Object> testMap(@PathVariable String mapName, 
                                       @PathVariable String key, 
                                       @PathVariable String value) {
        RMap<String, String> map = redissonClient.getMap(mapName);
        map.put(key, value);
        
        Map<String, Object> result = new HashMap<>();
        result.put("operation", "map_put");
        result.put("mapName", mapName);
        result.put("key", key);
        result.put("value", value);
        result.put("success", true);
        return result;
    }

    /**
     * 获取Map中的值
     */
    @GetMapping("/map/{mapName}/{key}")
    public Map<String, Object> getMapValue(@PathVariable String mapName, @PathVariable String key) {
        RMap<String, String> map = redissonClient.getMap(mapName);
        String value = map.get(key);
        
        Map<String, Object> result = new HashMap<>();
        result.put("mapName", mapName);
        result.put("key", key);
        result.put("value", value);
        result.put("exists", value != null);
        return result;
    }
}
