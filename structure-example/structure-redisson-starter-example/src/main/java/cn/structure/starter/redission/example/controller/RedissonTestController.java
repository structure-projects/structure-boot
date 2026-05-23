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

import jakarta.annotation.Resource;
import org.redisson.api.RBucket;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Redisson 测试控制器 - 完整功能测试
 * </p>
 *
 * @author chuck
 * @version 1.0.0
 * @since 2025-05-24
 */
@RestController
@RequestMapping("/redisson")
public class RedissonTestController {

    @Resource
    private RedissonClient redissonClient;

    private static final String TEST_KEY_PREFIX = "test:redisson:";
    private static final String TEST_MAP_NAME = "test:redisson:map";
    private static final long DEFAULT_TIMEOUT = 30;

    /**
     * 健康检查
     */
    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> result = new HashMap<>();
        try {
            // 测试 Redisson 连接
            redissonClient.getKeys().count();
            result.put("status", "UP");
            result.put("framework", "Redisson");
            result.put("timestamp", System.currentTimeMillis());
        } catch (Exception e) {
            result.put("status", "DOWN");
            result.put("error", e.getMessage());
        }
        return result;
    }

    /**
     * 测试1: String Bucket 操作
     */
    @PostMapping("/test/bucket")
    public Map<String, Object> testBucket(@RequestParam(value = "key", defaultValue = "string-key") String key,
                                          @RequestParam(value = "value", defaultValue = "test-value") String value) {
        Map<String, Object> result = new HashMap<>();
        String fullKey = TEST_KEY_PREFIX + key;
        try {
            RBucket<String> bucket = redissonClient.getBucket(fullKey);
            bucket.set(value, DEFAULT_TIMEOUT, TimeUnit.SECONDS);
            
            String retrievedValue = bucket.get();
            boolean success = value.equals(retrievedValue);
            
            result.put("success", success);
            result.put("operation", "bucket_set_get");
            result.put("key", fullKey);
            result.put("storedValue", value);
            result.put("retrievedValue", retrievedValue);
            result.put("message", success ? "Bucket test passed" : "Value mismatch");
        } catch (Exception e) {
            result.put("success", false);
            result.put("operation", "bucket_set_get");
            result.put("error", e.getMessage());
        }
        return result;
    }

    /**
     * 测试2: Bucket 读取
     */
    @GetMapping("/test/bucket/get")
    public Map<String, Object> testBucketGet(@RequestParam(value = "key", defaultValue = "string-key") String key) {
        Map<String, Object> result = new HashMap<>();
        String fullKey = TEST_KEY_PREFIX + key;
        try {
            RBucket<String> bucket = redissonClient.getBucket(fullKey);
            String value = bucket.get();
            
            result.put("success", value != null);
            result.put("operation", "bucket_get");
            result.put("key", fullKey);
            result.put("value", value);
            result.put("message", value != null ? "Value retrieved" : "Key not found");
        } catch (Exception e) {
            result.put("success", false);
            result.put("operation", "bucket_get");
            result.put("error", e.getMessage());
        }
        return result;
    }

    /**
     * 测试3: Map 操作
     */
    @PostMapping("/test/map")
    public Map<String, Object> testMap(@RequestParam(value = "mapKey", defaultValue = "") String mapKey,
                                       @RequestParam(value = "field", defaultValue = "") String field,
                                       @RequestParam(value = "value", defaultValue = "") String value) {
        Map<String, Object> result = new HashMap<>();
        String fullMapName = TEST_KEY_PREFIX + (mapKey.isEmpty() ? TEST_MAP_NAME : mapKey);
        
        if (field.isEmpty() || value.isEmpty()) {
            result.put("success", false);
            result.put("message", "field and value are required");
            return result;
        }
        
        try {
            RMap<String, String> map = redissonClient.getMap(fullMapName);
            map.put(field, value, DEFAULT_TIMEOUT, TimeUnit.SECONDS);
            
            String retrievedValue = map.get(field);
            boolean success = value.equals(retrievedValue);
            
            result.put("success", success);
            result.put("operation", "map_put_get");
            result.put("mapName", fullMapName);
            result.put("field", field);
            result.put("storedValue", value);
            result.put("retrievedValue", retrievedValue);
            result.put("message", success ? "Map test passed" : "Value mismatch");
        } catch (Exception e) {
            result.put("success", false);
            result.put("operation", "map_put_get");
            result.put("error", e.getMessage());
        }
        return result;
    }

    /**
     * 测试4: Map 读取
     */
    @GetMapping("/test/map/get")
    public Map<String, Object> testMapGet(@RequestParam(value = "mapKey", defaultValue = "") String mapKey,
                                          @RequestParam(value = "field", defaultValue = "") String field) {
        Map<String, Object> result = new HashMap<>();
        String fullMapName = TEST_KEY_PREFIX + (mapKey.isEmpty() ? TEST_MAP_NAME : mapKey);
        
        try {
            RMap<String, String> map = redissonClient.getMap(fullMapName);
            String value = map.get(field);
            
            result.put("success", value != null);
            result.put("operation", "map_get");
            result.put("mapName", fullMapName);
            result.put("field", field);
            result.put("value", value);
            result.put("message", value != null ? "Value retrieved" : "Field not found");
        } catch (Exception e) {
            result.put("success", false);
            result.put("operation", "map_get");
            result.put("error", e.getMessage());
        }
        return result;
    }

    /**
     * 测试5: Object Bucket 操作
     */
    @PostMapping("/test/object")
    public Map<String, Object> testObject(@RequestParam(value = "key", defaultValue = "object-key") String key,
                                         @RequestParam(value = "id", defaultValue = "1") String id,
                                         @RequestParam(value = "name", defaultValue = "Test Object") String name) {
        Map<String, Object> result = new HashMap<>();
        String fullKey = TEST_KEY_PREFIX + key;
        
        try {
            TestVO obj = new TestVO();
            obj.setId(id);
            obj.setName(name);
            
            RBucket<TestVO> bucket = redissonClient.getBucket(fullKey);
            bucket.set(obj, DEFAULT_TIMEOUT, TimeUnit.SECONDS);
            
            TestVO retrievedObj = bucket.get();
            boolean success = retrievedObj != null && 
                              id.equals(retrievedObj.getId()) && 
                              name.equals(retrievedObj.getName());
            
            result.put("success", success);
            result.put("operation", "object_bucket_set_get");
            result.put("key", fullKey);
            result.put("storedObject", obj);
            result.put("retrievedObject", retrievedObj);
            result.put("message", success ? "Object test passed" : "Object mismatch");
        } catch (Exception e) {
            result.put("success", false);
            result.put("operation", "object_bucket_set_get");
            result.put("error", e.getMessage());
        }
        return result;
    }

    /**
     * 测试6: 键操作
     */
    @GetMapping("/test/keys")
    public Map<String, Object> testKeys(@RequestParam(value = "pattern", defaultValue = "test:redisson:*") String pattern) {
        Map<String, Object> result = new HashMap<>();
        try {
            Iterable<String> keys = redissonClient.getKeys().getKeysByPattern(pattern);
            List<String> keyList = new ArrayList<>();
            keys.forEach(keyList::add);
            
            result.put("success", true);
            result.put("operation", "keys_get");
            result.put("pattern", pattern);
            result.put("count", keyList.size());
            result.put("keys", keyList);
            result.put("message", "Found " + keyList.size() + " keys");
        } catch (Exception e) {
            result.put("success", false);
            result.put("operation", "keys_get");
            result.put("error", e.getMessage());
        }
        return result;
    }

    /**
     * 测试7: 删除键
     */
    @DeleteMapping("/test/delete")
    public Map<String, Object> testDelete(@RequestParam(value = "key", defaultValue = "string-key") String key) {
        Map<String, Object> result = new HashMap<>();
        String fullKey = TEST_KEY_PREFIX + key;
        
        try {
            boolean deleted = redissonClient.getBucket(fullKey).delete();
            
            result.put("success", deleted);
            result.put("operation", "key_delete");
            result.put("key", fullKey);
            result.put("message", deleted ? "Key deleted" : "Key not found or already deleted");
        } catch (Exception e) {
            result.put("success", false);
            result.put("operation", "key_delete");
            result.put("error", e.getMessage());
        }
        return result;
    }

    /**
     * 测试8: 完整功能测试
     */
    @GetMapping("/test/all")
    public Map<String, Object> testAll() {
        Map<String, Object> result = new HashMap<>();
        int totalTests = 0;
        int passedTests = 0;
        String timestamp = String.valueOf(System.currentTimeMillis());

        // Test 1: Bucket operations
        try {
            String testKey = "bucket_test_" + timestamp;
            RBucket<String> bucket = redissonClient.getBucket(TEST_KEY_PREFIX + testKey);
            bucket.set("test_value", DEFAULT_TIMEOUT, TimeUnit.SECONDS);
            String value = bucket.get();
            boolean success = "test_value".equals(value);
            result.put("test1_bucket", success);
            totalTests++;
            if (success) passedTests++;
        } catch (Exception e) {
            result.put("test1_bucket", "FAILED: " + e.getMessage());
            totalTests++;
        }

        // Test 2: Map operations
        try {
            String mapName = TEST_KEY_PREFIX + "map_test_" + timestamp;
            RMap<String, String> map = redissonClient.getMap(mapName);
            map.put("field1", "value1", DEFAULT_TIMEOUT, TimeUnit.SECONDS);
            String value = map.get("field1");
            boolean success = "value1".equals(value);
            result.put("test2_map", success);
            totalTests++;
            if (success) passedTests++;
        } catch (Exception e) {
            result.put("test2_map", "FAILED: " + e.getMessage());
            totalTests++;
        }

        // Test 3: Object bucket
        try {
            TestVO obj = new TestVO();
            obj.setId("test-id");
            obj.setName("test-name");
            RBucket<TestVO> bucket = redissonClient.getBucket(TEST_KEY_PREFIX + "obj_test_" + timestamp);
            bucket.set(obj, DEFAULT_TIMEOUT, TimeUnit.SECONDS);
            TestVO retrieved = bucket.get();
            boolean success = retrieved != null && "test-id".equals(retrieved.getId());
            result.put("test3_object", success);
            totalTests++;
            if (success) passedTests++;
        } catch (Exception e) {
            result.put("test3_object", "FAILED: " + e.getMessage());
            totalTests++;
        }

        // Test 4: Keys count
        try {
            long count = redissonClient.getKeys().count();
            boolean success = count >= 0;
            result.put("test4_keys_count", success);
            result.put("keysCount", count);
            totalTests++;
            if (success) passedTests++;
        } catch (Exception e) {
            result.put("test4_keys_count", "FAILED: " + e.getMessage());
            totalTests++;
        }

        result.put("totalTests", totalTests);
        result.put("passedTests", passedTests);
        result.put("failedTests", totalTests - passedTests);
        result.put("success", passedTests == totalTests);

        return result;
    }

    /**
     * 测试9: 性能测试 - 批量操作
     */
    @PostMapping("/test/batch")
    public Map<String, Object> testBatch(@RequestParam(value = "count", defaultValue = "100") int count) {
        Map<String, Object> result = new HashMap<>();
        String timestamp = String.valueOf(System.currentTimeMillis());
        
        try {
            long startTime = System.currentTimeMillis();
            String mapName = TEST_KEY_PREFIX + "batch_test_" + timestamp;
            RMap<String, String> map = redissonClient.getMap(mapName);
            
            for (int i = 0; i < count; i++) {
                map.put("key_" + i, "value_" + i);
            }
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            result.put("success", true);
            result.put("operation", "batch_insert");
            result.put("count", count);
            result.put("duration_ms", duration);
            result.put("ops_per_second", count * 1000.0 / duration);
            result.put("message", "Batch insert completed in " + duration + "ms");
        } catch (Exception e) {
            result.put("success", false);
            result.put("operation", "batch_insert");
            result.put("error", e.getMessage());
        }
        return result;
    }
}
